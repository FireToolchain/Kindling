package transpiler.codeblocks.normal

import MalformedList
import Value
import serializer.serialize
import serializer.serializeArgs
import transpiler.*
import transpiler.codeblocks.header.DFHeader
import transpiler.values.DFValue

data class IfEntity(val type: String, val selector: Selector, val inverse: Boolean, val params: List<DFValue>) :
    DFBlock {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): IfPlayer {
            val inpList = checkList(input)
            if (inpList.size != 5) throw MalformedList("CodeBlock", "(if-entity String<Action> Identifier<Selector> Identifier<Invert> List<Parameters>)", input)
            val action = checkStr(inpList[1])
            return IfPlayer(action, checkSelector(inpList[2]), checkBool(inpList[3]), checkParams(inpList[4], CheckContext(header, "if_entity", action)))
        }
    }
    override val technicalName: String
        get() = "if_entity"
    override val literalSize: Int
        get() = 2
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"if_entity",""" +
            """"args":${serializeArgs(params)},""" +
            """"action":${type.serialize()},""" +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"target":"${selector.serialize()}",""" +
            """},{"id":"bracket","direct":"open","type":"norm"}"""
    override fun toString() = "IfEntity.$type<$selector>(${ params.joinToString( ", ") { it.toString() } }) {"
}