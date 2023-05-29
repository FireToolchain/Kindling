package refactoring

import transpiler.codeblocks.DoubleCodeHolder
import transpiler.codeblocks.header.DFHeader
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

fun DFHeader.liftCodeblocks(): DFHeader {
    if (this.code.isEmpty()) return this
    val code = this.code.toMutableList()

    val newCode: MutableList<DFBlock> = code.flatMap {
        when (it) {
            is DoubleCodeHolder -> {
                if (it.getElseCode() != null) {
                    val beforeCode = mutableListOf<DFBlock>()
                    val afterCode = mutableListOf<DFBlock>()
                    val mainCode = it.getCode().toMutableList()
                    val elseCode = it.getElseCode()?.toMutableList()!!
                    while (mainCode.size > 0 && elseCode.size > 0 && mainCode[0] == elseCode[0]) {
                        beforeCode.add(mainCode[0])
                        mainCode.removeAt(0)
                        elseCode.removeAt(0)
                    }
                    while (mainCode.size > 0 && elseCode.size > 0 && mainCode[mainCode.size-1] == elseCode[elseCode.size-1]) {
                        afterCode.add(0, mainCode[mainCode.size-1])
                        mainCode.removeAt(mainCode.size-1)
                        elseCode.removeAt(elseCode.size-1)
                    }
                    when {
                        // Both empty
                        mainCode.isEmpty() && elseCode.isEmpty() -> {
                            beforeCode + afterCode
                        }
                        // If { } else { ... }
                        mainCode.isNotEmpty() && elseCode.isEmpty() -> {
                            beforeCode + (it.flipped() as DoubleCodeHolder).cloneWith(elseCode, null) + afterCode
                        }
                        // If { ... } else {}
                        mainCode.isEmpty() -> {
                            beforeCode + it.cloneWith(mainCode, null) + afterCode
                        }
                        // If { ... } else { ... }
                        else -> {
                            beforeCode + it.cloneWith(mainCode, elseCode) + afterCode
                        }
                    }
                } else listOf(it)
            }
            else -> listOf(it)
        }
    }.toMutableList()
    while (newCode.size > 0) {
        val current = newCode[newCode.size-1]
        if (current is Control && (current.type == "End" || current.type == "Return")) {
            newCode.removeAt(newCode.size-1)
        } else break
    }
    return this.cloneWith(newCode)

}