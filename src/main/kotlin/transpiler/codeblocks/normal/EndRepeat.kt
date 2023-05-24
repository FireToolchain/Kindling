package transpiler.codeblocks.normal

object EndRepeat : DFBlock {
    override val technicalName: String
        get() = "close"
    override fun serialize() = """{"id":"bracket","direct":"close","type":"repeat"}"""
    override fun toString() = "}~"
}