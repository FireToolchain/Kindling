package transpiler.codeblocks.header

import serializer.serialize

data class EntityEvent(val type: String) : DFHeader {
    override fun serialize() = """{"id":"block",""" +
            """"block":"entity_event",""" +
            """"args":{"items":[]},""" +
            """"action":${type.serialize()}}"""
    override fun technicalName() = type
    override fun toString() = "PlayerEvent[$type]"
}