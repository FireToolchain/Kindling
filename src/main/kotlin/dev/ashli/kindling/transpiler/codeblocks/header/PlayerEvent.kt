package dev.ashli.kindling.transpiler.codeblocks.header

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.transpiler.codeblocks.normal.DFBlock

class PlayerEvent(val type: String, blocks: List<DFBlock>) : DFHeader(blocks) {
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"event",""" +
            """"args":{"items":[]},""" +
            """"action":${type.serialize()}}""" +
            this.code.joinToString("") { "," + it.serialize() }
    override fun technicalName() = type

    override fun getItemName() = """{"extra":[""" +
            """{"italic":false,"color":"#FF9955","text":"Event"},""" +
            """{"italic":false,"bold":true,"color":"#666666","text":" » | "},""" +
            """{"italic":false,"color":"#FFCC88","text":${this.technicalName().trim().serialize()}}""" +
            """],"text":""}"""

    override fun getItemType() = "soul_campfire"
    override fun cloneWith(code: List<DFBlock>) = PlayerEvent(this.type, code)
}