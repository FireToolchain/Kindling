import transpiler.DFLine
import transpiler.DFProgram
import transpiler.DFValue

interface DFSerializable {
    fun serialize(): String
}

fun serializeString(s: String): String {
    return '"' + s.replace("'", "\\'")
            .replace(Regex("""\\([^']|$)"""), "\\\\$1") + '"'
}

fun serializeArgs(args: List<DFValue>): String {
    return """{"TODO":"params"}"""
}

fun sendPackage(prog: DFProgram) {
    val templates = prog.lines.map(DFLine::serialize)
    for (s in templates) {
        println(s)
    }
}


