package dev.ashli.kindling.transpiler.codeblocks.header

import dev.ashli.kindling.MalformedList
import dev.ashli.kindling.Value
import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.transpiler.VariableScope
import dev.ashli.kindling.transpiler.checkBlocks
import dev.ashli.kindling.transpiler.checkList
import dev.ashli.kindling.transpiler.checkStr
import dev.ashli.kindling.transpiler.codeblocks.normal.DFBlock
import dev.ashli.kindling.transpiler.codeblocks.normal.SetVar
import dev.ashli.kindling.transpiler.values.Variable

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
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"process",""" +
            """"args":{"items":[]},""" +
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