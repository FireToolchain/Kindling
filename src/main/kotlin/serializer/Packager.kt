package serializer

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
    val out = mutableListOf<String>()
    var mainIndex = 0
    var endIndex = 26
    for (arg in args) {
        if (endIndex <= mainIndex) {
            throw SerializationError("Total chest parameters exceed 27.", arg)
        }
        if (arg is DFValue.Tag) {
            out.add("""{"item":${arg.serialize()},"slot":$endIndex}""")
            endIndex--
        } else {
            out.add("""{"item":${arg.serialize()},"slot":$mainIndex}""")
            mainIndex++
        }
    }
    return """{"items":[${out.joinToString(",")}]}"""
}

fun sendPackage(prog: DFProgram) {
    val templates = prog.lines.map(DFLine::serialize)
    for (s in templates) {
        println(s)
    }
}


