package transpiler.codeblocks.normal

import MalformedList
import Value
import transpiler.CheckContext
import transpiler.checkList
import transpiler.checkParams
import transpiler.checkStr
import transpiler.codeblocks.header.DFHeader

object EndRepeat : DFBlock {
    fun transpileFrom(input: Value, header: DFHeader): EndRepeat {
        val inpList = checkList(input)
        if (inpList.size != 1) throw MalformedList("CodeBlock", "(end-repeat)", input)
        return EndRepeat
    }
    override val technicalName: String
        get() = "close"
    override val literalSize: Int
        get() = 2
    override fun serialize() = """{"id":"bracket","direct":"close","type":"repeat"}"""
    override fun toString() = "}~"
}

