package transpiler.values

import MalformedList
import Value
import serializer.serializeString
import transpiler.*

data class GameValue(val type: String, val selector: Selector) : DFValue {
    companion object {
        fun transpileFrom(input: Value, context: CheckContext): GameValue {
            val inpList = checkList(input)
            if (inpList.size != 3) throw MalformedList("Value", "(val String<Type> Identifier<Selector>", input)
            return GameValue(checkStr(inpList[1]), checkSelector(inpList[2]))
        }
    }

    override fun serialize() = """{"id":"g_val","data":{""" +
            """"type":${serializeString(type)},""" +
            """"target":"${selector.serialize()}"}}"""
    override fun toString() = "Val[$type, $selector]"
}