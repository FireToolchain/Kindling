package transpiler.values

import MalformedList
import Value
import serializer.serialize
import transpiler.CheckContext
import transpiler.checkList
import transpiler.checkStr

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