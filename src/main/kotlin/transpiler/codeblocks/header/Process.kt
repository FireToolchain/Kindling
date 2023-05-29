package transpiler.codeblocks.header

import MalformedList
import Value
import serializer.serialize
import transpiler.*
import transpiler.codeblocks.normal.DFBlock
import transpiler.codeblocks.normal.Repeat
import transpiler.codeblocks.normal.SetVar
import transpiler.values.Variable

class Process(val name: String, blocks: List<DFBlock>) : DFHeader(blocks) {
    companion object {
        fun transpileFrom(input: Value): Process {
            val inpList = checkList(input)
            if (inpList.size != 3) throw MalformedList("Header", "(process String<Name> List<CodeBlock>)", input)
            val dummy = Process(checkStr(inpList[1]), listOf())
            val blocks = listOf(SetVar("+=", listOf(Variable("^depth ${dummy.name}", VariableScope.LOCAL)))) +
                    checkBlocks(inpList[2], dummy) +
                    SetVar("-=", listOf(Variable("^depth ${dummy.name}", VariableScope.LOCAL)))
            return Process(dummy.name, blocks)
        }
    }
    override fun serialize() = super.serializeLine("{" +
            """"id":"block",""" +
            """"block":"process",""" +
            """"args":{"items":[]},""" +
            """"data":${name.serialize()}}""")
    override fun technicalName() = name

    override fun getItemName() = """{"extra":[""" +
            """{"italic":false,"color":"#FF9955","text":"Process"},""" +
            """{"italic":false,"bold":true,"color":"#666666","text":" » | "},""" +
            """{"italic":false,"color":"#FFCC88","text":${this.technicalName().trim().serialize()}}""" +
            """],"text":""}"""

    override fun getItemType() = "campfire"
    override fun cloneWith(code: List<DFBlock>) = Process(this.name, code)
}