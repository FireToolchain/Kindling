package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.transpiler.*
import kotlin.String

/**
 * Represents DiamondFire's game value type.
 * @param type The type of game value to find
 * @param selector The target of the game value
 */
data class GameValue(val type: String, val selector: Selector) : DFValue {
    override fun serialize() = """{"id":"g_val","data":{""" +
            """"type":${type.serialize()},""" +
            """"target":"${selector.serialize()}"}}"""
}