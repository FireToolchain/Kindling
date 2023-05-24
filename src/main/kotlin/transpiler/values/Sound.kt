package transpiler.values

import serializer.serializeString

data class Sound(val type: String, val pitch: Float, val volume: Float) : DFValue {
    override fun serialize() = """{"id":"snd","data":{""" +
            """"sound":${serializeString(type)},""" +
            """"pitch":$pitch,""" +
            """"vol":$volume}}"""
    override fun toString() = "Snd[$type, $pitch, $volume]"
}