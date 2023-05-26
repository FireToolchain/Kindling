package transpiler.codeblocks.header

import MalformedList
import Value
import serializer.serialize
import transpiler.checkList
import transpiler.checkStr

data class PlayerEvent(val type: String) : DFHeader {
    companion object {
        fun transpileFrom(input: Value): PlayerEvent {
            val inpList = checkList(input)
            if (inpList.size != 2) throw MalformedList("Header", "(player-event String<Type>", input)
            return PlayerEvent(checkStr(inpList[1]))
        }
    }
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"event",""" +
            """"args":{"items":[]},""" +
            """"action":${type.serialize()}}"""
    override fun technicalName() = type
    override fun toString() = "PlayerEvent[$type]"

    override fun getItemName() = """{"extra":[""" +
            """{"italic":false,"color":"#FF9955","text":"Event"},""" +
            """{"italic":false,"bold":true,"color":"#666666","text":" » | "},""" +
            """{"italic":false,"color":"#FFCC88","text":${this.technicalName().trim().serialize()}}""" +
            """],"text":""}"""

    override fun getItemType() = "soul_campfire"
}