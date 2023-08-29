package dev.ashli.kindling.transpiler.values

data class Number(val num: Float): DFValue {
    override fun serialize() = """{"id":"num","data":{"name":"$num"}}"""
}