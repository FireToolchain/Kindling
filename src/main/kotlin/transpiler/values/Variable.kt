package transpiler.values

import serializer.serializeString
import transpiler.VariableScope

data class Variable(val name: String, val scope: VariableScope) : DFValue {
    override fun serialize() = """{"id":"var","data":{""" +
            """"name":${serializeString(name)},""" +
            """"scope":"${scope.serialize()}"}}"""
    override fun toString() = "$scope[$name]"
}