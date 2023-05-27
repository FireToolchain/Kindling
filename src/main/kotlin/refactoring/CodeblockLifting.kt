package refactoring

import DFLine
import transpiler.codeblocks.normal.Control

fun DFLine.liftCodeblocks(): DFLine {
    // TODO remove duplicate codeblocks from IF/ELSE statements and place them before/after

    // Removes any Returns leftover at the end of a line
    val reduced = this.code.toMutableList()
    while (reduced.size > 0) {
        val final = reduced[reduced.size-1]
        if (final is Control && final.type == "Return") {
            reduced.removeAt(reduced.size-1)
        } else break
    }
    return DFLine(this.header, reduced)
}