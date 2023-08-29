package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.serialize

data class Sound(val isCustom: Boolean, val type: String, val pitch: Float, val volume: Float, val variant: String?) : DFValue {
    override fun serialize() = """{"id":"snd","data":{""" +
            if(isCustom)  """"key":${type.serialize()},""" else """"sound":${type.serialize()},""" +
            """"pitch":$pitch,""" +
            """"vol":$volume""" +
            (if (variant != null) ""","variant":${variant.serialize()}""" else "") +
            """}}"""
}