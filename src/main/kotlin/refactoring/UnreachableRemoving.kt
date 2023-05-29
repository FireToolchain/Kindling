package refactoring

import transpiler.codeblocks.CodeHolder
import transpiler.codeblocks.DoubleCodeHolder
import transpiler.codeblocks.header.DFHeader
import transpiler.codeblocks.normal.*


fun DFHeader.removeUnreachable(): DFHeader {
    return this.cloneWith(this.code.removeUnreachable())
}

fun List<DFBlock>.removeUnreachable(): List<DFBlock> {
    val out = mutableListOf<DFBlock>()

    var index = 0
    while (index < this.size) {
        var block = this[index]
        if (block is DoubleCodeHolder) {
            block = block.cloneWith(block.getCode().removeUnreachable(), block.getElseCode()?.removeUnreachable())
        } else if (block is CodeHolder) {
            block = block.cloneWith(block.getCode().removeUnreachable())
        }
        out.add(block)
        if (block.isFinal) break

        index++
    }

    return out
}