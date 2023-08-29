package dev.ashli.kindling.transpiler.values


/**
 * Represents a location on DiamondFire.
 * @param x The X coordinate
 * @param y The Y coordinate
 * @param z The Z coordinate
 * @param pitch The pitch of the location
 * @param yaw The yaw of the location
 */
data class Location(val x: Float, val y: Float, val z: Float, val pitch: Float, val yaw: Float) : DFValue {
    override fun serialize() = """{"id":"loc","data":{""" +
            """"isBlock":false,"loc":{""" +
            """"x":$x,""" +
            """"y":$y,""" +
            """"z":$z,""" +
            """"pitch":$pitch,""" +
            """"yaw":$yaw}}}"""
}