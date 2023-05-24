package transpiler.codeblocks.normal

import serializer.serializeArgs
import serializer.serializeString
import transpiler.values.DFValue

data class SetVar(val type: String, val params: List<DFValue>) : DFBlock {
    override val technicalName: String
        get() = "set_var"
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"set_var",""" +
            """"args":${serializeArgs(params)},""" +
            """"action":${serializeString(type)}""" +
            "}"
    override fun toString() = "SetVar.$type(${ params.joinToString( ", ") { it.toString() } })"
}