package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.toInner
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

data class StyledText(val comp: Component) : DFValue {
    companion object {
        fun fromMiniMessage(message: String): StyledText = StyledText(MiniMessage.miniMessage().deserialize(message))
    }
    override fun serialize() = """{"id":"comp","data":{"name":"${MiniMessage.miniMessage().serialize(comp)}"}}"""

}
