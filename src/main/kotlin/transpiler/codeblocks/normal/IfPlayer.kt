package transpiler.codeblocks.normal

import MalformedList
import Value
import serializer.serialize
import serializer.serializeArgs
import transpiler.*
import transpiler.codeblocks.header.DFHeader
import transpiler.values.DFValue

data class IfPlayer(val type: String, val selector: Selector, val inverse: Boolean, val params: List<DFValue>, val mainBranch: List<DFBlock>, val elseBranch: List<DFBlock>?) :
    DFBlock("if_player", 4) {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): IfPlayer {
            val inpList = checkList(input)
            return if (inpList.size == 6) {
                val action = checkStr(inpList[1])
                IfPlayer(action, checkSelector(inpList[2]), checkBool(inpList[3]), checkParams(inpList[4], CheckContext(header, "if_entity", action)), checkBlocks(inpList[5], header), null)
            } else if (inpList.size == 7) {
                val action = checkStr(inpList[1])
                IfPlayer(action, checkSelector(inpList[2]), checkBool(inpList[3]), checkParams(inpList[4], CheckContext(header, "if_entity", action)), checkBlocks(inpList[5], header), checkBlocks(inpList[6], header))
            } else throw MalformedList("CodeBlock", "(if-entity String<Action> Identifier<Selector> Identifier<Invert> List<Parameters> List<IfBlocks> List<ElseBlocks>?)", input)
        }
    }
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"if_player",""" +
            """"args":${serializeArgs(params)},""" +
            """"action":${type.serialize()},""" +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"target":"${selector.serialize()}"""" +
            """},{"id":"bracket","direct":"open","type":"norm"},""" +
            mainBranch.joinToString("") { it.serialize() + "," } +
            """{"id":"bracket","direct":"close","type":"norm"}""" +
            if (elseBranch != null) {
                """,{"id":"block","block":"else"},""" +
                """{"id":"bracket","direct":"open","type":"norm"},""" +
                elseBranch.joinToString("") { it.serialize() + "," } +
                """{"id":"bracket","direct":"close","type":"norm"}"""
            } else ""
}