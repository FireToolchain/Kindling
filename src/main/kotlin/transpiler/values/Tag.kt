package transpiler.values

import MalformedList
import Value
import serializer.serializeString
import transpiler.CheckContext
import transpiler.checkList
import transpiler.checkNum
import transpiler.checkStr

data class Tag(val option: String, val tag: String, val block: String, val action: String) : DFValue {
    companion object {
        fun transpileFrom(input: Value, context: CheckContext): Tag {
            val inpList = checkList(input)
            if (inpList.size != 3) throw MalformedList("Value", "(tag String<Key> String<Value>)", input)
            return Tag(checkStr(inpList[2]), checkStr(inpList[1]), context.blockType, context.blockAction)
        }
    }
    override fun serialize() = """{"id":"bl_tag","data":{"option":${serializeString(option)},"tag":${serializeString(tag)},"action":${
        serializeString(
            action
        )
    },"block":${serializeString(block)}}}"""
    override fun toString() = "{$tag = $option}"
}