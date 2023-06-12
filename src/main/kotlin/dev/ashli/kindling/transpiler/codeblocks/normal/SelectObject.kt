package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.MalformedList
import dev.ashli.kindling.Value
import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.serializer.serializeArgs
import dev.ashli.kindling.transpiler.*
import dev.ashli.kindling.transpiler.codeblocks.header.DFHeader
import dev.ashli.kindling.transpiler.values.DFValue

data class SelectObject(val type: String, val subtype: String, val inverse: Boolean, val params: List<DFValue>) :
    DFBlock("select_obj", 2) {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): SelectObject {
            val inpList = checkList(input)
            if (inpList.size != 5) throw MalformedList("CodeBlock", "(select-object String<Action> String<Subaction> Identifier<Invert> List<Parameters>)", input)
            val action = checkStr(inpList[1])
            return SelectObject(action, checkStr(inpList[2]), checkBool(inpList[3]), checkParams(inpList[4], CheckContext(header, "select_obj", action)))
        }
    }
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"select_obj",""" +
            """"args":${serializeArgs(params)},""" +
            if (subtype.isEmpty()) { "" } else { """"subAction":${type.serialize()},""" } +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${type.serialize()}""" +
            """}"""
}
