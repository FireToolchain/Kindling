package transpiler.codeblocks.normal

import MalformedList
import Value
import serializer.serialize
import transpiler.*
import transpiler.codeblocks.header.DFHeader
import transpiler.values.DFValue
import transpiler.values.Number
import transpiler.values.Variable

data class CallFunction(val name: String) : DFBlock {
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
    override val technicalName: String
        get() = "call_func"
    override val literalSize: Int
        get() = 2
    override fun serialize() =  "{" +
            """"id":"block",""" +
            """"block":"call_func",""" +
            """"args":{"items":[]},""" +
            """"data":${name.serialize()}""" +
            "}"
    override fun toString() = "CallFunc $name"
}