package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.MalformedList
import dev.ashli.kindling.Value
import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.serializer.serializeArgs
import dev.ashli.kindling.transpiler.*
import dev.ashli.kindling.transpiler.codeblocks.header.DFHeader
import dev.ashli.kindling.transpiler.values.DFValue

data class PlayerAction(val type: String, val selector: Selector, val params: List<DFValue>) : DFBlock("player_action", 2) {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): PlayerAction {
            val inpList = checkList(input)
            if (inpList.size != 4) throw MalformedList("CodeBlock", "(player-action String<Action> Identifier<Selector> List<Parameters>)", input)
            val action = checkStr(inpList[1])
            return PlayerAction(action, checkSelector(inpList[2]), checkParams(inpList[3], CheckContext(header, "player_action", action)))
        }
    }
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"player_action",""" +
            """"args":${serializeArgs(params)},""" +
            """"target":"${selector.serialize()}",""" +
            """"action":${type.serialize()}""" +
            "}"
}