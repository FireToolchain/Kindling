package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.MalformedList
import dev.ashli.kindling.Value
import dev.ashli.kindling.serializer.toInner
import dev.ashli.kindling.transpiler.CheckContext
import dev.ashli.kindling.transpiler.checkList
import dev.ashli.kindling.transpiler.checkStr

data class Text(val text: String) : DFValue {
    companion object {
        fun transpileFrom(input: Value, context: CheckContext): Text {
            val inpList = checkList(input)
            if (inpList.size != 2) throw MalformedList("Value", "(text String<Text>)", input)
            return Text(checkStr(inpList[1]))
        }
    }
    override fun serialize() = """{"id":"txt","data":{"name":"${toInner(text)}"}}"""
}