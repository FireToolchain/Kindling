package transpiler.codeblocks.normal

import MalformedList
import Value
import transpiler.CheckContext
import transpiler.checkList
import transpiler.codeblocks.header.DFHeader

object EndIf : DFBlock {
    fun transpileFrom(input: Value, header: DFHeader): EndIf {
        val inpList = checkList(input)
        if (inpList.size != 1) throw MalformedList("CodeBlock", "(end-if)", input)
        return EndIf
    }
    override val technicalName: String
        get() = "close"
    override val literalSize: Int
        get() = 2
    override fun serialize() = """{"id":"bracket","direct":"close","type":"norm"}"""
    override fun toString() = "}"
}