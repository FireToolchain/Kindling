package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.serializer.serializeArgs
import dev.ashli.kindling.transpiler.values.DFValue

data class StartProcess(val name: String, val params: List<DFValue>) : DFBlock("start_process", 2) {
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"start_process",""" +
            """"args":${serializeArgs(params)},""" +
            """"data":${name.serialize()}""" +
            "}"
}