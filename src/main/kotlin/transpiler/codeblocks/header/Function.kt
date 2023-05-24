package transpiler.codeblocks.header

import serializer.serializeString

data class Function(val name: String) : DFHeader {
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"func",""" +
            """"args":{"items":[{"item":{"id":"bl_tag","data":{"option":"False","tag":"Is Hidden","action":"dynamic","block":"func"}},"slot":26}]},""" +
            """"data":${serializeString(name)}}"""
    override fun technicalName() = name
    override fun toString() = "PlayerEvent[$name]"
}