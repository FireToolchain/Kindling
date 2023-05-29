package transpiler.codeblocks.normal

import MalformedList
import Value
import serializer.serialize
import serializer.serializeArgs
import transpiler.*
import transpiler.codeblocks.CodeHolder
import transpiler.codeblocks.header.DFHeader
import transpiler.values.DFValue

data class Repeat(val type: String, val subtype: String, val inverse: Boolean, val params: List<DFValue>, val codeblocks: List<DFBlock>) :
    DFBlock("repeat", 4 + codeblocks.sumOf { it.literalSize }), CodeHolder {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): Repeat {
            val inpList = checkList(input)
            if (inpList.size != 6) throw MalformedList("CodeBlock", "(repeat String<Action> String<Subaction> Identifier<Invert> List<Parameters> List<CodeBlocks>)", input)
            val action = checkStr(inpList[1])
            return Repeat(action, checkStr(inpList[2]), checkBool(inpList[3]), checkParams(inpList[4], CheckContext(header, "repeat", action)), checkBlocks(inpList[5], header))
        }
    }
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"repeat",""" +
            """"args":${serializeArgs(params)},""" +
            if (subtype.isEmpty()) { "" } else { """"subAction":${type.serialize()},""" } +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${type.serialize()}""" +
            """},{"id":"bracket","direct":"open","type":"repeat"},""" +
            codeblocks.joinToString("") { it.serialize() + "," } +
            """{"id":"bracket","direct":"close","type":"repeat"}"""
    override fun getCode() = this.codeblocks
    override fun cloneWith(code: List<DFBlock>) = Repeat(type, subtype, inverse, params, code)
}