package transpiler

import MalformedList
import UnexpectedValue
import Value
import ValueType
import transpiler.codeblocks.header.DFHeader
import DFLine
import DFProgram
import transpiler.codeblocks.header.EntityEvent
import transpiler.codeblocks.header.Function
import transpiler.codeblocks.header.PlayerEvent
import transpiler.codeblocks.header.Process
import transpiler.codeblocks.normal.*
import transpiler.values.*

/**
 * Stores context for a check/parse.
 */
data class CheckContext(val header: DFHeader, val blockType: String, val blockAction: String)

/**
 * Transpiles a list of parsed values into a DF Program.
 */
fun transpile(input: List<Value>): DFProgram {
    val lines = mutableListOf<DFLine>()
    for (v in input)
        lines.add(parseLine(v))
    return DFProgram(lines)
}

/**
 * Takes a parsed value and attempts to transpile it into a DFLine
 */
fun parseLine(input: Value): DFLine {
    // Validate
    if (input.type !is ValueType.VList) throw UnexpectedValue("List", input)
    if (input.type.inner.isEmpty()) throw MalformedList("Code Line", "(Header Codeblock*)", input)

    val header = parseHeader(input.type.inner[0])
    val blocks = input.type.inner.filterIndexed { index, _ -> index >= 1 }.flatMap {
        parseBlock(it, header)
    }
    return when (header) {
        is Function -> {
            val newBlocks = blocks.toMutableList()
            newBlocks.add(0,
                SetVar("+=", listOf(Variable("^depth ${header.name}", VariableScope.LOCAL)))
            )
            newBlocks.add(SetVar("-=", listOf(Variable("^depth ${header.name}", VariableScope.LOCAL))))
            DFLine(header, newBlocks)
        }
        is Process -> {
            val newBlocks = blocks.toMutableList()
            newBlocks.add(0,
                SetVar("+=", listOf(Variable("^depth ${header.name}", VariableScope.LOCAL)))
            )
            newBlocks.add(SetVar("-=", listOf(Variable("^depth ${header.name}", VariableScope.LOCAL))))
            DFLine(header, newBlocks)
        }
        else -> DFLine(header, blocks)
    }


}

/**
 * Takes a parsed value and attempts to transpile it into a DFHeader
 */
fun parseHeader(input: Value): DFHeader {
    // Validate
    if (input.type !is ValueType.VList) throw UnexpectedValue("List", input)
    if (input.type.inner.size != 2) throw MalformedList("Header", "(Identifier String)", input)

    val first = input.type.inner[0]
    val second = input.type.inner[1]

    // Validate
    if (first.type !is ValueType.Ident) throw UnexpectedValue("Identifier", first)
    if (second.type !is ValueType.Str) throw UnexpectedValue("Identifier", second)

    return when (first.type.str) {
        "player-event" -> PlayerEvent(second.type.str)
        "entity-event" -> EntityEvent(second.type.str)
        "def" -> Function(second.type.str)
        "process" -> Process(second.type.str)
        else -> throw UnexpectedValue("a valid header type", first)
    }
}

/**
 * Takes a parsed value and attempted to transpile it into a DFBlock.
 */
fun parseBlock(input: Value, header: DFHeader): List<DFBlock> {
    // Validate & Setup
    if (input.type !is ValueType.VList) throw UnexpectedValue("List", input)
    val params = input.type.inner.toMutableList()
    if (params.isEmpty()) throw MalformedList("Code Block", "(Identifier <data>...)", input)

    // Get typeVal and validate
    val typeVal = params.removeAt(0)
    if (typeVal.type !is ValueType.Ident) throw UnexpectedValue("Identifier", typeVal)

    when (typeVal.type.str) {
        "else" -> {
            if (params.size != 0) throw MalformedList("Code Block", "(else)", input)
            return listOf<DFBlock>(Else)
        }
        "end-if" -> {
            if (params.size != 0) throw MalformedList("Code Block", "(end-if)", input)
            return listOf<DFBlock>(EndIf)
        }
        "end-repeat" -> {
            if (params.size != 0) throw MalformedList("Code Block", "(end-repeat)", input)
            return listOf<DFBlock>(EndRepeat)
        }
        "return" -> {
            val out = mutableListOf<DFBlock>()
            val headerName = when (header) {
                is Function -> header.name
                is Process -> header.name
                else -> null
            }
            if (params.size == 1) {
                if (headerName != null)
                    out.add(
                        SetVar(
                            "=",
                            listOf(Variable("^ret", VariableScope.LOCAL), checkVal(params[0], CheckContext(header, "set_var", "=")))
                        )
                    )
                else throw UnexpectedValue("none (Cannot return value from event)", params[0])
            } else if (headerName != null) throw MalformedList("Code Block", "(return Value?)", input)
            if (headerName != null)
                out.add(SetVar("-=", listOf(Variable("^depth $headerName", VariableScope.LOCAL))))

            out.add(Control("Return", listOf()))
            return out
        }
        "yield" -> {
            if (params.size != 1) throw MalformedList("Code Block", "(yield Value)", input)
            return when (header) {
                is Function ->
                    listOf(
                        SetVar(
                            "=",
                            listOf(Variable("^ret", VariableScope.LOCAL), checkVal(params[0], CheckContext(header, "set_var", "=")))
                        )
                    )

                is Process ->
                    listOf(
                        SetVar(
                            "=",
                            listOf(Variable("^ret", VariableScope.LOCAL), checkVal(params[0], CheckContext(header, "set_var", "=")))
                        )
                    )

                else -> throw UnexpectedValue("none (Cannot yield value from event)", params[0])
            }
        }
        else -> {
            if (params.size == 0) throw MalformedList("Code Block", "(Identifier String ...)", input)
            val second = params.removeAt(0)
            if (second.type !is ValueType.Str) throw UnexpectedValue("String", typeVal)
            val blockType = second.type.str

            return when (typeVal.type.str) {
                "call" -> {
                    if (params.size != 1) throw MalformedList("Code Block", "(call String<Name> List<Params>)", input)
                    val out = mutableListOf<DFBlock>()
                    for ((paramNum, p) in checkParams(params[0], header, "call_function", "dynamic").withIndex()) {
                        out.add(
                            SetVar(
                                "+", listOf(
                                    Variable("^depthCalc", VariableScope.LOCAL),
                                    Variable("^depth $blockType", VariableScope.LOCAL),
                                    Number(1f)
                                )
                            )
                        )
                        out.add(
                            SetVar(
                                "=", listOf(
                                    Variable("^param $blockType $paramNum %var(^depthCalc)", VariableScope.LOCAL),
                                    p
                                )
                            )
                        )
                    }
                    out.add(CallFunction(blockType))
                    return out
                }
                "start" -> {
                    if (params.size != 1) throw MalformedList("Code Block", "(start String<Name> List<Params>)", input)
                    val out = mutableListOf<DFBlock>()
                    for ((paramNum, p) in checkParams(params[0], header, "start_process", "dynamic").withIndex()) {
                        out.add(
                            SetVar(
                                "+", listOf(
                                    Variable("^depthCalc", VariableScope.LOCAL),
                                    Variable("^depth $blockType", VariableScope.LOCAL),
                                    Number(1f)
                                )
                            )
                        )
                        out.add(
                            SetVar(
                                "=", listOf(
                                    Variable("^param $blockType $paramNum %var(^depthCalc)", VariableScope.LOCAL),
                                    p
                                )
                            )
                        )
                    }
                    out.add(StartProcess(blockType, listOf(Tag("Local Variables", "Copy", "start_process", "dynamic"))))
                    return out
                }
                "set-var" -> {
                    if (params.size != 1) throw MalformedList(
                        "Code Block",
                        "(set-var String<Type> List<Params>)",
                        input
                    )
                    listOf<DFBlock>(SetVar(blockType, checkParams(params.removeAt(0), header, "set_var", blockType)))
                }
                "control" -> {
                    if (params.size != 1) throw MalformedList(
                        "Code Block",
                        "(control String<Type> List<Params>)",
                        input
                    )
                    listOf<DFBlock>(Control(blockType, checkParams(params.removeAt(0), header, "control", blockType)))
                }
                "game-action" -> {
                    if (params.size != 1) throw MalformedList(
                        "Code Block",
                        "(game-action String<Type> List<Params>)",
                        input
                    )
                    listOf<DFBlock>(GameAction(blockType, checkParams(params.removeAt(0), header, "game_action", blockType)))
                }
                "if-player" -> {
                    if (params.size != 3) throw MalformedList(
                        "Code Block",
                        "(if-player String<Type> Identifier<Selector> Identifier<Inverse> List<Arguments>)",
                        input
                    )
                    val a = checkSelector(params.removeAt(0))
                    val b = checkBool(params.removeAt(0))
                    val c = checkParams(params.removeAt(0), header, "if_player", blockType)
                    listOf<DFBlock>(IfPlayer(blockType, a, b, c))
                }
                "if-entity" -> {
                    if (params.size != 3) throw MalformedList(
                        "Code Block",
                        "(if-entity String<Type> Identifier<Selector> Identifier<Inverse> List<Arguments>)",
                        input
                    )
                    val a = checkSelector(params.removeAt(0))
                    val b = checkBool(params.removeAt(0))
                    val c = checkParams(params.removeAt(0), header, "if_entity", blockType)
                    listOf<DFBlock>(IfEntity(blockType, a, b, c))
                }
                "player-action" -> {
                    if (params.size != 2) throw MalformedList(
                        "Code Block",
                        "(player-action String<Type> Identifier<Selector> List<Arguments>)",
                        input
                    )
                    val a = checkSelector(params.removeAt(0))
                    val b = checkParams(params.removeAt(0), header, "player_action", blockType)
                    listOf<DFBlock>(PlayerAction(blockType, a, b))
                }
                "entity-action" -> {
                    if (params.size != 2) throw MalformedList(
                        "Code Block",
                        "(entity-action String<Type> Identifier<Selector> List<Arguments>)",
                        input
                    )
                    val a = checkSelector(params.removeAt(0))
                    val b = checkParams(params.removeAt(0), header, "entity_action", blockType)
                    listOf<DFBlock>(EntityAction(blockType, a, b))
                }
                "if-game" -> {
                    if (params.size != 2) throw MalformedList(
                        "Code Block",
                        "(if-game String<Type> Identifier<Inverse> List<Arguments>)",
                        input
                    )
                    val a = checkBool(params.removeAt(0))
                    val b = checkParams(params.removeAt(0), header, "if_game", blockType)
                    listOf<DFBlock>(IfGame(blockType, a, b))
                }
                "if-var" -> {
                    if (params.size != 2) throw MalformedList(
                        "Code Block",
                        "(if-variable String<Type> Identifier<Inverse> List<Arguments>)",
                        input
                    )
                    val a = checkBool(params.removeAt(0))
                    val b = checkParams(params.removeAt(0), header, "if_var", blockType)
                    listOf<DFBlock>(IfVariable(blockType, a, b))
                }
                "select-object" -> {
                    if (params.size != 3) throw MalformedList(
                        "Code Block",
                        "(select-object String<Type> String<Subtype> Identifier<Inverse> List<Arguments>)",
                        input
                    )
                    val a = checkStr(params.removeAt(0))
                    val b = checkBool(params.removeAt(0))
                    val c = checkParams(params.removeAt(0), header, "select_obj", blockType)
                    listOf<DFBlock>(SelectObject(blockType, a, b, c))
                }
                "repeat" -> {
                    if (params.size != 3) throw MalformedList(
                        "Code Block",
                        "(repeat String<Type> String<Subtype> Identifier<Inverse> List<Arguments>)",
                        input
                    )
                    val a = checkStr(params.removeAt(0))
                    val b = checkBool(params.removeAt(0))
                    val c = checkParams(params.removeAt(0), header, "repeat", blockType)
                    listOf<DFBlock>(Repeat(blockType, a, b, c))
                }
                else -> throw UnexpectedValue("a valid block type", second)
            }
        }
    }
}

