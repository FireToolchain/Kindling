package transpiler.codeblocks.header

import serializer.serialize

data class Process(val name: String) : DFHeader {
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"process",""" +
            """"args":{"items":[]},""" +
            """"data":${name.serialize()}}"""
    override fun technicalName() = name
    override fun toString() = "PlayerEvent[$name]"

    override fun getItemName() = """{"extra":[""" +
            """{"italic":false,"color":"#FF7722","text":"🔥 "},""" +
            """{"italic":false,"color":"#FFAA55","text":"${this.technicalName().trim()}"},""" +
            """{"italic":false,"color":"#666666","text":" (Process)"}""" +
            """],"text":""}"""
}