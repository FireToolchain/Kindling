package transpiler.codeblocks.normal

import serializer.serialize

data class CallFunction(val name: String) : DFBlock {
    override val technicalName: String
        get() = "call_func"
    override fun serialize() =  "{" +
            """"id":"block",""" +
            """"block":"call_func",""" +
            """"args":{"items":[]},""" +
            """"data":${name.serialize()}""" +
            "}"
    override fun toString() = "CallFunc $name"
}