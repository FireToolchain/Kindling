package transpiler

import serializer.DFSerializable

/**
 * Represents a DF Selector.
 */
enum class Selector : DFSerializable {
    DEFAULT { override fun serialize() = "Default" },
    DAMAGER { override fun serialize() = "Damager" },
    KILLER { override fun serialize() = "Killer" },
    VICTIM { override fun serialize() = "Victim" },
    PROJECTILE { override fun serialize() = "Projectile" },
    SHOOTER { override fun serialize() = "Shooter" },
    ALL_PLAYERS { override fun serialize() = "AllPlayers" },
    ALL_MOBS { override fun serialize() = "AllMobs" },
    ALL_ENTITIES { override fun serialize() = "AllEntities" },
    LAST_SPAWNED_ENTITY { override fun serialize() = "LastEntity" },
    SELECTED { override fun serialize() = "Selection" },
}