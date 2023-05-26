package transpiler.codeblocks.header

import serializer.serialize

data class EntityEvent(val type: String) : DFHeader {
    override fun serialize() = """{"id":"block",""" +
            """"block":"entity_event",""" +
            """"args":{"items":[]},""" +
            """"action":${type.serialize()}}"""
    override fun technicalName() = type
    override fun toString() = "PlayerEvent[$type]"

    override fun getItemName() = """{"extra":[""" +
            """{"italic":false,"color":"#BB5500","text":"🔥 "},""" +
            """{"italic":false,"color":"#FF8822","text":"${this.technicalName().trim()}"},""" +
            """{"italic":false,"color":"#666666","text":" (Event)"}""" +
            """],"text":""}"""
}