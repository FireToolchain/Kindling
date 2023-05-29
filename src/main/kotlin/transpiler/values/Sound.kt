package transpiler.values

import MalformedList
import Value
import serializer.serialize
import transpiler.CheckContext
import transpiler.checkList
import transpiler.checkNum
import transpiler.checkStr

data class Sound(val type: String, val pitch: Float, val volume: Float, val variant: String?) : DFValue {
    companion object {
        fun transpileFrom(input: Value, context: CheckContext): Sound {
            val inpList = checkList(input)
            return if (inpList.size == 4) {
                Sound(checkStr(inpList[1]), checkNum(inpList[2]), checkNum(inpList[3]), null)
            } else if (inpList.size == 5) {
                Sound(checkStr(inpList[1]), checkNum(inpList[3]), checkNum(inpList[4]), checkStr(inpList[2]))
            } else throw MalformedList("Value", "(sound String<Name> String<Variant>? Num<Pitch> Num<Yaw>)", input)
        }
    }
    override fun serialize() = """{"id":"snd","data":{""" +
            """"sound":${type.serialize()},""" +
            """"pitch":$pitch,""" +
            """"vol":$volume""" +
            (if (variant != null) ""","variant":${variant.serialize()}""" else "") +
            """}}"""
}