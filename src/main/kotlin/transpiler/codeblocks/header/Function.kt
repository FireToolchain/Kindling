package transpiler.codeblocks.header

import MalformedList
import Value
import serializer.serialize
import transpiler.checkList
import transpiler.checkStr

data class Function(val name: String) : DFHeader {
    companion object {
        fun transpileFrom(input: Value): Function {
            val inpList = checkList(input)
            if (inpList.size != 2) throw MalformedList("Header", "(def String<Name>", input)
            return Function(checkStr(inpList[1]))
        }
    }
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"func",""" +
            """"args":{"items":[{"item":{"id":"bl_tag","data":{"option":"False","tag":"Is Hidden","action":"dynamic","block":"func"}},"slot":26}]},""" +
            """"data":${name.serialize()}}"""
    override fun technicalName() = name
    override fun toString() = "PlayerEvent[$name]"

    override fun getItemName() = """{"extra":[""" +
            """{"italic":false,"color":"#FF9955","text":"Function"},""" +
            """{"italic":false,"bold":true,"color":"#666666","text":" » | "},""" +
            """{"italic":false,"color":"#FFCC88","text":${this.technicalName().trim().serialize()}}""" +
            """],"text":""}"""

    override fun getItemType() = "campfire"
}