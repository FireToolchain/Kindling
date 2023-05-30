package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.MalformedList
import dev.ashli.kindling.Value
import dev.ashli.kindling.transpiler.CheckContext
import dev.ashli.kindling.transpiler.checkList
import dev.ashli.kindling.transpiler.checkNum

data class Number(val num: Float): DFValue {
    companion object {
        fun transpileFrom(input: Value, context: CheckContext): Number {
            val inpList = checkList(input)
            if (inpList.size != 2) throw MalformedList("Value", "(num Num<Value>)", input)
            return Number(checkNum(inpList[1]))
        }
    }
    override fun serialize() = """{"id":"num","data":{"name":"$num"}}"""
}