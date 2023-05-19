import transpiler.DFProgram
import transpiler.DFValue

interface DFSerializable {
    fun serialize(): String
}

fun serializeString(s: String): String {
    return s.replace("'", "\\'")
            .replace(Regex("""\\([^']|$)"""), "\\\\$1")
}

fun serializeArgs(args: List<DFValue>): String {
    TODO()
}

fun sendPackage(prog: DFProgram) {

}


