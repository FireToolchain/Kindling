package refactoring

import transpiler.codeblocks.normal.*

data class Run(val index: Int, val blockCount: Int, val size: Int)

/**
 * Shortens a DFLine down into a maximum size.
 * maxSize should be 50, 100, or 300 for normal DF plots.
 *
 * Returns a List<DFLine>, size > 0.
 * First element is always the original this, shortened. The rest are extension functions.
 */
/*
fun DFLine.shortenLine(maxSize: Int): List<DFLine> {
    if (this.code.isEmpty()) return listOf(this)
    var lastSize = 0
    var size = 0
    var lastIndex = 0
    var index = 0
    mainLoop@while (index < this.code.size && size < maxSize - 4) {
        lastSize = size
        lastIndex = index
        when (this.code[index]) {
            is IfPlayer, is IfVariable, is IfGame, is IfEntity, is Repeat -> {
                var depth = 0
                do {
                    when (val innerBlock = this.code[index]) {
                        is IfPlayer, is IfVariable, is IfGame, is IfEntity, is Repeat -> depth++
                        is EndRepeat, is EndIf -> depth--
                        is Control -> if (innerBlock.type == "Return") break@mainLoop
                        else -> {}
                    }
                    size += this.code[index].literalSize
                    index++
                } while (depth > 0 && index < this.code.size && size < maxSize - 4)
            }
            is EndRepeat, is EndIf -> break@mainLoop
            else -> {
                size += this.code[index].literalSize
                index++
            }
        }
    }
    return if (index >= this.code.size) {
        listOf(this)
    } else {
        val shortened = this.code.subList(lastIndex, this.code.size)
        val newMain = this.code.toMutableList().subList(0, lastIndex)
        if (newMain.isEmpty()) {
            throw CompileError("Could not shorten event or function ${this.header.technicalName()} to the desired plot size.")
        } else if (shortened.isEmpty()) {
            listOf(this)
        } else {
            val extension = createExtension(shortened) // Extension function
            newMain.add(CallFunction(extension.header.technicalName()))
            val original = DFLine(this.header, newMain)
            val out = mutableListOf(original)
            out.addAll(extension.shortenLine(maxSize))
            out
        }
    }
}*/