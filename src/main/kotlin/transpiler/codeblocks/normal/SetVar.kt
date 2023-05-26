package transpiler.codeblocks.normal

import MalformedList
import Value
import serializer.serialize
import serializer.serializeArgs
import transpiler.CheckContext
import transpiler.checkList
import transpiler.checkParams
import transpiler.checkStr
import transpiler.codeblocks.header.DFHeader
import transpiler.values.DFValue

data class SetVar(val type: String, val params: List<DFValue>) : DFBlock {
    companion object {
        fun transpileFrom(input: Value, header: DFHeader): SetVar {
            val inpList = checkList(input)
            if (inpList.size != 3) throw MalformedList("CodeBlock", "(set-var String<Name> List<Parameters>)", input)
            val action = checkStr(inpList[1])
            return SetVar(action, checkParams(inpList[2], CheckContext(header, "set_var", action)))
        }
    }
    override val technicalName: String
        get() = "set_var"
    override val literalSize: Int
        get() = 2
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"set_var",""" +
            """"args":${serializeArgs(params)},""" +
            """"action":${type.serialize()}""" +
            "}"
    override fun toString() = "SetVar.$type(${ params.joinToString( ", ") { it.toString() } })"
}