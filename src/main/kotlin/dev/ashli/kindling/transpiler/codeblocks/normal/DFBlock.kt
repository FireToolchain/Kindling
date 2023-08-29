package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.serializer.DFSerializable

/**
 * Represents a DF Block, such as a Player Action, Set Variable, or If Game.
 * @param technicalName Name of the codeblock in DF Template JSON format.
 * @param literalSize ???
 * @param isFinal ???
 */
sealed class DFBlock(val technicalName: String, val literalSize: Int, val isFinal: Boolean = false) : DFSerializable