package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.serializer.serializeArgs
import dev.ashli.kindling.transpiler.values.DFValue

data class Control(val type: String, val params: List<DFValue>) : DFBlock("control", 2, type == "End" || type == "Return") {
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"control",""" +
            """"args":${serializeArgs(params)},""" +
            """"action":${type.serialize()}""" +
            "}"
}