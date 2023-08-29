package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.transpiler.*

data class GameValue(val type: String, val selector: Selector) : DFValue {

    override fun serialize() = """{"id":"g_val","data":{""" +
            """"type":${type.serialize()},""" +
            """"target":"${selector.serialize()}"}}"""
}