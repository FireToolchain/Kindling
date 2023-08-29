package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.serializer.serializeArgs
import dev.ashli.kindling.transpiler.codeblocks.CodeHolder
import dev.ashli.kindling.transpiler.values.DFValue

/**
 * Represents DiamondFire's repeat block.
 * @param type The action of the code block
 * @param subtype The secondary action of the codeblock
 * @param inverse This is `true` if the block should have NOT on it's sign
 * @param params Parameters to the code block
 * @param codeblocks The branch to repeat on
 */
data class Repeat(val type: String, val subtype: String, val inverse: Boolean, val params: List<DFValue>, val codeblocks: List<DFBlock>) :
    DFBlock("repeat", 4 + codeblocks.sumOf { it.literalSize }), CodeHolder {

    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"repeat",""" +
            """"args":${serializeArgs(params)},""" +
            if (subtype.isEmpty()) { "" } else { """"subAction":${subtype.serialize()},""" } +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${type.serialize()}""" +
            """},{"id":"bracket","direct":"open","type":"repeat"},""" +
            codeblocks.joinToString("") { it.serialize() + "," } +
            """{"id":"bracket","direct":"close","type":"repeat"}"""
    override fun getCode() = this.codeblocks
    override fun cloneWith(code: List<DFBlock>) = Repeat(type, subtype, inverse, params, code)
}