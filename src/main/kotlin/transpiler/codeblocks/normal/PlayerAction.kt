package transpiler.codeblocks.normal

import serializer.serialize
import serializer.serializeArgs
import transpiler.Selector
import transpiler.values.DFValue

data class PlayerAction(val type: String, val selector: Selector, val params: List<DFValue>) : DFBlock {
    override val technicalName: String
        get() = "player_action"
    override val literalSize: Int
        get() = 2
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"player_action",""" +
            """"args":${serializeArgs(params)},""" +
            """"target":"${selector.serialize()}",""" +
            """"action":${type.serialize()}""" +
            "}"
    override fun toString() = "PlayerAction.$type<$selector>(${ params.joinToString( ", ") { it.toString() } })"
}