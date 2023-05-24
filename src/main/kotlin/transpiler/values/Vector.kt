package transpiler.values

import MalformedList
import Value
import transpiler.*

data class Vector(val x: Float, val y: Float, val z: Float) : DFValue {
    companion object {
        fun transpileFrom(input: Value, context: CheckContext): Vector {
            val inpList = checkList(input)
            if (inpList.size != 4) throw MalformedList("Value", "(vec Num<X> Num<Y> Num<Z>)", input)
            return Vector(checkNum(inpList[1]), checkNum(inpList[2]), checkNum(inpList[3]))
        }
    }
    override fun serialize() = """{"id":"vec","data":{"x":$x,"y":$y,"z":$z}}"""
    override fun toString() = "<$x, $y, $z>"
}