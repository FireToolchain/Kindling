package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.serialize

/**
 * Represents a Block Tag on DiamondFire.
 * @param option The option selected in the tag
 * @param tag The tag to consider
 * @param block The block this tag is in
 * @param action The action of the block this tag is in
 * @param variable The variable to use as the option
 */
data class Tag(val option: String, val tag: String, val block: String, val action: String, val variable: Variable?) :
    DFValue {
    override fun serialize() = """{"id":"bl_tag","data":{"option":${option.serialize()},"tag":${tag.serialize()},"action":${
        action.serialize()
    },"block":${block.serialize()}""" +
    (if (variable != null) ""","variable":${variable.serialize()}""" else "") +
    """}}"""
}