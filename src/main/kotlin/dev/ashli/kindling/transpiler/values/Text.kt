package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.toInner

/**
 * Represents a String on DiamondFire.
 * @param text The text of the string
 */
data class Text(val text: String) : DFValue {
    override fun serialize() = """{"id":"txt","data":{"name":"${toInner(text)}"}}"""
}