package transpiler.codeblocks.normal

import serializer.serialize
import serializer.serializeArgs
import transpiler.values.DFValue

data class SetVar(val type: String, val params: List<DFValue>) : DFBlock {
    override val technicalName: String
        get() = "set_var"
    override val literalSize: Int
        get() = 2
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"set_var",""" +
            """"args":${serializeArgs(params)},""" +
            """"action":${type.serialize()}""" +
            "}"
    override fun toString() = "SetVar.$type(${ params.joinToString( ", ") { it.toString() } })"
}