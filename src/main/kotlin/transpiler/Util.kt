package transpiler

import MalformedList
import UnexpectedValue
import Value
import ValueType
import transpiler.codeblocks.header.DFHeader
import transpiler.values.*
import transpiler.values.Number

/**
 * Takes a parsed value and ensures it's a boolean
 */
fun checkBool(input: Value): Boolean {
    if (input.type !is ValueType.Ident) throw UnexpectedValue("Identifier", input)
    return when (input.type.str) {
        "norm" -> false
        "not" -> true
        else -> throw UnexpectedValue("Inverse", input)
    }
}

/**
 * Takes a parsed value and ensures it's a string
 */
fun checkStr(v: Value): String {
    if (v.type !is ValueType.Str) throw UnexpectedValue("String", v)
    return v.type.str
}

/**
 * Takes a parsed value and ensures it's an identifier
 */
fun checkIdent(v: Value): String {
    if (v.type !is ValueType.Ident) throw UnexpectedValue("Identifier", v)
    return v.type.str
}

/**
 * Takes a parsed value and ensures it's a number
 */
fun checkNum(v: Value): Float {
    if (v.type !is ValueType.Num) throw UnexpectedValue("Number", v)
    return v.type.num
}

/**
 * Takes a parsed value and ensures it's an integer
 */
fun checkInt(v: Value): Int {
    if (v.type !is ValueType.Num) throw UnexpectedValue("Number", v)
    return v.type.num.toInt()
}

/**
 * Takes a parsed value and ensures it's a list
 */
fun checkList(v: Value): List<Value> {
    if (v.type !is ValueType.VList) throw UnexpectedValue("List", v)
    return v.type.inner
}

/**
 * Takes a parsed value and ensures it's a selector
 */
fun checkSelector(input: Value): Selector {
    if (input.type !is ValueType.Ident) throw UnexpectedValue("Identifier", input)
    return when (input.type.str) {
        "all-players" -> Selector.ALL_PLAYERS
        "all-entities" -> Selector.ALL_ENTITIES
        "all-mobs" -> Selector.ALL_MOBS
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

/**
 * Takes a parsed value and ensures it's a scope
 */
fun checkScope(input: Value): VariableScope {
    if (input.type !is ValueType.Ident) throw UnexpectedValue("Identifier", input)
    return when (input.type.str) {
        "local" -> VariableScope.LOCAL
        "global" -> VariableScope.GLOBAL
        "save" -> VariableScope.SAVE
        else -> throw UnexpectedValue("Scope", input)
    }
}

/**
 * Takes a parsed value and transpiles it into a list of DFValues as parameters.
 */
fun checkParams(input: Value, header: DFHeader, blockType: String, blockAction: String): List<DFValue> {
    val out = mutableListOf<DFValue>()
    for (e in checkList(input)) {
        out.add(checkVal(e, CheckContext(header, blockType, blockAction)))
    }
    return out
}

/**
 * Takes a parsed value and transpiles it into a DFValues.
 */
fun checkVal(input: Value, context: CheckContext): DFValue {
    val list = checkList(input)
    if (list.isEmpty()) throw MalformedList("Value", "(Type <data>...)", input)
    val v = list[0]
    return when (checkIdent(v)) {
        "text" -> Text.transpileFrom(input, context)
        "num" -> Number.transpileFrom(input, context)
        "loc" -> Location.transpileFrom(input, context)
        "vec" -> Vector.transpileFrom(input, context)
        "sound" -> Sound.transpileFrom(input, context)
        "pot" -> PotionEffect.transpileFrom(input, context)
        "par" -> Particle.transpileFrom(input, context)
        "local", "global", "save", "var", "param" -> Variable.transpileFrom(input, context)
        "val" -> GameValue.transpileFrom(input, context)
        "tag" -> Tag.transpileFrom(input, context)
        "item" -> Item.transpileFrom(input, context)
        "ret" -> Variable("^ret", VariableScope.LOCAL)
        else -> throw UnexpectedValue("a valid value type", input)
    }
}