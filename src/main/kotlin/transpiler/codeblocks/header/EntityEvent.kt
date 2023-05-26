package transpiler.codeblocks.header

import MalformedList
import Value
import serializer.serialize
import transpiler.CheckContext
import transpiler.checkList
import transpiler.checkSelector
import transpiler.checkStr
import transpiler.values.GameValue

data class EntityEvent(val type: String) : DFHeader {
    companion object {
        fun transpileFrom(input: Value): EntityEvent {
            val inpList = checkList(input)
            if (inpList.size != 2) throw MalformedList("Header", "(entity-event String<Type>", input)
            return EntityEvent(checkStr(inpList[1]))
        }
    }
    override fun serialize() = """{"id":"block",""" +
            """"block":"entity_event",""" +
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