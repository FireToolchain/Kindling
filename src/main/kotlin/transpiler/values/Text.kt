package transpiler.values

import serializer.toInner

data class Text(val text: String) : DFValue {
    override fun serialize() = """{"id":"txt","data":{"name":"${toInner(text)}"}}"""
    override fun toString() = "\"$text\""
}