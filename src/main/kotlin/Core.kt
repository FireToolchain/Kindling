import refactoring.createExtension
import refactoring.shortenLine
import serializer.DFSerializable
import transpiler.codeblocks.header.DFHeader
import transpiler.codeblocks.normal.*

/**
 * Represents a DF Program, which is a list of DFLines.
 */
data class DFProgram(val lines: List<DFLine>) {
    override fun toString() = lines.joinToString("\n") { it.toString() }

    fun optimized(maxSize: Int): DFProgram {
        val newLines = mutableListOf<DFLine>()
        for (line in lines) {
            newLines.addAll(shortenLine(line, maxSize))
        }
        return DFProgram(newLines)
    }
}

/**
 * Represents a DFLine, which is a header and a list of codeblocks.
 */
data class DFLine(val header: DFHeader, val code: List<DFBlock>) : DFSerializable {
    override fun serialize() =
        if (code.isEmpty()) { """{"blocks":[${header.serialize()}]}""" }
        else { """{"blocks":[${header.serialize()},${code.joinToString(",") { it.serialize() }}]}""" }


    // A line has a header and a list of statements
    override fun toString(): String {
        var out = "$header:\n"
        var indent = 0
        for (s in code) {
            if (s is EndIf || s is EndRepeat || s is Else) {
                indent--
                if (indent < 0) indent = 0
            }
            out += "${"  ".repeat(indent + 1)}$s\n"
            if (s is IfPlayer || s is IfGame || s is IfEntity || s is IfVariable || s is Else || s is Repeat) {
                indent++
            }
        }
        return out
    }
}

