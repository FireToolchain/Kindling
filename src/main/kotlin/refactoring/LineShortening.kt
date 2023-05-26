package refactoring

import DFLine
import org.jetbrains.annotations.Contract
import transpiler.codeblocks.normal.*

data class Run(val index: Int, val blockCount: Int, val size: Int)

/**
 * Shortens a DFLine down into a maximum size.
 * maxSize should be 50, 100, or 300 for normal DF plots.
 *
 * Returns a List<DFLine>, size > 0.
 * First element is always the original line, shortened. The rest are extension functions.
 */
fun shortenLine(line: DFLine, maxSize: Int): List<DFLine> {
    var lastSize = 0
    var size = 0
    var lastIndex = 0
    var index = 0
    mainLoop@while (index < line.code.size && size < maxSize - 4) {
        lastSize = size
        lastIndex = index
        when (line.code[index]) {
            is IfPlayer, is IfVariable, is IfGame, is IfEntity, is Repeat -> {
                var depth = 0
                do {
                    when (val innerBlock = line.code[index]) {
                        is IfPlayer, is IfVariable, is IfGame, is IfEntity, is Repeat -> depth++
                        is EndRepeat, is EndIf -> depth--
                        is Control -> if (innerBlock.type == "Return") break@mainLoop
                        else -> {}
                    }
                    size += line.code[index].literalSize
                    index++
                } while (depth > 0 && size < maxSize - 4)
            }
            is EndRepeat, is EndIf -> break@mainLoop
            else -> {
                size += line.code[index].literalSize
                index++
            }
        }
    }
    return if (index >= line.code.size) {
        listOf(line)
    } else {
        val shortened = line.code.subList(lastIndex, line.code.size)
        val newMain = line.code.toMutableList().subList(0, lastIndex)
        if (newMain.isEmpty()) {
            throw CompileError("Could not shorten event or function ${line.header.technicalName()} to the desired plot size.")
        } else if (shortened.isEmpty()) {
            listOf(line)
        } else {
            val extension = createExtension(shortened) // Extension function
            newMain.add(CallFunction(extension.header.technicalName()))
            val original = DFLine(line.header, newMain)
            val out = mutableListOf(original)
            out.addAll(shortenLine(extension, maxSize))
            out
        }
    }
}