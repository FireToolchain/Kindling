package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.MalformedList
import dev.ashli.kindling.Value
import dev.ashli.kindling.transpiler.CheckContext
import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.serializer.serializeArgs
import dev.ashli.kindling.transpiler.checkList
import dev.ashli.kindling.transpiler.checkParams
import dev.ashli.kindling.transpiler.checkStr
import dev.ashli.kindling.transpiler.codeblocks.header.DFHeader
import dev.ashli.kindling.transpiler.values.DFValue

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