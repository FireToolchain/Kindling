package dev.ashli.kindling.transpiler.codeblocks.header

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.serializer.serializeArgs
import dev.ashli.kindling.transpiler.codeblocks.normal.DFBlock
import dev.ashli.kindling.transpiler.values.Item
import dev.ashli.kindling.transpiler.values.Parameter
import dev.ashli.kindling.transpiler.values.Tag

class Function(val name: String, blocks: List<DFBlock>, val icon: Item? = null, val parameters: List<Parameter> = listOf()) : DFHeader(blocks) {
//    override fun serialize() = "{" +
//            """"id":"block",""" +
//            """"block":"func",""" +
//            """"args":{"items":[{"item":{"id":"bl_tag","data":{"option":"False","tag":"Is Hidden","action":"dynamic","block":"func"}},"slot":26],""" +
//            """"data":${name.serialize()}}""" +
//            this.code.joinToString("") { "," + it.serialize() }

    override fun serialize(): String {
        val args: String = if (icon != null)
            serializeArgs(listOf(icon) + parameters + Tag("False", "Is Hidden", "func", "dynamic", null))
        else
            serializeArgs(parameters + Tag("False", "Is Hidden", "func", "dynamic", null))


        return """
{
    "id":"block",
    "block":"func",
    "args":$args,
    "data":${name.serialize()}
}
    """.trimIndent().replace('\n', ' ')
    }
    override fun technicalName() = name

    override fun getItemName() = """{"extra":[""" +
            """{"italic":false,"color":"#FF9955","text":"Function"},""" +
            """{"italic":false,"bold":true,"color":"#666666","text":" » | "},""" +
            """{"italic":false,"color":"#FFCC88","text":${this.technicalName().trim().serialize()}}""" +
            """],"text":""}"""

    override fun getItemType() = "campfire"
    override fun cloneWith(code: List<DFBlock>) = Function(this.name, code)
}