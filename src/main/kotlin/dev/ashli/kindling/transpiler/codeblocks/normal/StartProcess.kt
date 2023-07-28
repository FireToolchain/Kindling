package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.MalformedList
import dev.ashli.kindling.Value
import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.serializer.serializeArgs
import dev.ashli.kindling.transpiler.*
import dev.ashli.kindling.transpiler.codeblocks.header.DFHeader
import dev.ashli.kindling.transpiler.values.DFValue
import dev.ashli.kindling.transpiler.values.Number
import dev.ashli.kindling.transpiler.values.Tag
import dev.ashli.kindling.transpiler.values.Variable

data class StartProcess(val name: String, val params: List<DFValue>) : DFBlock("start_process", 2) {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): List<DFBlock> {
            val inpList = checkList(input)
            if (inpList.size != 3) throw MalformedList("CodeBlock", "(call String<Action> List<Arguments>", input)
            val action = checkStr(inpList[1])
            val params = checkParams(inpList[2], CheckContext(header, "call_func", action))
            val blocks = mutableListOf<DFBlock>()
            var tagList = listOf(Tag("Copy", "Local Variables", "start_process", "dynamic", null))

            for ((paramNum, p) in params.withIndex()) {
                when(p) {
                    is Tag -> {
                        tagList = listOf(p) + tagList
                    }
                    else -> {
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
                }
            }

            if(!tagList.contains(Tag("With current targets", "Target Mode", "start_process", "dynamic", null))
                && !tagList.contains(Tag("With current selection", "Target Mode", "start_process", "dynamic", null))
                    && !tagList.contains(Tag("With no targets", "Target Mode", "start_process", "dynamic", null))
                    && !tagList.contains(Tag("For each in selection", "Target Mode", "start_process", "dynamic", null))) {
                        tagList = listOf(Tag("With current targets", "Target Mode", "start_process", "dynamic", null)) + tagList
                }

            blocks.add(StartProcess(action, tagList))
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