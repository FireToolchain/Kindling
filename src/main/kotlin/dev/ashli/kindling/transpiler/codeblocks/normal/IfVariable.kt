package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.MalformedList
import dev.ashli.kindling.Value
import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.serializer.serializeArgs
import dev.ashli.kindling.transpiler.*
import dev.ashli.kindling.transpiler.codeblocks.DoubleCodeHolder
import dev.ashli.kindling.transpiler.codeblocks.header.DFHeader
import dev.ashli.kindling.transpiler.values.DFValue

data class IfVariable(val type: String, val inverse: Boolean, val params: List<DFValue>, val mainBranch: List<DFBlock>, val elseBranch: List<DFBlock>?) :
    DFBlock("if_var", 4 + mainBranch.sumOf { it.literalSize } + (elseBranch?.sumOf { it.literalSize }?.plus(4) ?: 0),
        elseBranch != null && (mainBranch.any { it.isFinal } && elseBranch.any { it.isFinal })), DoubleCodeHolder {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): IfVariable {
            val inpList = checkList(input)
            return if (inpList.size == 5) {
                val action = checkStr(inpList[1])
                IfVariable(action, checkBool(inpList[2]), checkParams(inpList[3], CheckContext(header, "if_var", action)), checkBlocks(inpList[4], header), null)
            } else if (inpList.size == 6) {
                val action = checkStr(inpList[1])
                IfVariable(action, checkBool(inpList[2]), checkParams(inpList[3], CheckContext(header, "if_var", action)), checkBlocks(inpList[4], header), checkBlocks(inpList[5], header))
            } else throw MalformedList("CodeBlock", "(if-var String<Action> Identifier<Invert> List<Parameters> List<IfBlocks> List<ElseBlocks>?)", input)
        }
    }
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"if_var",""" +
            """"args":${serializeArgs(params)},""" +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${type.serialize()}""" +
            """},{"id":"bracket","direct":"open","type":"norm"},""" +
            mainBranch.joinToString("") { it.serialize() + "," } +
            """{"id":"bracket","direct":"close","type":"norm"}""" +
            if (elseBranch != null) {
                """,{"id":"block","block":"else"},""" +
                """{"id":"bracket","direct":"open","type":"norm"},""" +
                elseBranch.joinToString("") { it.serialize() + "," } +
                """{"id":"bracket","direct":"close","type":"norm"}"""
            } else ""

    override fun getElseCode() = this.elseBranch
    override fun cloneWith(code: List<DFBlock>, elseCode: List<DFBlock>?) = IfVariable(type, inverse, params, code, elseCode)
    override fun cloneWith(code: List<DFBlock>) = IfVariable(type, inverse, params, code, elseBranch)
    override fun flipped() = IfVariable(type, !inverse, params, mainBranch, elseBranch)

    override fun getCode() = this.mainBranch
}