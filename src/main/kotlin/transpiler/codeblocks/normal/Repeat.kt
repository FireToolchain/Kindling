package transpiler.codeblocks.normal

import serializer.serializeArgs
import serializer.serializeString
import transpiler.values.DFValue

data class Repeat(val type: String, val subtype: String, val inverse: Boolean, val params: List<DFValue>) :
    DFBlock {
    override val technicalName: String
        get() = "repeat"
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"repeat",""" +
            """"args":${serializeArgs(params)},""" +
            if (subtype.isEmpty()) { "" } else { """"subAction":${serializeString(type)},""" } +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${serializeString(type)}""" +
            """},{"id":"bracket","direct":"open","type":"repeat"}"""
    override fun toString() = "Repeat.$type.$subtype(${ params.joinToString( ", ") { it.toString() } }) ~{"
}