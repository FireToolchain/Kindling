package dev.ashli.kindling.transpiler.codeblocks.header

import dev.ashli.kindling.MalformedList
import dev.ashli.kindling.Value
import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.transpiler.*
import dev.ashli.kindling.transpiler.codeblocks.normal.DFBlock
import dev.ashli.kindling.transpiler.codeblocks.normal.SetVar
import dev.ashli.kindling.transpiler.values.Variable

class Function(val name: String, blocks: List<DFBlock>) : DFHeader(blocks) {
    companion object {
        fun transpileFrom(input: Value): Function {
            val inpList = checkList(input)
            if (inpList.size != 3) throw MalformedList("Header", "(def String<Name> List<CodeBlock>)", input)
            val dummy = Function(checkStr(inpList[1]), listOf())
            val blocks = listOf(SetVar("+=", listOf(Variable("^depth ${dummy.name}", VariableScope.LOCAL)))) +
                    checkBlocks(inpList[2], dummy) +
                    SetVar("-=", listOf(Variable("^depth ${dummy.name}", VariableScope.LOCAL)))
            return Function(dummy.name, blocks)
        }
    }
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"func",""" +
            """"args":{"items":[{"item":{"id":"bl_tag","data":{"option":"False","tag":"Is Hidden","action":"dynamic","block":"func"}},"slot":26}]},""" +
            """"data":${name.serialize()}}""" +
            this.code.joinToString("") { "," + it.serialize() }
    override fun technicalName() = name

    override fun getItemName() = """{"extra":[""" +
            """{"italic":false,"color":"#FF9955","text":"Function"},""" +
            """{"italic":false,"bold":true,"color":"#666666","text":" » | "},""" +
            """{"italic":false,"color":"#FFCC88","text":${this.technicalName().trim().serialize()}}""" +
            """],"text":""}"""

    override fun getItemType() = "campfire"
    override fun cloneWith(code: List<DFBlock>) = Function(this.name, code)
}