package transpiler.codeblocks.header

import serializer.serializeString

data class Process(val name: String) : DFHeader {
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"process",""" +
            """"args":{},""" +
            """"data":${serializeString(name)}}"""
    override fun technicalName() = name
    override fun toString() = "PlayerEvent[$name]"
}