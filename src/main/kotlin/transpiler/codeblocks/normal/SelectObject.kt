package transpiler.codeblocks.normal

import serializer.serializeArgs
import serializer.serializeString
import transpiler.values.DFValue

data class SelectObject(val type: String, val subtype: String, val inverse: Boolean, val params: List<DFValue>) :
    DFBlock {
    override val technicalName: String
        get() = "select_obj"
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"select_obj",""" +
            """"args"":${serializeArgs(params)},""" +
            if (subtype.isEmpty()) { "" } else { """"subAction":${serializeString(type)},""" } +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${serializeString(type)}""" +
            """},{"id":"bracket","direct":"open","type":"norm"}"""
    override fun toString() = "SelectObject.$type.$subtype(${ params.joinToString( ", ") { it.toString() } })"
}