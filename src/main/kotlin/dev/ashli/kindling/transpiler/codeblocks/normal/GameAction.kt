package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.serializer.serializeArgs
import dev.ashli.kindling.transpiler.values.DFValue

data class GameAction(val type: String, val params: List<DFValue>) : DFBlock("game_action", 2) {
    override fun serialize() =  "{" +
            """"id":"block",""" +
            """"block":"game_action",""" +
            """"args":${serializeArgs(params)},""" +
            """"action":${type.serialize()}""" +
            "}"
}