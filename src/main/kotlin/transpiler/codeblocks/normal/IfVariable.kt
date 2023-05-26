package transpiler.codeblocks.normal

import MalformedList
import Value
import serializer.serialize
import serializer.serializeArgs
import transpiler.*
import transpiler.codeblocks.header.DFHeader
import transpiler.values.DFValue

data class IfVariable(val type: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): IfVariable {
            val inpList = checkList(input)
            if (inpList.size != 4) throw MalformedList("CodeBlock", "(if-var String<Action> Identifier<Invert> List<Parameters>)", input)
            val action = checkStr(inpList[1])
            return IfVariable(action, checkBool(inpList[2]), checkParams(inpList[3], CheckContext(header, "if_var", action)))
        }
    }
    override val technicalName: String
        get() = "if_var"
    override val literalSize: Int
        get() = 2
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"if_var",""" +
            """"args":${serializeArgs(params)},""" +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${type.serialize()}""" +
            """},{"id":"bracket","direct":"open","type":"norm"}"""
    override fun toString() = "IfVar.$type(${ params.joinToString( ", ") { it.toString() } }) {"
}