package transpiler.values

import MalformedList
import Value
import serializer.serializeString
import transpiler.CheckContext
import transpiler.checkList
import transpiler.checkNum
import transpiler.checkStr

data class Sound(val type: String, val pitch: Float, val volume: Float) : DFValue {
    companion object {
        fun transpileFrom(input: Value, context: CheckContext): Sound {
            val inpList = checkList(input)
            if (inpList.size != 4) throw MalformedList("Value", "(sound String<Name> Num<Pitch> Num<Yaw>)", input)
            return Sound(checkStr(inpList[1]), checkNum(inpList[2]), checkNum(inpList[3]))
        }
    }
    override fun serialize() = """{"id":"snd","data":{""" +
            """"sound":${serializeString(type)},""" +
            """"pitch":$pitch,""" +
            """"vol":$volume}}"""
    override fun toString() = "Snd[$type, $pitch, $volume]"
}