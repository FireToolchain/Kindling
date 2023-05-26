package transpiler.values

import MalformedList
import Value
import transpiler.CheckContext
import transpiler.checkList
import transpiler.checkNum

data class Location(val x: Float, val y: Float, val z: Float, val pitch: Float, val yaw: Float) : DFValue {
    companion object {
        fun transpileFrom(input: Value, context: CheckContext): Location {
            val inpList = checkList(input)
            if (inpList.size != 6) throw MalformedList("Value", "(loc Num<X> Num<Y> Num<Z> Num<Pitch> Num<Yaw>)", input)
            return Location(checkNum(inpList[1]),checkNum(inpList[2]),checkNum(inpList[3]),checkNum(inpList[4]),checkNum(inpList[5]))
        }
    }
    override fun serialize() = """{"id":"loc","data":{""" +
            """"isBlock":false,"loc":{""" +
            """"x":$x,""" +
            """"y":$y,""" +
            """"z":$z,""" +
            """"pitch":$pitch,""" +
            """"yaw":$yaw}}}"""
    override fun toString() = "Loc[$x, $y, $z, $pitch, $yaw]"
}