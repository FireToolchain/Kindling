package transpiler.codeblocks.normal

import serializer.DFSerializable

/**
 * Represents a DF Block, such as a Player Action, Set Variable, or If Game.
 */
sealed interface DFBlock : DFSerializable {
    val technicalName: String

}