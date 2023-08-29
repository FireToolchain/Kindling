package dev.ashli.kindling.transpiler.codeblocks.header

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.transpiler.codeblocks.normal.DFBlock

class Process(val name: String, blocks: List<DFBlock>) : DFHeader(blocks) {

    override fun serialize() = """{""" +
        """"id":"block",""" +
        """"block":"process",""" +
        """"args":{"items":[""" +
        """{""" +
            """"item": {""" +
            """"id": "bl_tag",""" +
            """"data": {""" +
                """"option": "False",""" +
                """"tag": "Is Hidden",""" +
                """"action": "dynamic",""" +
                """"block": "process"""" +
            """}""" +
        """}, "slot": 26""" +
        """}""" +
        """]},""" +
        """"data":${name.serialize()}}""" +
            this.code.joinToString("") { "," + it.serialize() }
    override fun technicalName() = name

    override fun getItemName() = """{"extra":[""" +
            """{"italic":false,"color":"#FF9955","text":"Process"},""" +
            """{"italic":false,"bold":true,"color":"#666666","text":" » | "},""" +
            """{"italic":false,"color":"#FFCC88","text":${this.technicalName().trim().serialize()}}""" +
            """],"text":""}"""

    override fun getItemType() = "campfire"
    override fun cloneWith(code: List<DFBlock>) = Process(this.name, code)
}