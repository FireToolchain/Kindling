import refactoring.*
import serializer.DFSerializable
import transpiler.codeblocks.header.DFHeader
import transpiler.codeblocks.normal.*

/**
 * Represents a DF Program, which is a list of DFHeaders.
 */
data class DFProgram(val lines: List<DFHeader>) {
    override fun toString() = lines.joinToString("\n") { it.toString() }
    //fun optimized(maxSize: Int) = DFProgram(lines.flatMap { it.liftCodeblocks().removeUnreachable().inlineVars().shortenLine(maxSize) })
    //fun optimized(maxSize: Int) = DFProgram(lines.map { it.removeUnreachable() })
    fun optimized(maxSize: Int) = this
}