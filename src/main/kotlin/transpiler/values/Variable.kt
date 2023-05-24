package transpiler.values

import MalformedList
import UnexpectedValue
import Value
import serializer.serializeString
import transpiler.*

data class Variable(val name: String, val scope: VariableScope) : DFValue {
    companion object {
        fun transpileFrom(input: Value, context: CheckContext): Variable {
            val inpList = checkList(input)
            if (inpList.size != 2) throw MalformedList("Value", "(Scope<VariableScope> String<Name>)", input)
            return when (checkIdent(inpList[1])) {
                "save", "global", "local" -> Variable(checkStr(inpList[1]), checkScope(inpList[0]))
                "var" -> Variable(
                    "^var ${context.header.technicalName()} ^ ${checkStr(inpList[1])} %var(^depth ${context.header.technicalName()})",
                    VariableScope.LOCAL
                )
                "param" -> Variable(
                    "^param ${context.header.technicalName()} ${checkInt(inpList[0])} %var(^depth ${context.header.technicalName()})",
                    VariableScope.LOCAL
                )
                else -> throw UnexpectedValue("a valid variable type", inpList[1])
            }
        }
    }
    override fun serialize() = """{"id":"var","data":{""" +
            """"name":${serializeString(name)},""" +
            """"scope":"${scope.serialize()}"}}"""
    override fun toString() = "$scope[$name]"
}