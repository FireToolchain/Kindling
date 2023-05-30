package dev.ashli.kindling.refactoring

import dev.ashli.kindling.transpiler.codeblocks.CodeHolder
import dev.ashli.kindling.transpiler.codeblocks.DoubleCodeHolder
import dev.ashli.kindling.transpiler.codeblocks.header.DFHeader
import dev.ashli.kindling.transpiler.codeblocks.normal.DFBlock


fun DFHeader.removeUnreachable() = this.cloneWith(this.code.removeUnreachable())

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