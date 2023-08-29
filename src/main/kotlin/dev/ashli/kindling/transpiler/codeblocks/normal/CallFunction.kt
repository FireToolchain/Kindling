package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.serializer.serializeArgs
import dev.ashli.kindling.transpiler.values.DFValue

/**
 * Represents DiamondFire's Call Function block.
 * @param name Name of the function to call
 * @param arguments Arguments to call function with
 */
data class CallFunction(val name: String, val arguments: List<DFValue>) : DFBlock("call_func", 2) {
    override fun serialize() =  "{" +
            """"id":"block",""" +
            """"block":"call_func",""" +
            """"args":${serializeArgs(arguments)},""" +
            """"data":${name.serialize()}""" +
            "}"
}