package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.serializer.serializeArgs
import dev.ashli.kindling.transpiler.values.DFValue

data class SelectObject(val type: String, val subtype: String, val inverse: Boolean, val params: List<DFValue>) :
    DFBlock("select_obj", 2) {

    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"select_obj",""" +
            """"args":${serializeArgs(params)},""" +
            if (subtype.isEmpty()) { "" } else { """"subAction":${subtype.serialize()},""" } +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${type.serialize()}""" +
            """}"""
}
