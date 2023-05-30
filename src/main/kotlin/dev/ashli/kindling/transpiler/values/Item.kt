package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.MalformedList
import dev.ashli.kindling.Value
import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.transpiler.CheckContext
import dev.ashli.kindling.transpiler.checkList
import dev.ashli.kindling.transpiler.checkStr

data class Item(val nbt: String) : DFValue {
    companion object {
        fun transpileFrom(input: Value, context: CheckContext): Item {
            val inpList = checkList(input)
            if (inpList.size != 2) throw MalformedList("Value", "(item String<NBT>)", input)
            return Item(checkStr(inpList[1]))
        }
    }
    override fun serialize() = """{"id":"item","data":{"item":${nbt.serialize()}}}"""
}