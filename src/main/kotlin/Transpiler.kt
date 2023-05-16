fun transpile(input: List<Value>): DFProgram {
    val lines = mutableListOf<DFLine>()
    for (v in input)
        lines.add(parseLine(v))
    return DFProgram(lines)
}
fun parseLine(input: Value): DFLine {
    // Validate
    if (input.type !is ValueType.VList) throw UnexpectedValue("List", "Code Line", input)
    if (input.type.inner.size < 2) throw MalformedList("Code Line", "(line Header Codeblock*)", input)

    val first = input.type.inner[0]

    // Validate
    if (first.type !is ValueType.Ident || first.type.str == "line") throw UnexpectedValue("Identifier (line)", "Code Line", first)

    val header = parseHeader(input.type.inner[1])
    val blocks = input.type.inner.filterIndexed { index, _ -> index >= 2 }.map {
        parseBlock(it, header)
    }
    return DFLine(header, blocks)

}
fun parseHeader(input: Value): DFHeader {
    // Validate
    if (input.type !is ValueType.VList) throw UnexpectedValue("List", "Header", input)
    if (input.type.inner.size != 2) throw MalformedList("Header", "(Identifier String)", input)

    val first = input.type.inner[0]
    val second = input.type.inner[1]

    // Validate
    if (first.type !is ValueType.Ident) throw UnexpectedValue("Identifier", "Header", first)
    if (second.type !is ValueType.Str) throw UnexpectedValue("Identifier", "Header", second)

    return when (first.type.str) {
        "player-event" -> DFHeader.PlayerEvent(second.type.str)
        "entity-event" -> DFHeader.EntityEvent(second.type.str)
        "def" -> DFHeader.Function(second.type.str)
        "process" -> DFHeader.Process(second.type.str)
        else -> throw UnexpectedValue("a valid header type", "Header", first)
    }
}

fun parseBlock(input: Value, header: DFHeader): DFBlock {
    // Validate & Setup
    if (input.type !is ValueType.VList) throw UnexpectedValue("List", "Code Block", input)
    val params = input.type.inner.toMutableList()
    if (params.isEmpty()) throw MalformedList("Code Block", "(Identifier <data>...)", input)

    // Get typeVal and validate
    val typeVal = params.removeAt(0)
    if (typeVal.type !is ValueType.Ident) throw UnexpectedValue("Identifier", "Code Block", typeVal)

    return when (typeVal.type.str) {
        "else" -> {
            if (params.size != 0) throw MalformedList("Code Block", "(else)", input)
            DFBlock.Else
        }
        "end-if" -> {
            if (params.size != 0) throw MalformedList("Code Block", "(end-if)", input)
            DFBlock.EndIf
        }
        "end-repeat" -> {
            if (params.size != 0) throw MalformedList("Code Block", "(end-repeat)", input)
            DFBlock.EndRepeat
        }
        else -> {
            if (params.size == 0) throw MalformedList("Code Block", "(Identifier String ...)", input)
            val second = params.removeAt(0)
            if (second.type !is ValueType.Str) throw UnexpectedValue("String", "Code Block", typeVal)
            val blockType = second.type.str
            when (typeVal.type.str) {
                "call" -> {
                    if (params.size != 1) throw MalformedList("Code Block", "(call String<Name> List<Params>)", input)
                    DFBlock.CallFunction(blockType, parseParams(params.removeAt(0)))
                }
                "start" -> {
                    if (params.size != 1) throw MalformedList("Code Block", "(start String<Name> List<Params>)", input)
                    DFBlock.StartProcess(blockType, parseParams(params.removeAt(0)))
                }
                "set-var" -> {
                    if (params.size != 1) throw MalformedList("Code Block", "(set-var String<Type> List<Params>)", input)
                    DFBlock.SetVar(blockType, parseParams(params.removeAt(0)))
                }
                "control" -> {
                    if (params.size != 1) throw MalformedList("Code Block", "(control String<Type> List<Params>)", input)
                    DFBlock.Control(blockType, parseParams(params.removeAt(0)))
                }
                "game-action" -> {
                    if (params.size != 1) throw MalformedList("Code Block", "(game-action String<Type> List<Params>)", input)
                    DFBlock.GameAction(blockType, parseParams(params.removeAt(0)))
                }
                else -> {
                    when (typeVal.type.str) {
                        "if-player" -> {
                            if (params.size != 3) throw MalformedList("Code Block", "(if-player String<Type> Identifier<Selector> Identifier<Inverse> List<Arguments>)", input)
                            val a = params.removeAt(0)
                            val b = params.removeAt(0)
                            val c = params.removeAt(0)
                        }
                        "if-entity" -> {
                            if (params.size != 3) throw MalformedList("Code Block", "(if-entity String<Type> Identifier<Selector> Identifier<Inverse> List<Arguments>)", input)
                            val a = params.removeAt(0)
                            val b = params.removeAt(0)
                            val c = params.removeAt(0)
                        }
                        "player-action" -> {
                            if (params.size != 2) throw MalformedList("Code Block", "(player-action String<Type> Identifier<Selector> List<Arguments>)", input)
                            val a = params.removeAt(0)
                            val b = params.removeAt(0)
                        }
                        "entity-action" -> {
                            if (params.size != 2) throw MalformedList("Code Block", "(entity-action String<Type> Identifier<Selector> List<Arguments>)", input)
                            val a = params.removeAt(0)
                            val b = params.removeAt(0)
                        }
                        "if-game" -> {
                            if (params.size != 2) throw MalformedList("Code Block", "(if-game String<Type> Identifier<Inverse> List<Arguments>)", input)
                            val a = params.removeAt(0)
                            val b = params.removeAt(0)
                        }
                        "if-variable" -> {
                            if (params.size != 2) throw MalformedList("Code Block", "(if-variable String<Type> Identifier<Inverse> List<Arguments>)", input)
                            val a = params.removeAt(0)
                            val b = params.removeAt(0)
                        }
                        "select-object" -> {
                            if (params.size != 3) throw MalformedList("Code Block", "(select-object String<Type> String<Subtype> Identifier<Inverse> List<Arguments>)", input)
                            val a = params.removeAt(0)
                            val b = params.removeAt(0)
                            val c = params.removeAt(0)
                        }
                        "repeat" -> {
                            if (params.size != 3) throw MalformedList("Code Block", "(repeat String<Type> String<Subtype> Identifier<Inverse> List<Arguments>)", input)
                            val a = params.removeAt(0)
                            val b = params.removeAt(0)
                            val c = params.removeAt(0)
                        }
                        else -> {

                        }
                    }
                    TODO()
                }
            }
        }
    }
}

fun parseParams(input: Value): List<DFValue> {
    TODO()
}

fun parseVal(input: Value, header: DFHeader): DFValue {
    TODO()
}

