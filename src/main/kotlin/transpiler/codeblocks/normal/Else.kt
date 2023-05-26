package transpiler.codeblocks.normal

import MalformedList
import Value
import transpiler.checkList
import transpiler.codeblocks.header.DFHeader

object Else : DFBlock {
    fun transpileFrom(input: Value, header: DFHeader): Else {
        val inpList = checkList(input)
        if (inpList.size != 1) throw MalformedList("CodeBlock", "(else)", input)
        return Else
    }
    override val technicalName: String
        get() = "else"
    override val literalSize: Int
        get() = 4
    override fun serialize() =
            """{"id":"bracket","direct":"close","type":"norm"},""" +
            """{"id":"block","block":"else"},""" +
            """{"id":"bracket","direct":"open","type":"norm"}"""
    override fun toString() = "} else {"
}