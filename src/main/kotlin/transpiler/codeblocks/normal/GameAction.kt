package transpiler.codeblocks.normal

import serializer.serializeArgs
import serializer.serializeString
import transpiler.values.DFValue

data class GameAction(val type: String, val params: List<DFValue>) : DFBlock {
    override val technicalName: String
        get() = "game_action"
    override fun serialize() =  "{" +
            """"id":"block",""" +
            """"block":"game_action",""" +
            """"args":${serializeArgs(params)},""" +
            """"action":${serializeString(type)}""" +
            "}"
    override fun toString() = "GameAction.$type(${ params.joinToString( ", ") { it.toString() } })"
}