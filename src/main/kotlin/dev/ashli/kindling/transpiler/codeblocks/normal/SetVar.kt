package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.MalformedList
import dev.ashli.kindling.Value
import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.serializer.serializeArgs
import dev.ashli.kindling.transpiler.CheckContext
import dev.ashli.kindling.transpiler.checkList
import dev.ashli.kindling.transpiler.checkParams
import dev.ashli.kindling.transpiler.checkStr
import dev.ashli.kindling.transpiler.codeblocks.header.DFHeader
import dev.ashli.kindling.transpiler.values.DFValue

data class SetVar(val type: String, val params: List<DFValue>) : DFBlock("set_var", 2) {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): SetVar {
            val inpList = checkList(input)
            if (inpList.size != 3) throw MalformedList("CodeBlock", "(set-var String<Name> List<Parameters>)", input)
            val action = checkStr(inpList[1])
            return SetVar(action, checkParams(inpList[2], CheckContext(header, "set_var", action)))
        }
    }
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"set_var",""" +
            """"args":${serializeArgs(params)},""" +
            """"action":${type.serialize()}""" +
            "}"
}