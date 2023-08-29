package dev.ashli.kindling.transpiler.values

/**
 * Represents a vector on DiamondFire.
 * @param x X component
 * @param y Y component
 * @param z Z component
 */
data class Vector(val x: Float, val y: Float, val z: Float) : DFValue {
    override fun serialize() = """{"id":"vec","data":{"x":$x,"y":$y,"z":$z}}"""
}