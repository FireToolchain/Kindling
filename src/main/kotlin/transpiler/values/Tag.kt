package transpiler.values

import serializer.serializeString

data class Tag(val option: String, val tag: String, val block: String, val action: String) : DFValue {
    override fun serialize() = """{"id":"bl_tag","data":{"option":${serializeString(option)},"tag":${serializeString(tag)},"action":${
        serializeString(
            action
        )
    },"block":${serializeString(block)}}}"""
    override fun toString() = "{$tag = $option}"
}