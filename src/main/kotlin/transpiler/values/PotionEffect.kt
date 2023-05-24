package transpiler.values

import MalformedList
import Value
import serializer.serializeString
import transpiler.*

data class PotionEffect(val type: String, val duration: Int, val level: Int) : DFValue {
    companion object {
        fun transpileFrom(input: Value, context: CheckContext): PotionEffect {
            val inpList = checkList(input)
            if (inpList.size != 4) throw MalformedList("Value", "(pot String<Name> Num<Duration> Num<Potency>)", input)
            return PotionEffect(checkStr(inpList[1]), checkInt(inpList[2]), checkInt(inpList[3]))
        }
    }
    override fun serialize() = """{"id":"pot","data":{""" +
            """"pot":${serializeString(type)},""" +
            """"dur":$duration,""" +
            """"amp":$level}}"""
    override fun toString() = "Pot[$type, $duration, $level]"
}