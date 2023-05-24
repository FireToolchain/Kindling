package transpiler.values

import serializer.serializeString
import transpiler.Selector

data class GameValue(val type: String, val selector: Selector) : DFValue {
    override fun serialize() = """{"id":"g_val","data":{""" +
            """"type":${serializeString(type)},""" +
            """"target":"${selector.serialize()}"}}"""
    override fun toString() = "Val[$type, $selector]"
}