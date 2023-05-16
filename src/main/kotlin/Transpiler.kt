fun transpile(input: List<Value>): DFProgram {
    val lines = mutableListOf<DFLine>()
    for (v in input)
        lines.add(parseLine(v))
    return DFProgram(lines)
}
fun parseLine(input: Value): DFLine {
    // Validate
    if (input.type !is ValueType.VList) throw UnexpectedValue("List", input)
    if (input.type.inner.isEmpty()) throw MalformedList("Code Line", "(Header Codeblock*)", input)

    val header = parseHeader(input.type.inner[0])
    val blocks = input.type.inner.filterIndexed { index, _ -> index >= 1 }.map {
        parseBlock(it, header)
    }
    return DFLine(header, blocks)

}
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
        "player-event" -> DFHeader.PlayerEvent(second.type.str)
        "entity-event" -> DFHeader.EntityEvent(second.type.str)
        "def" -> DFHeader.Function(second.type.str)
        "process" -> DFHeader.Process(second.type.str)
        else -> throw UnexpectedValue("a valid header type", first)
    }
}
fun parseBlock(input: Value, header: DFHeader): DFBlock {
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
            return DFBlock.Else
        }
        "end-if" -> {
            if (params.size != 0) throw MalformedList("Code Block", "(end-if)", input)
            return DFBlock.EndIf
        }
        "end-repeat" -> {
            if (params.size != 0) throw MalformedList("Code Block", "(end-repeat)", input)
            return DFBlock.EndRepeat
        }
        else -> {
            if (params.size == 0) throw MalformedList("Code Block", "(Identifier String ...)", input)
            val second = params.removeAt(0)
            if (second.type !is ValueType.Str) throw UnexpectedValue("String", typeVal)
            val blockType = second.type.str

            return when (typeVal.type.str) {
                "call" -> {
                    if (params.size != 1) throw MalformedList("Code Block", "(call String<Name> List<Params>)", input)
                    DFBlock.CallFunction(blockType, parseParams(params.removeAt(0), header))
                }
                "start" -> {
                    if (params.size != 1) throw MalformedList("Code Block", "(start String<Name> List<Params>)", input)
                    DFBlock.StartProcess(blockType, parseParams(params.removeAt(0), header))
                }
                "set-var" -> {
                    if (params.size != 1) throw MalformedList("Code Block", "(set-var String<Type> List<Params>)", input)
                    DFBlock.SetVar(blockType, parseParams(params.removeAt(0), header))
                }
                "control" -> {
                    if (params.size != 1) throw MalformedList("Code Block", "(control String<Type> List<Params>)", input)
                    DFBlock.Control(blockType, parseParams(params.removeAt(0), header))
                }
                "game-action" -> {
                    if (params.size != 1) throw MalformedList("Code Block", "(game-action String<Type> List<Params>)", input)
                    DFBlock.GameAction(blockType, parseParams(params.removeAt(0), header))
                }
                "if-player" -> {
                    if (params.size != 3) throw MalformedList("Code Block", "(if-player String<Type> Identifier<Selector> Identifier<Inverse> List<Arguments>)", input)
                    val a = parseSelector(params.removeAt(0))
                    val b = parseBool(params.removeAt(0))
                    val c = parseParams(params.removeAt(0), header)
                    DFBlock.IfPlayer(blockType, a, b, c)
                }
                "if-entity" -> {
                    if (params.size != 3) throw MalformedList("Code Block", "(if-entity String<Type> Identifier<Selector> Identifier<Inverse> List<Arguments>)", input)
                    val a = parseSelector(params.removeAt(0))
                    val b = parseBool(params.removeAt(0))
                    val c = parseParams(params.removeAt(0), header)
                    DFBlock.IfEntity(blockType, a, b, c)
                }
                "player-action" -> {
                    if (params.size != 2) throw MalformedList("Code Block", "(player-action String<Type> Identifier<Selector> List<Arguments>)", input)
                    val a = parseSelector(params.removeAt(0))
                    val b = parseParams(params.removeAt(0), header)
                    DFBlock.PlayerAction(blockType, a, b)
                }
                "entity-action" -> {
                    if (params.size != 2) throw MalformedList("Code Block", "(entity-action String<Type> Identifier<Selector> List<Arguments>)", input)
                    val a = parseSelector(params.removeAt(0))
                    val b = parseParams(params.removeAt(0), header)
                    DFBlock.PlayerAction(blockType, a, b)
                }
                "if-game" -> {
                    if (params.size != 2) throw MalformedList("Code Block", "(if-game String<Type> Identifier<Inverse> List<Arguments>)", input)
                    val a = parseBool(params.removeAt(0))
                    val b = parseParams(params.removeAt(0), header)
                    DFBlock.IfGame(blockType, a, b)
                }
                "if-variable" -> {
                    if (params.size != 2) throw MalformedList("Code Block", "(if-variable String<Type> Identifier<Inverse> List<Arguments>)", input)
                    val a = parseBool(params.removeAt(0))
                    val b = parseParams(params.removeAt(0), header)
                    DFBlock.IfVariable(blockType, a, b)
                }
                "select-object" -> {
                    if (params.size != 3) throw MalformedList("Code Block", "(select-object String<Type> String<Subtype> Identifier<Inverse> List<Arguments>)", input)
                    val a = parseStr(params.removeAt(0))
                    val b = parseBool(params.removeAt(0))
                    val c = parseParams(params.removeAt(0), header)
                    DFBlock.SelectObject(blockType, a, b, c)
                }
                "repeat" -> {
                    if (params.size != 3) throw MalformedList("Code Block", "(repeat String<Type> String<Subtype> Identifier<Inverse> List<Arguments>)", input)
                    val a = parseStr(params.removeAt(0))
                    val b = parseBool(params.removeAt(0))
                    val c = parseParams(params.removeAt(0), header)
                    DFBlock.Repeat(blockType, a, b, c)
                }
                else -> throw UnexpectedValue("a valid block type", second)
            }
        }
    }
}
fun parseBool(input: Value): Boolean {
    if (input.type !is ValueType.Ident) throw UnexpectedValue("Identifier", input)
    return when (input.type.str) {
        "norm" -> false
        "not" -> true
        else -> throw UnexpectedValue("Inverse", input)
    }
}
fun parseStr(v: Value): String {
    if (v.type !is ValueType.Str) throw UnexpectedValue("String", v)
    return v.type.str
}
fun parseIdent(v: Value): String {
    if (v.type !is ValueType.Ident) throw UnexpectedValue("Identifier", v)
    return v.type.str
}
fun parseNum(v: Value): Float {
    if (v.type !is ValueType.Num) throw UnexpectedValue("Number", v)
    return v.type.num
}
fun parseInt(v: Value): Int {
    if (v.type !is ValueType.Num) throw UnexpectedValue("Number", v)
    return v.type.num.toInt()
}
fun parseList(v: Value): List<Value> {
    if (v.type !is ValueType.VList) throw UnexpectedValue("List", v)
    return v.type.inner
}
fun parseSelector(input: Value): Selector {
    if (input.type !is ValueType.Ident) throw UnexpectedValue("Identifier", input)
    return when (input.type.str) {
        "all-players" -> Selector.ALL_PLAYERS
        "all-entities" -> Selector.ALL_ENTITIES
        "damager" -> Selector.DAMAGER
        "default" -> Selector.DEFAULT
        "last-spawned" -> Selector.LAST_SPAWNED_ENTITY
        "killer" -> Selector.KILLER
        "projectile" -> Selector.PROJECTILE
        "selected" -> Selector.SELECTED
        "shooter" -> Selector.SHOOTER
        "victim" -> Selector.VICTIM
        else -> throw UnexpectedValue("Selector", input)
    }
}
fun parseParams(input: Value, header: DFHeader): List<DFValue> {
    val out = mutableListOf<DFValue>()
    for (e in parseList(input)) {
        out.add(parseVal(e, header))
    }
    return out
}
fun parseVal(input: Value, header: DFHeader): DFValue {
    val inner = parseList(input).toMutableList()
    if (inner.isEmpty()) throw MalformedList("Value", "(Type <data>...)", input)
    val v = inner.removeAt(0)
    when (parseIdent(v)) {
        "text" -> {
            if (inner.size != 1) throw MalformedList("Value", "(text String)", input)
            return DFValue.Text(parseStr(inner.removeAt(0)))
        }
        "num" -> {
            if (inner.size != 1) throw MalformedList("Value", "(num Number)", input)
            return DFValue.Number(parseNum(inner.removeAt(0)))
        }
        "loc" -> {
            if (inner.size != 5) throw MalformedList("Value", "(loc Number<X> Number<Y> Number<Z> Number<Pitch> Number<Yaw>)", input)
            val x = parseNum(inner.removeAt(0))
            val y = parseNum(inner.removeAt(0))
            val z = parseNum(inner.removeAt(0))
            val pit = parseNum(inner.removeAt(0))
            val yaw = parseNum(inner.removeAt(0))
            return DFValue.Location(x, y, z, pit, yaw)
        }
        "vec" -> {
            if (inner.size != 3) throw MalformedList("Value", "(vec Number<X> Number<Y> Number<Z>)", input)
            val x = parseNum(inner.removeAt(0))
            val y = parseNum(inner.removeAt(0))
            val z = parseNum(inner.removeAt(0))
            return DFValue.Vector(x, y, z)
        }
        "sound" -> {
            if (inner.size != 3) throw MalformedList("Value", "(sound String<Type> Number<Pitch> Number<Volume>)", input)
            val type = parseStr(inner.removeAt(0))
            val pit = parseNum(inner.removeAt(0))
            val vol = parseNum(inner.removeAt(0))
            return DFValue.Sound(type, pit, vol)
        }
        "pot" -> {
            if (inner.size != 3) throw MalformedList("Value", "(pot String<Type> Number<Duration> Number<Amplifier>)", input)
            val type = parseStr(inner.removeAt(0))
            val dur = parseNum(inner.removeAt(0))
            val amp = parseNum(inner.removeAt(0))
            return DFValue.Sound(type, dur, amp)
        }
        "par" -> {
            if (inner.isEmpty()) throw MalformedList("Value", "(text String<Type> List<Setting>*)", input)
            val type = parseStr(inner.removeAt(0))
            val par = DFValue.Particle(type)
            for (s in inner) {
                val setting = parseList(s)
                if (setting.size != 2) throw MalformedList("Sound Setting", "(String<Name> <Value>)", s)
                when (parseStr(setting[0])) {
                    "amount" -> par.amount = parseInt(setting[1])
                    "spread-x" -> par.spreadX = parseNum(setting[1])
                    "spread-y" -> par.spreadY = parseNum(setting[1])
                    "motion-x" -> par.motionX = parseNum(setting[1])
                    "motion-y" -> par.motionY = parseNum(setting[1])
                    "motion-z" -> par.motionZ = parseNum(setting[1])
                    "roll" -> par.roll = parseNum(setting[1])
                    "size" -> par.size = parseNum(setting[1])
                    "color" -> par.color = parseStr(setting[1])
                    "material" -> par.material = parseStr(setting[1])
                    "variation-color" -> par.variationColor = parseInt(setting[1])
                    "variation-motion" -> par.variationMotion = parseInt(setting[1])
                    "variation-size" -> par.variationSize = parseInt(setting[1])
                    else -> throw UnexpectedValue("a valid particle setting", s)
                }
            }
            return par
        }
        "local" -> {
            if (inner.size != 1) throw MalformedList("Value", "(local String<Name>)", input)
            return DFValue.Variable(parseStr(inner.removeAt(0)), VariableScope.LOCAL)
        }
        "global" -> {
            if (inner.size != 1) throw MalformedList("Value", "(global String<Name>)", input)
            return DFValue.Variable(parseStr(inner.removeAt(0)), VariableScope.GLOBAL)
        }
        "save" -> {
            if (inner.size != 1) throw MalformedList("Value", "(save String<Name>)", input)
            return DFValue.Variable(parseStr(inner.removeAt(0)), VariableScope.SAVE)
        }
        "val" -> {
            return when (inner.size) {
                1 -> DFValue.GameValue(parseStr(inner[0]), Selector.DEFAULT)
                2 -> DFValue.GameValue(parseStr(inner[0]), parseSelector(inner[1]))
                else -> throw MalformedList("Value", "(val String<Name> Identifier<Selector>?)", input)
            }
        }
        "tag" -> {
            if (inner.size != 2) throw MalformedList("Value", "(tag String<Name> String<Value>)", input)
            val tag = parseStr(inner[0])
            val value = parseStr(inner[1])
            return DFValue.Tag(tag, value)
        }
        "raw-item" -> {
            if (inner.size != 1) throw MalformedList("Value", "(raw-item String<Itemdata>)", input)
            return DFValue.Item(parseStr(inner[0]))
        }
        else -> throw UnexpectedValue("a valid value type", input)
    }
}

