package transpiler.codeblocks.normal

import MalformedList
import Value
import serializer.serialize
import serializer.serializeArgs
import transpiler.*
import transpiler.codeblocks.header.DFHeader
import transpiler.codeblocks.header.Function
import transpiler.values.DFValue
import transpiler.values.Number
import transpiler.values.Tag
import transpiler.values.Variable

data class StartProcess(val name: String, val params: List<DFValue>) : DFBlock("start_process", 2) {
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
            blocks.add(StartProcess(action, listOf(Tag("Copy", "Local Variables", "start_process", "dynamic", null))))
            return blocks
        }
    }
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"start_process",""" +
            """"args":${serializeArgs(params)},""" +
            """"data":${name.serialize()}""" +
            "}"
}