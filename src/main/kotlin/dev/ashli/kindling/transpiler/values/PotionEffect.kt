package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.serialize

data class PotionEffect(val type: String, val duration: Int, val level: Int) : DFValue {
    override fun serialize() = """{"id":"pot","data":{""" +
            """"pot":${type.serialize()},""" +
            """"dur":$duration,""" +
            """"amp":$level}}"""
}