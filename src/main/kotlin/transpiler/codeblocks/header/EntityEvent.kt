package transpiler.codeblocks.header

import serializer.serializeString

data class EntityEvent(val type: String) : DFHeader {
    override fun serialize() = """{"id":"block",""" +
            """"block":"entity_event",""" +
            """"args":{"items":[]},""" +
            """"action":${serializeString(type)}}"""
    override fun technicalName() = type
    override fun toString() = "PlayerEvent[$type]"
}