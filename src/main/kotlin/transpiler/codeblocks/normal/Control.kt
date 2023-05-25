package transpiler.codeblocks.normal

import serializer.serialize
import serializer.serializeArgs
import transpiler.values.DFValue

data class Control(val type: String, val params: List<DFValue>) : DFBlock {
    override val technicalName: String
        get() = "control"
    override val literalSize: Int
        get() = 2
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"control",""" +
            """"args":${serializeArgs(params)},""" +
            """"action":${type.serialize()}""" +
            "}"
    override fun toString() = "Control.$type(${ params.joinToString( ", ") { it.toString() } })"
}