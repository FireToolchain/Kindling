package transpiler.codeblocks.normal

import serializer.DFSerializable

/**
 * Represents a DF Block, such as a Player Action, Set Variable, or If Game.
 */
sealed class DFBlock(val technicalName: String, val literalSize: Int, val isFinal: Boolean = false) : DFSerializable