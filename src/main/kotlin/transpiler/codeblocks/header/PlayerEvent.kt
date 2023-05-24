package transpiler.codeblocks.header

import serializer.serializeString

data class PlayerEvent(val type: String) : DFHeader {
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"event",""" +
            """"args":{"items":[]},""" +
            """"action":${serializeString(type)}}"""
    override fun technicalName() = type
    override fun toString() = "PlayerEvent[$type]"
}