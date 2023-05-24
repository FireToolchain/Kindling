package transpiler.codeblocks.normal

import serializer.serialize
import serializer.serializeArgs
import transpiler.values.DFValue

data class SelectObject(val type: String, val subtype: String, val inverse: Boolean, val params: List<DFValue>) :
    DFBlock {
    override val technicalName: String
        get() = "select_obj"
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"select_obj",""" +
            """"args"":${serializeArgs(params)},""" +
            if (subtype.isEmpty()) { "" } else { """"subAction":${type.serialize()},""" } +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${type.serialize()}""" +
            """},{"id":"bracket","direct":"open","type":"norm"}"""
    override fun toString() = "SelectObject.$type.$subtype(${ params.joinToString( ", ") { it.toString() } })"
}