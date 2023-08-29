package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.serialize

/**
 * Represents a regular, vanilla item
 * @param nbt The NBT of the item
 */
data class Item(val nbt: String) : DFValue {
    override fun serialize() = """{"id":"item","data":{"item":${nbt.serialize()}}}"""
}