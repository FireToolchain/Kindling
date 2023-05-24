package transpiler.codeblocks.normal

import serializer.serializeArgs
import serializer.serializeString
import transpiler.values.DFValue

data class IfVariable(val type: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock {
    override val technicalName: String
        get() = "if_var"
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"if_var",""" +
            """"args":${serializeArgs(params)},""" +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${serializeString(type)}""" +
            """},{"id":"bracket","direct":"open","type":"norm"}"""
    override fun toString() = "IfVar.$type(${ params.joinToString( ", ") { it.toString() } }) {"
}