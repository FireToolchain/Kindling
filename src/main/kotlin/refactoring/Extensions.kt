package refactoring

import transpiler.codeblocks.header.DFHeader
import transpiler.codeblocks.header.Function
import transpiler.codeblocks.normal.DFBlock

var extensionNumber = 0

fun createExtension(blocks: List<DFBlock>): DFHeader {
    return Function("^${extensionNumber++}", blocks)
}