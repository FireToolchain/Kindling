package transpiler.codeblocks.normal

import serializer.serializeArgs
import serializer.serializeString
import transpiler.values.DFValue

data class StartProcess(val name: String, val params: List<DFValue>) : DFBlock {
    override val technicalName: String
        get() = "start_process"
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"start_process",""" +
            """"args":${serializeArgs(params)},""" +
            """"data":${serializeString(name)}""" +
            "}"
    override fun toString() = "StartProc $name(${ params.joinToString( ", ") { it.toString() } })"
}