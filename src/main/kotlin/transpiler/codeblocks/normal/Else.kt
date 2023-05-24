package transpiler.codeblocks.normal

object Else : DFBlock {
    override val technicalName: String
        get() = "else"
    override fun serialize() =
            """{"id":"bracket","direct":"close","type":"norm"},""" +
            """{"id":"block","block":"else"},""" +
            """{"id":"bracket","direct":"open","type":"norm"}"""
    override fun toString() = "} else {"
}