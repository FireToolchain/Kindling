package transpiler.codeblocks.normal

import MalformedList
import Value
import serializer.serialize
import serializer.serializeArgs
import transpiler.*
import transpiler.codeblocks.header.DFHeader
import transpiler.values.DFValue

data class Repeat(val type: String, val subtype: String, val inverse: Boolean, val params: List<DFValue>) :
    DFBlock {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): Repeat {
            val inpList = checkList(input)
            if (inpList.size != 5) throw MalformedList("CodeBlock", "(repeat String<Action> String<Subaction> Identifier<Invert> List<Parameters>)", input)
            val action = checkStr(inpList[1])
            return Repeat(action, checkStr(inpList[2]), checkBool(inpList[3]), checkParams(inpList[4], CheckContext(header, "repeat", action)))
        }
    }
    override val technicalName: String
        get() = "repeat"
    override val literalSize: Int
        get() = 2
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"repeat",""" +
            """"args":${serializeArgs(params)},""" +
            if (subtype.isEmpty()) { "" } else { """"subAction":${type.serialize()},""" } +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${type.serialize()}""" +
            """},{"id":"bracket","direct":"open","type":"repeat"}"""
    override fun toString() = "Repeat.$type.$subtype(${ params.joinToString( ", ") { it.toString() } }) ~{"
}