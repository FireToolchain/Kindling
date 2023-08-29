package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.serialize

data class Item(val nbt: String) : DFValue {
    override fun serialize() = """{"id":"item","data":{"item":${nbt.serialize()}}}"""
}