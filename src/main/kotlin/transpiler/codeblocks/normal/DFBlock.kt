package transpiler.codeblocks.normal

import serializer.DFSerializable

/**
 * Represents a DF Block, such as a Player Action, Set Variable, or If Game.
 */
sealed interface DFBlock : DFSerializable {
    val technicalName: String

    /**
     * The size of the block in DiamondFire. A player action should be 2.
     */
    val literalSize: Int
}