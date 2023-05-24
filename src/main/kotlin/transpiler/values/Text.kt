package transpiler.values

import MalformedList
import Value
import serializer.toInner
import transpiler.CheckContext
import transpiler.checkList
import transpiler.checkStr

data class Text(val text: String) : DFValue {
    companion object {
        fun transpileFrom(input: Value, context: CheckContext): Text {
            val inpList = checkList(input)
            if (inpList.size != 2) throw MalformedList("Value", "(text String<Text>)", input)
            return Text(checkStr(inpList[1]))
        }
    }
    override fun serialize() = """{"id":"txt","data":{"name":"${toInner(text)}"}}"""
    override fun toString() = "\"$text\""
}