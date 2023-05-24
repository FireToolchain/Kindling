package transpiler.codeblocks.normal

import serializer.serialize
import serializer.serializeArgs
import transpiler.values.DFValue

data class Repeat(val type: String, val subtype: String, val inverse: Boolean, val params: List<DFValue>) :
    DFBlock {
    override val technicalName: String
        get() = "repeat"
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"repeat",""" +
            """"args":${serializeArgs(params)},""" +
            if (subtype.isEmpty()) { "" } else { """"subAction":${type.serialize()},""" } +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${type.serialize()}""" +
            """},{"id":"bracket","direct":"open","type":"repeat"}"""
    override fun toString() = "Repeat.$type.$subtype(${ params.joinToString( ", ") { it.toString() } }) ~{"
}