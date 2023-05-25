package refactoring

import DFLine
import transpiler.codeblocks.header.Function
import transpiler.codeblocks.normal.DFBlock

var extensionNumber = 0

fun createExtension(blocks: List<DFBlock>): DFLine {
    return DFLine(Function("^${extensionNumber++}"), blocks)
}