package transpiler.codeblocks.normal

import MalformedList
import UnexpectedValue
import Value
import serializer.serialize
import serializer.serializeArgs
import transpiler.*
import transpiler.codeblocks.header.DFHeader
import transpiler.values.DFValue
import transpiler.values.Variable

data class Control(val type: String, val params: List<DFValue>) : DFBlock {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): List<DFBlock> {
            val inpList = checkList(input)
            if (inpList.isEmpty()) throw MalformedList("CodeBlock", "(Identifier ...)", input)
            return when (checkIdent(inpList[0])) {
                "return" -> {
                    if (inpList.size != 2) throw MalformedList("CodeBlock", "(return List<Value>)", input)
                    listOf(
                        SetVar("=", listOf(Variable("^ret", VariableScope.LOCAL), checkVal(inpList[1], CheckContext(header, "set_var", "=")))),
                        SetVar("-=", listOf(Variable("^depth ${header.technicalName()}", VariableScope.LOCAL))),
                        Control("Return", emptyList())
                    )
                }
                "yield" -> {
                    if (inpList.size != 2) throw MalformedList("CodeBlock", "(yield List<Value>)", input)
                    listOf(SetVar("=", listOf(Variable("^ret", VariableScope.LOCAL), checkVal(inpList[1], CheckContext(header, "set_var", "=")))))
                }
                "control" -> {
                    if (inpList.size != 3) throw MalformedList("CodeBlock", "(control String<Type> List<Arguments>)", input)
                    val action = checkStr(inpList[1])
                    listOf(Control(action, checkParams(inpList[2], CheckContext(header, "control", action))))
                }
                else -> throw UnexpectedValue("return, yield, or control", inpList[0])
            }
        }
    }
    override val technicalName: String
        get() = "control"
    override val literalSize: Int
        get() = 2
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"control",""" +
            """"args":${serializeArgs(params)},""" +
            """"action":${type.serialize()}""" +
            "}"
    override fun toString() = "Control.$type(${ params.joinToString( ", ") { it.toString() } })"
}