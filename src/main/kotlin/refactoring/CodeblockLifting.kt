package refactoring

import transpiler.codeblocks.normal.*

/**
 * Takes a DFLine and will remove duplicate blocks inside of If/Else structures.
 * For example:
 *
 * if ? {
 *   x = 2
 *   y = 3
 *   z = 3
 * } else {
 *   x = 2
 *   y = 4
 *   z = 3
 * }
 *
 * will become
 *
 * x = 2
 * if ? {
 *   y = 3
 * } else {
 *   y = 4
 * }
 * z = 3
 *
 * (pseudocode, of course)
 */
/*
fun DFLine.liftCodeblocks(): DFLine {
    if (this.code.isEmpty()) return this

    val codeblocks: MutableList<DFBlock?> = this.code.toMutableList()
    val reduced = mutableListOf<DFBlock>()
    var blockIndex = 0
    while (blockIndex < codeblocks.size) {
        when (val curBlock = codeblocks[blockIndex]) {
            null -> {}
            is IfPlayer, is IfVariable, is IfEntity, is IfGame -> {
                var elseIndex = blockIndex
                var depth = 0
                do {
                    when (codeblocks[elseIndex]) {
                        is IfPlayer, is IfVariable, is IfGame, is IfEntity, is Repeat -> depth++
                        is EndRepeat, is EndIf -> depth--
                        is Else -> if (depth == 1) depth = 0
                        else -> {}
                    }
                    elseIndex++
                } while (depth > 0 && elseIndex < codeblocks.size)
                if (elseIndex < codeblocks.size) { // If there is an attached else
                    var atBeginning = true

                } else reduced.add(curBlock)
            }
            else -> reduced.add(curBlock)
        }
        blockIndex++
    }

    // Removes any Returns leftover at the end of a line
    while (reduced.isNotEmpty()) {
        val final = reduced[reduced.size-1]
        if (final is Control && final.type == "Return") {
            reduced.removeAt(reduced.size-1)
        } else break
    }
    return DFLine(this.header, reduced)
}*/