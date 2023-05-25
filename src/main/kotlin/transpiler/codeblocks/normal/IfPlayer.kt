package transpiler.codeblocks.normal

import serializer.serialize
import serializer.serializeArgs
import transpiler.Selector
import transpiler.values.DFValue

data class IfPlayer(val type: String, val selector: Selector, val inverse: Boolean, val params: List<DFValue>) :
    DFBlock {
    override val technicalName: String
        get() = "if_player"
    override val literalSize: Int
        get() = 2
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"if_player",""" +
            """"args":${serializeArgs(params)},""" +
            """"action":${type.serialize()},""" +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"target":"${selector.serialize()}",""" +
            """},{"id":"bracket","direct":"open","type":"norm"}"""
    override fun toString() = "IfPlayer.$type<$selector>(${ params.joinToString( ", ") { it.toString() } }) {"
}