package transpiler.codeblocks.normal

object EndIf : DFBlock {
    override val technicalName: String
        get() = "close"
    override val literalSize: Int
        get() = 2
    override fun serialize() = """{"id":"bracket","direct":"close","type":"norm"}"""
    override fun toString() = "}"
}