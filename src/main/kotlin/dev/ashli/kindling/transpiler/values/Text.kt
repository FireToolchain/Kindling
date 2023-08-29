package dev.ashli.kindling.transpiler.values

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import kotlin.String

/**
 * Represents a Text on DiamondFire.
 * @param comp The component to use.
 */
data class Text(val comp: Component) : DFValue {
    companion object {
        /**
         * Generates a Text from a String using MiniMessage.
         * @param message The string to reference
         */
        fun fromMiniMessage(message: String): Text = Text(MiniMessage.miniMessage().deserialize(message))
    }
    override fun serialize() = """{"id":"comp","data":{"name":"${MiniMessage.miniMessage().serialize(comp)}"}}"""

}
