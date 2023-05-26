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
    val vList = checkList(input)
    if (vList.isEmpty()) throw MalformedList("Header", "(Identifier<Type> ...)", input)

    return when (checkIdent(vList[0])) {
        "player-event" -> PlayerEvent.transpileFrom(input)
        "entity-event" -> EntityEvent.transpileFrom(input)
        "def" -> Function.transpileFrom(input)
        "process" -> Process.transpileFrom(input)
        else -> throw UnexpectedValue("a valid header type", vList[0])
    }
}

/**
 * Takes a parsed value and attempted to transpile it into a DFBlock.
 */
fun parseBlock(input: Value, header: DFHeader): List<DFBlock> {
    val vList = checkList(input)
    if (vList.isEmpty()) throw MalformedList("Header", "(Identifier<Type> ...)", input)

    return when (checkIdent(vList[0])) {
        "else" -> listOf(Else.transpileFrom(input, header))
        "end-if" -> listOf(EndIf.transpileFrom(input, header))
        "end-repeat" -> listOf(EndRepeat.transpileFrom(input, header))
        "return", "yield", "control" -> Control.transpileFrom(input, header)
        "call" -> CallFunction.transpileFrom(input, header)
        "start" -> StartProcess.transpileFrom(input, header)
        "set-var" -> listOf(SetVar.transpileFrom(input, header))
        "game-action" -> listOf(GameAction.transpileFrom(input, header))
        "if-player" -> listOf(IfPlayer.transpileFrom(input, header))
        "if-entity" -> listOf(IfEntity.transpileFrom(input, header))
        "player-action" -> listOf(PlayerAction.transpileFrom(input, header))
        "entity-action" -> listOf(EntityAction.transpileFrom(input, header))
        "if-game" -> listOf(IfGame.transpileFrom(input, header))
        "if-var" -> listOf(IfVariable.transpileFrom(input, header))
        "select-object" -> listOf(SelectObject.transpileFrom(input, header))
        "repeat" -> listOf(Repeat.transpileFrom(input, header))
        else -> throw UnexpectedValue("a valid block type", vList[0])
    }
}

