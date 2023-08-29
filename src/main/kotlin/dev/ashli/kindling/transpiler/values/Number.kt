package dev.ashli.kindling.transpiler.values


/**
 * Represents a number on DiamondFire.
 * @param num The number to use.
 */
data class Number(val num: Float): DFValue {
    override fun serialize() = """{"id":"num","data":{"name":"$num"}}"""
}