package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.serialize

data class Tag(val option: String, val tag: String, val block: String, val action: String, val variable: Variable?) :
    DFValue {
    override fun serialize() = """{"id":"bl_tag","data":{"option":${option.serialize()},"tag":${tag.serialize()},"action":${
        action.serialize()
    },"block":${block.serialize()}""" +
    (if (variable != null) ""","variable":${variable.serialize()}""" else "") +
    """}}"""
}