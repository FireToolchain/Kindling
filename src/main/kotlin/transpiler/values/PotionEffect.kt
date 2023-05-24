package transpiler.values

import serializer.serializeString

data class PotionEffect(val type: String, val duration: Int, val level: Int) : DFValue {
    override fun serialize() = """{"id":"pot","data":{""" +
            """"pot":${serializeString(type)},""" +
            """"dur":$duration,""" +
            """"amp":$level}}"""
    override fun toString() = "Pot[$type, $duration, $level]"
}