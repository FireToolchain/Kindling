package transpiler.codeblocks.normal

import MalformedList
import Value
import serializer.serialize
import serializer.serializeArgs
import transpiler.*
import transpiler.codeblocks.header.DFHeader
import transpiler.values.DFValue

data class GameAction(val type: String, val params: List<DFValue>) : DFBlock("game_action", 2) {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): GameAction {
            val inpList = checkList(input)
            if (inpList.size != 3) throw MalformedList("CodeBlock", "(game-action String<Action> List<Parameters>)", input)
            val action = checkStr(inpList[1])
            return GameAction(action, checkParams(inpList[2], CheckContext(header, "game_action", action)))
        }
    }
    override fun serialize() =  "{" +
            """"id":"block",""" +
            """"block":"game_action",""" +
            """"args":${serializeArgs(params)},""" +
            """"action":${type.serialize()}""" +
            "}"
}