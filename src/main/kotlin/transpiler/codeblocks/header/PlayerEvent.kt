package transpiler.codeblocks.header

import serializer.serialize

data class PlayerEvent(val type: String) : DFHeader {
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"event",""" +
            """"args":{"items":[]},""" +
            """"action":${type.serialize()}}"""
    override fun technicalName() = type
    override fun toString() = "PlayerEvent[$type]"
}