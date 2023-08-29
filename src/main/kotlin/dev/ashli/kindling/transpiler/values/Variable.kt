package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.transpiler.*

data class Variable(val name: String, val scope: VariableScope) : DFValue {
    override fun serialize() = """{"id":"var","data":{""" +
            """"name":${name.serialize()},""" +
            """"scope":"${scope.serialize()}"}}"""
}