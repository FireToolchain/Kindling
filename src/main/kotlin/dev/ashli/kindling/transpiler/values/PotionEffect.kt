package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.MalformedList
import dev.ashli.kindling.Value
import dev.ashli.kindling.transpiler.CheckContext
import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.transpiler.checkInt
import dev.ashli.kindling.transpiler.checkList
import dev.ashli.kindling.transpiler.checkStr

data class PotionEffect(val type: String, val duration: Int, val level: Int) : DFValue {
    companion object {
        fun transpileFrom(input: Value, context: CheckContext): PotionEffect {
            val inpList = checkList(input)
            if (inpList.size != 4) throw MalformedList("Value", "(pot String<Name> Num<Duration> Num<Potency>)", input)
            return PotionEffect(checkStr(inpList[1]), checkInt(inpList[2]), checkInt(inpList[3]))
        }
    }
    override fun serialize() = """{"id":"pot","data":{""" +
            """"pot":${type.serialize()},""" +
            """"dur":$duration,""" +
            """"amp":$level}}"""
}