package refactoring

import DFLine
import transpiler.codeblocks.normal.*

fun DFLine.removeUnreachable(): DFLine {
    if (this.code.isEmpty()) return this

    val out = arrayOfNulls<DFBlock?>(this.code.size)
    val escapeStack = mutableListOf<Int>()

    var runner = 0
    mainLoop@while (runner < this.code.size) {
        out[runner] = this.code[runner]
        when (val block = this.code[runner]) {
            is Control -> if (block.type == "Return" || block.type == "End") {
                escapeStack[escapeStack.size-1]++
                var depth = 0
                while (depth >= 0 && runner < this.code.size) {
                    when (this.code[runner]) {
                        is IfPlayer, is IfVariable, is IfEntity, is IfGame, is Repeat -> depth++
                        is EndIf, is EndRepeat -> depth--
                        is Else -> if (depth == 0) depth--
                        else -> {}
                    }
                    runner++
                }
                out[runner-1] = this.code[runner-1]
            }
            is IfPlayer, is IfVariable, is IfEntity, is IfGame, is Repeat -> {
                escapeStack.add(0)
            }
            is EndIf, is EndRepeat -> {
                if (escapeStack[escapeStack.size-1] >= 2) break@mainLoop
                escapeStack.removeAt(escapeStack.size-1)
            }
            else -> {}
        }
        runner++ // Move forward
    }
    return DFLine(this.header, out.filterNotNull())
}