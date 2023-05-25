package transpiler.codeblocks.normal

import serializer.serialize
import serializer.serializeArgs
import transpiler.values.DFValue

data class StartProcess(val name: String, val params: List<DFValue>) : DFBlock {
    override val technicalName: String
        get() = "start_process"
    override val literalSize: Int
        get() = 2
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"start_process",""" +
            """"args":${serializeArgs(params)},""" +
            """"data":${name.serialize()}""" +
            "}"
    override fun toString() = "StartProc $name(${ params.joinToString( ", ") { it.toString() } })"
}