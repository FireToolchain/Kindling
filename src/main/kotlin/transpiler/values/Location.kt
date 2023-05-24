package transpiler.values

data class Location(val x: Float, val y: Float, val z: Float, val pitch: Float, val yaw: Float) : DFValue {
    override fun serialize() = """{"id":"loc","data":{""" +
            """"isBlock":false,""" +
            """"x":$x,""" +
            """"y":$y,""" +
            """"z":$z,""" +
            """"pitch":$pitch,""" +
            """"yaw":$yaw}}"""
    override fun toString() = "Loc[$x, $y, $z, $pitch, $yaw]"
}