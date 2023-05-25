package transpiler.codeblocks.normal

import serializer.serialize
import serializer.serializeArgs
import transpiler.values.DFValue

data class GameAction(val type: String, val params: List<DFValue>) : DFBlock {
    override val technicalName: String
        get() = "game_action"
    override val literalSize: Int
        get() = 2
    override fun serialize() =  "{" +
            """"id":"block",""" +
            """"block":"game_action",""" +
            """"args":${serializeArgs(params)},""" +
            """"action":${type.serialize()}""" +
            "}"
    override fun toString() = "GameAction.$type(${ params.joinToString( ", ") { it.toString() } })"
}