import refactoring.*
import serializer.DFSerializable
import transpiler.codeblocks.header.DFHeader
import transpiler.codeblocks.normal.*

/**
 * Represents a DF Program, which is a list of DFHeaders.
 */
data class DFProgram(val lines: List<DFHeader>) {
    fun optimized(maxSize: Int) = DFProgram(lines.flatMap { it.removeUnreachable().liftCodeblocks().shortenLine(maxSize) })
}