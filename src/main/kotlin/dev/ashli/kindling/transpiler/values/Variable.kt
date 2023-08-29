package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.transpiler.*
import kotlin.String

/**
 * Represents a variable on DiamondFire.
 * @param name The name of the variable
 * @param scope The scope of the variable
 */
data class Variable(val name: String, val scope: VariableScope) : DFValue {
    override fun serialize() = """{"id":"var","data":{""" +
            """"name":${name.serialize()},""" +
            """"scope":"${scope.serialize()}"}}"""
}