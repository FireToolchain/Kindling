package transpiler.codeblocks.header

import MalformedList
import Value
import serializer.serialize
import transpiler.checkList
import transpiler.checkStr

data class Process(val name: String) : DFHeader {
    companion object {
        fun transpileFrom(input: Value): Process {
            val inpList = checkList(input)
            if (inpList.size != 2) throw MalformedList("Header", "(process String<Name>", input)
            return Process(checkStr(inpList[1]))
        }
    }
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"process",""" +
            """"args":{"items":[]},""" +
            """"data":${name.serialize()}}"""
    override fun technicalName() = name
    override fun toString() = "PlayerEvent[$name]"

    override fun getItemName() = """{"extra":[""" +
            """{"italic":false,"color":"#FF9955","text":"Process"},""" +
            """{"italic":false,"bold":true,"color":"#666666","text":" » | "},""" +
            """{"italic":false,"color":"#FFCC88","text":${this.technicalName().trim().serialize()}}""" +
            """],"text":""}"""

    override fun getItemType() = "campfire"
}