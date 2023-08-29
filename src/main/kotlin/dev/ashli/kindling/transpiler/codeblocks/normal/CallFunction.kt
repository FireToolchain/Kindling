package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.serializer.serialize

/**
 * Represents DiamondFire's Call Function block.
 * @param name Name of the function to call.
 */
data class CallFunction(val name: String) : DFBlock("call_func", 2) {
    override fun serialize() =  "{" +
            """"id":"block",""" +
            """"block":"call_func",""" +
            """"args":{"items":[]},""" +
            """"data":${name.serialize()}""" +
            "}"
}