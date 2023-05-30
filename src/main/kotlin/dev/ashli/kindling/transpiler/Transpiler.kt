package dev.ashli.kindling.transpiler

import dev.ashli.kindling.MalformedList
import dev.ashli.kindling.UnexpectedValue
import dev.ashli.kindling.Value
import dev.ashli.kindling.transpiler.codeblocks.header.DFHeader
import dev.ashli.kindling.DFProgram
import dev.ashli.kindling.transpiler.codeblocks.header.EntityEvent
import dev.ashli.kindling.transpiler.codeblocks.header.Function
import dev.ashli.kindling.transpiler.codeblocks.header.PlayerEvent
import dev.ashli.kindling.transpiler.codeblocks.header.Process

/**
 * Stores context for a check/parse.
 */
data class CheckContext(val header: DFHeader, val blockType: String, val blockAction: String)

/**
 * Transpiles a list of parsed values into a DF Program.
 */
fun transpile(input: List<Value>): DFProgram {
    val lines = mutableListOf<DFHeader>()
    for (v in input)
        lines.add(parseHeader(v))
    return DFProgram(lines)
}

/**
 * Takes a parsed value and attempts to transpile it into a DFHeader
 */
fun parseHeader(input: Value): DFHeader {
    val vList = checkList(input)
    if (vList.isEmpty()) throw MalformedList("Header", "(Identifier<Type> ...)", input)

    return when (checkIdent(vList[0])) {
        "player-event" -> PlayerEvent.transpileFrom(input)
        "entity-event" -> EntityEvent.transpileFrom(input)
        "def" -> Function.transpileFrom(input)
        "process" -> Process.transpileFrom(input)
        else -> throw UnexpectedValue("a valid header type", vList[0])
    }
}

