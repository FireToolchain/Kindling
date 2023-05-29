package transpiler.codeblocks.header

import MalformedList
import Value
import serializer.serialize
import transpiler.checkBlock
import transpiler.checkBlocks
import transpiler.checkList
import transpiler.checkStr
import transpiler.codeblocks.normal.DFBlock

class EntityEvent(val type: String, blocks: List<DFBlock>) : DFHeader(blocks) {
    companion object {
        fun transpileFrom(input: Value): EntityEvent {
            val inpList = checkList(input)
            if (inpList.size != 3) throw MalformedList("Header", "(entity-event String<Type> List<CodeBlock>)", input)
            val dummy = EntityEvent(checkStr(inpList[1]), listOf())
            val blocks = checkBlocks(inpList[2], dummy)
            return EntityEvent(dummy.type, blocks)
        }
    }
    override fun serialize() = """{"id":"block",""" +
            """"block":"entity_event",""" +
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
    override fun cloneWith(code: List<DFBlock>) = EntityEvent(this.type, code)
}