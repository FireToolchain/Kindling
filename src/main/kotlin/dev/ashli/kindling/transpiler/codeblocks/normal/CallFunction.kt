package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.MalformedList
import dev.ashli.kindling.Value
import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.transpiler.*
import dev.ashli.kindling.transpiler.codeblocks.header.DFHeader
import dev.ashli.kindling.transpiler.values.Number
import dev.ashli.kindling.transpiler.values.Variable

data class CallFunction(val name: String) : DFBlock("call_func", 2) {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): List<DFBlock> {
            val inpList = checkList(input)
            if (inpList.size != 3) throw MalformedList("CodeBlock", "(call String<Action> List<Arguments>", input)
            val action = checkStr(inpList[1])
            val params = checkParams(inpList[2], CheckContext(header, "call_func", action))
            val blocks = mutableListOf<DFBlock>()
            for ((paramNum, p) in params.withIndex()) {
                blocks.add(
                    SetVar(
                        "+", listOf(
                            Variable("^depthCalc", VariableScope.LOCAL),
                            Variable("^depth $action", VariableScope.LOCAL),
                            Number(1f)
                        )
                    )
                )
                blocks.add(
                    SetVar(
                        "=", listOf(
                            Variable("^param $action $paramNum %var(^depthCalc)", VariableScope.LOCAL),
                            p
                        )
                    )
                )
            }
            blocks.add(CallFunction(action))
            return blocks
        }
    }
    override fun serialize() =  "{" +
            """"id":"block",""" +
            """"block":"call_func",""" +
            """"args":{"items":[]},""" +
            """"data":${name.serialize()}""" +
            "}"
}