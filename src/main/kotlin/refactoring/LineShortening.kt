package refactoring

import transpiler.codeblocks.header.DFHeader
import transpiler.codeblocks.normal.*

data class Run(val index: Int, val blockCount: Int, val size: Int)

/**
 * Shortens a DFLine down into a maximum size.
 * maxSize should be 50, 100, or 300 for normal DF plots.
 *
 * Returns a List<DFLine>, size > 0.
 * First element is always the original this, shortened. The rest are extension functions.
 */

fun DFHeader.shortenLine(maxSize: Int): List<DFHeader> {
    // Return early if possible
    if (this.code.isEmpty()) return listOf(this)

    // Find point in code where the left half is short enough, but right half isn't.
    var size = 0
    var index = 0
    while (index < this.code.size && size < maxSize - 4) {
        size += this.code[index].literalSize
        index++
    }

    // Reached end of code; no need to shorten
    if (index >= this.code.size) return listOf(this)

    // Split the line into two parts
    index--
    val shortened = this.code.subList(index, this.code.size)
    val newMain = this.code.subList(0, index)

    // Perform checks
    if (newMain.isEmpty()) throw CompileError("Could not shorten event or function ${this.technicalName()} to the desired plot size.")
    if (shortened.isEmpty()) return listOf(this)

    // Return optimized code
    val extension = createExtension(shortened) // Extension function
    val extendedMain = newMain + CallFunction(extension.technicalName()) // Original with callFunc added
    return listOf(this.cloneWith(extendedMain)) + extension.shortenLine(maxSize)
}