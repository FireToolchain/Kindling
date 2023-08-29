package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.serialize
import kotlin.String


/**
 * Represents a sound on DiamondFire.
 * @param isCustom Should be true if this is a custom sound
 * @param type Type of sound / key of sound to play
 * @param pitch Pitch of the sound
 * @param volume Voluime of the sound
 * @param variant If applicable, the variant of the sound to play
 */
data class Sound(val isCustom: Boolean, val type: String, val pitch: Float, val volume: Float, val variant: String?) : DFValue {
    override fun serialize() = """{"id":"snd","data":{""" +
            if(isCustom)  """"key":${type.serialize()},""" else """"sound":${type.serialize()},""" +
            """"pitch":$pitch,""" +
            """"vol":$volume""" +
            (if (variant != null) ""","variant":${variant.serialize()}""" else "") +
            """}}"""
}