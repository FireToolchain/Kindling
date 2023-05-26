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

    override fun getItemName() = """{"extra":[""" +
            """{"italic":false,"color":"#FF7722","text":"Kindling"},""" +
            """{"italic":false,"color":"#666666","text":" » "},""" +
            """{"italic":false,"color":"#FFAA55","text":${this.technicalName().trim().serialize()}}""" +
            """],"text":""}"""

    override fun getItemType() = "diamond_block"
}