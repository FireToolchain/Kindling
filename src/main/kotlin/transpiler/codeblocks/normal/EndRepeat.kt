package transpiler.codeblocks.normal

object EndRepeat : DFBlock {
    override val technicalName: String
        get() = "close"
    override val literalSize: Int
        get() = 2
    override fun serialize() = """{"id":"bracket","direct":"close","type":"repeat"}"""
    override fun toString() = "}~"
}