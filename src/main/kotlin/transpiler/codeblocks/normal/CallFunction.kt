package transpiler.codeblocks.normal

import serializer.serializeString

data class CallFunction(val name: String) : DFBlock {
    override val technicalName: String
        get() = "call_func"
    override fun serialize() =  "{" +
            """"id":"block",""" +
            """"block":"call_func",""" +
            """"args":{"items":[]},""" +
            """"data":${serializeString(name)}""" +
            "}"
    override fun toString() = "CallFunc $name"
}