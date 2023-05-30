package dev.ashli.kindling

import dev.ashli.kindling.refactoring.liftCodeblocks
import dev.ashli.kindling.refactoring.removeUnreachable
import dev.ashli.kindling.refactoring.shortenLine
import dev.ashli.kindling.transpiler.codeblocks.header.DFHeader

/**
 * Represents a DF Program, which is a list of DFHeaders.
 */
data class DFProgram(val lines: List<DFHeader>) {
    fun optimized(maxSize: Int) = DFProgram(lines.flatMap { it.removeUnreachable().liftCodeblocks().shortenLine(maxSize) })
}