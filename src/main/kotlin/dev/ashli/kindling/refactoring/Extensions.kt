package dev.ashli.kindling.refactoring

import dev.ashli.kindling.transpiler.codeblocks.header.DFHeader
import dev.ashli.kindling.transpiler.codeblocks.header.Function
import dev.ashli.kindling.transpiler.codeblocks.normal.DFBlock

var extensionNumber = 0

fun createExtension(blocks: List<DFBlock>): DFHeader {
    return Function("^${extensionNumber++}", blocks)
}