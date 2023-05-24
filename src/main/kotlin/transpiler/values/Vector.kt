package transpiler.values

data class Vector(val x: Float, val y: Float, val z: Float) : DFValue {
    override fun serialize() = """{"id":"vec","data":{"x":$x,"y":$y,"z":$z}}"""
    override fun toString() = "<$x, $y, $z>"
}