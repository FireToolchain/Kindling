package transpiler.codeblocks.normal

import serializer.serializeArgs
import serializer.serializeString
import transpiler.values.DFValue

data class Control(val type: String, val params: List<DFValue>) : DFBlock {
    override val technicalName: String
        get() = "control"
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"control",""" +
            """"args":${serializeArgs(params)},""" +
            """"action":${serializeString(type)}""" +
            "}"
    override fun toString() = "Control.$type(${ params.joinToString( ", ") { it.toString() } })"
}