package transpiler.codeblocks.normal

import MalformedList
import Value
import serializer.serialize
import serializer.serializeArgs
import transpiler.*
import transpiler.codeblocks.header.DFHeader
import transpiler.values.DFValue

data class IfGame(val type: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): IfGame {
            val inpList = checkList(input)
            if (inpList.size != 4) throw MalformedList("CodeBlock", "(if-game String<Action> Identifier<Invert> List<Parameters>)", input)
            val action = checkStr(inpList[1])
            return IfGame(action, checkBool(inpList[3]), checkParams(inpList[4], CheckContext(header, "if_game", action)))
        }
    }
    override val technicalName: String
        get() = "if_game"
    override val literalSize: Int
        get() = 2
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"if_game",""" +
            """"args":${serializeArgs(params)},""" +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${type.serialize()}""" +
            """},{"id":"bracket","direct":"open","type":"norm"}"""
    override fun toString() = "IfGame.$type(${ params.joinToString( ", ") { it.toString() } }) {"
}