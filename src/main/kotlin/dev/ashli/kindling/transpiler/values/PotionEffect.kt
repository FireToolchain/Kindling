package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.serialize


/**
 * Represents a potion effect on DiamondFire.
 * @param type The type of effect to use
 * @param duration The length of the potion in ticks
 * @param level The amplifier of the potion
 */
data class PotionEffect(val type: String, val duration: Int, val level: Int) : DFValue {
    override fun serialize() = """{"id":"pot","data":{""" +
            """"pot":${type.serialize()},""" +
            """"dur":$duration,""" +
            """"amp":$level}}"""
}