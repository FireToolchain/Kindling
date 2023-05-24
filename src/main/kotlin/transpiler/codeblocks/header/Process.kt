package transpiler.codeblocks.header

import serializer.serialize

data class Process(val name: String) : DFHeader {
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"process",""" +
            """"args":{},""" +
            """"data":${name.serialize()}}"""
    override fun technicalName() = name
    override fun toString() = "PlayerEvent[$name]"
}