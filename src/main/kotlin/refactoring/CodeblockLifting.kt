package refactoring

import DFLine
import transpiler.codeblocks.normal.*

fun DFLine.liftCodeblocks(): DFLine {
    if (this.code.isEmpty()) return this

    val reduced = mutableListOf<DFBlock>()
    val blockIndex = 0
    while (blockIndex < this.code.size) {
        val curBlock = this.code[blockIndex]
        when (curBlock) {
            is IfPlayer, is IfVariable, is IfEntity, is IfGame -> {

            }
            else -> {

            }
        }
    }

    // Removes any Returns leftover at the end of a line
    while (reduced.isNotEmpty()) {
        val final = reduced[reduced.size-1]
        if (final is Control && final.type == "Return") {
            reduced.removeAt(reduced.size-1)
        } else break
    }
    return DFLine(this.header, reduced)
}