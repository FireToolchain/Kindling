package dev.ashli.kindling.transpiler.codeblocks

import dev.ashli.kindling.transpiler.codeblocks.normal.DFBlock

/**
 * Represents a codeblock that holds other codeblocks
 */
interface CodeHolder {
    fun getCode(): List<DFBlock>
    fun cloneWith(code: List<DFBlock>): DFBlock
}

/**
 * Represents a codeblock that holds other codeblocks, and potentially has an else branch
 */
interface DoubleCodeHolder : CodeHolder {
    fun getElseCode(): List<DFBlock>?
    fun cloneWith(code: List<DFBlock>, elseCode: List<DFBlock>?): DFBlock
    fun flipped(): DFBlock
}