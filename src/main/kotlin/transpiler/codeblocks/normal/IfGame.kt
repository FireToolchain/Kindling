package transpiler.codeblocks.normal

import serializer.serialize
import serializer.serializeArgs
import transpiler.values.DFValue

data class IfGame(val type: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock {
    override val technicalName: String
        get() = "if_game"
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"if_game",""" +
            """"args":${serializeArgs(params)},""" +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${type.serialize()}""" +
            """},{"id":"bracket","direct":"open","type":"norm"}"""
    override fun toString() = "IfGame.$type(${ params.joinToString( ", ") { it.toString() } }) {"
}