package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.toInner
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

/**
 * Represents a Styled Text on DiamondFire.
 * @param comp The component to use.
 */
data class StyledText(val comp: Component) : DFValue {
    companion object {
        /**
         * Generates a Styled Text from a String using MiniMessage.
         * @param message The string to reference
         */
        fun fromMiniMessage(message: String): StyledText = StyledText(MiniMessage.miniMessage().deserialize(message))
    }
    override fun serialize() = """{"id":"comp","data":{"name":"${MiniMessage.miniMessage().serialize(comp)}"}}"""

}
