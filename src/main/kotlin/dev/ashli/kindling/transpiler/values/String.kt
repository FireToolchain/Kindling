package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.toInner
import kotlin.String

/**
 * Represents a String on DiamondFire.
 * @param text The text of the string
 */
data class String(val text: String) : DFValue {
    override fun serialize() = """{"id":"txt","data":{"name":"${toInner(text)}"}}"""
}