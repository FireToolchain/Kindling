package transpiler.codeblocks.normal

import MalformedList
import Value
import serializer.serialize
import serializer.serializeArgs
import transpiler.*
import transpiler.codeblocks.header.DFHeader
import transpiler.values.DFValue

data class IfGame(val type: String, val inverse: Boolean, val params: List<DFValue>, val mainBranch: List<DFBlock>, val elseBranch: List<DFBlock>?) : DFBlock("if_game", 4) {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): IfGame {
            val inpList = checkList(input)
            return if (inpList.size == 5) {
                val action = checkStr(inpList[1])
                IfGame(action, checkBool(inpList[2]), checkParams(inpList[3], CheckContext(header, "if_entity", action)), checkBlocks(inpList[4], header), null)
            } else if (inpList.size == 6) {
                val action = checkStr(inpList[1])
                IfGame(action, checkBool(inpList[2]), checkParams(inpList[3], CheckContext(header, "if_entity", action)), checkBlocks(inpList[4], header), checkBlocks(inpList[5], header))
            } else throw MalformedList("CodeBlock", "(if-game String<Action> Identifier<Invert> List<Parameters> List<IfBlocks> List<ElseBlocks>?)", input)
        }
    }
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"if_game",""" +
            """"args":${serializeArgs(params)},""" +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${type.serialize()}""" +
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