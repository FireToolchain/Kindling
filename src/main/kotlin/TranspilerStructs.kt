data class DFProgram(val lines: List<DFLine>) {
    override fun toString() = lines.joinToString("\n") { it.toString() }
}
data class DFLine(val header: DFHeader, val code: List<DFBlock>) {
    override fun toString(): String {
        var out = "${header.toString()}:\n"
        var indent = 0
        for (s in code) {
            if (s is DFBlock.EndIf || s is DFBlock.EndRepeat || s is DFBlock.Else) {
                indent--
                if (indent < 0) indent = 0
            }
            out += "${"  ".repeat(indent + 1)}${s.toString()}\n"
            if (s is DFBlock.IfPlayer || s is DFBlock.IfGame || s is DFBlock.IfEntity || s is DFBlock.IfVariable || s is DFBlock.Else) {
                indent++
            }
        }
        return out
    }
} // A line has a header and a list of statements
sealed interface DFHeader { // A function, process, or event
    fun technicalName(): String
    data class PlayerEvent(val type: String) : DFHeader {
        override fun technicalName() = type
        override fun toString() = "PlayerEvent[$type]"
    }
    data class EntityEvent(val type: String) : DFHeader {
        override fun technicalName() = type
        override fun toString() = "PlayerEvent[$type]"
    }
    data class Function(val name: String) : DFHeader {
        override fun technicalName() = name
        override fun toString() = "PlayerEvent[$name]"
    }
    data class Process(val name: String) : DFHeader {
        override fun technicalName() = name
        override fun toString() = "PlayerEvent[$name]"
    }
}

sealed interface DFBlock { // A regular DF block
    data class PlayerAction(val type: String, val selector: Selector, val params: List<DFValue>) : DFBlock {
        override fun toString() = "PlayerAction.$type<$selector>(${ params.joinToString( ", ") { it.toString() } })"
    }
    data class EntityAction(val type: String, val selector: Selector, val params: List<DFValue>) : DFBlock {
        override fun toString() = "EntityAction.$type<$selector>(${ params.joinToString( ", ") { it.toString() } })"
    }
    data class GameAction(val type: String, val params: List<DFValue>) : DFBlock {
        override fun toString() = "GameAction.$type(${ params.joinToString( ", ") { it.toString() } })"
    }
    data class SetVar(val type: String, val params: List<DFValue>) : DFBlock {
        override fun toString() = "SetVar.$type(${ params.joinToString( ", ") { it.toString() } })"
    }
    data class Control(val type: String, val params: List<DFValue>) : DFBlock {
        override fun toString() = "Control.$type(${ params.joinToString( ", ") { it.toString() } })"
    }
    data class SelectObject(val type: String, val subtype: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock {
        override fun toString() = "SelectObject.$type.$subtype(${ params.joinToString( ", ") { it.toString() } })"
    }
    data class CallFunction(val name: String) : DFBlock {
        override fun toString() = "CallFunc $name"
    }
    data class StartProcess(val name: String, val params: List<DFValue>) : DFBlock {
        override fun toString() = "StartProc $name(${ params.joinToString( ", ") { it.toString() } })"
    }
    data class IfPlayer(val type: String, val selector: Selector, val inverse: Boolean, val params: List<DFValue>) : DFBlock {
        override fun toString() = "IfPlayer.$type<$selector>(${ params.joinToString( ", ") { it.toString() } }) {"
    }
    data class IfVariable(val type: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock {
        override fun toString() = "IfVar.$type(${ params.joinToString( ", ") { it.toString() } }) {"
    }
    data class IfEntity(val type: String, val selector: Selector, val inverse: Boolean, val params: List<DFValue>) : DFBlock {
        override fun toString() = "IfEntity.$type<$selector>(${ params.joinToString( ", ") { it.toString() } }) {"
    }
    data class IfGame(val type: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock {
        override fun toString() = "IfGame.$type(${ params.joinToString( ", ") { it.toString() } }) {"
    }
    object Else : DFBlock {
        override fun toString() = "} else {"
    }
    object EndIf : DFBlock {
        override fun toString() = "}"
    }
    object EndRepeat : DFBlock {
        override fun toString() = "}~"
    }
    data class Repeat(val type: String, val subtype: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock {
        override fun toString() = "Repeat.$type.$subtype(${ params.joinToString( ", ") { it.toString() } }) ~{"
    }
}
enum class VariableScope {
    SAVE, GLOBAL, LOCAL
}
enum class Selector {
    DEFAULT, DAMAGER, KILLER, VICTIM, PROJECTILE, SHOOTER, ALL_PLAYERS, ALL_ENTITIES, LAST_SPAWNED_ENTITY, SELECTED,
}

sealed interface DFValue { // A DF value such as a block tag, number, sound, item, etc
    data class Text(val text: String) : DFValue {
        override fun toString() = "\"$text\""
    }
    data class Number(val num: Float): DFValue {
        override fun toString() = num.toString()
    }
    data class Location(val x: Float, val y: Float, val z: Float, val pitch: Float, val yaw: Float) : DFValue {
        override fun toString() = "Loc[$x, $y, $z, $pitch, $yaw]"
    }
    data class Vector(val x: Float, val y: Float, val z: Float) : DFValue {
        override fun toString() = "<$x, $y, $z>"
    }
    data class Sound(val type: String, val pitch: Float, val volume: Float) : DFValue {
        override fun toString() = "Snd[$type, $pitch, $volume]"
    }
    data class PotionEffect(val type: String, val duration: Int, val level: Int) : DFValue {
        override fun toString() = "Pot[$type, $duration, $level]"
    }
    data class Variable(val name: String, val scope: VariableScope) : DFValue {
        override fun toString() = "$scope[$name]"
    }
    data class GameValue(val type: String, val selector: Selector) : DFValue {
        override fun toString() = "Val[$type, $selector]"
    }
    data class Particle(val type: String) : DFValue {
        var amount = 1
        var spreadX = 0f
        var spreadY = 0f
        var motionX: Float? = null
        var motionY: Float? = null
        var motionZ: Float? = null
        var size: Float? = null
        var roll: Float? = null
        var variationSize: Int? = null
        var variationColor: Int? = null
        var variationMotion: Int? = null
        var material: String? = null
        var color: String? = null
        override fun toString(): String {
            val settings = mutableListOf("amount = $amount", "spreadX = $spreadX", "spreadY = $spreadY")
            if (motionX != null) settings.add("motionX = $motionX")
            if (motionY != null) settings.add("motionY = $motionY")
            if (motionZ != null) settings.add("motionZ = $motionZ")
            if (size != null) settings.add("size = $size")
            if (roll != null) settings.add("rol = $roll")
            if (variationSize != null) settings.add("variationSize = $variationSize")
            if (variationColor != null) settings.add("variationColor = $variationColor")
            if (variationMotion != null) settings.add("variationMotion = $variationMotion")
            if (material != null) settings.add("material = $material")
            if (color != null) settings.add("color = $color")
            return "Par[$type: ${ settings.joinToString(", ") }]"
        }
    }

    data class Tag(val name: String, val value: String) : DFValue {
        override fun toString() = "{$name = $value}"
    }
    data class Item(val data: String) : DFValue {
        override fun toString() = "{ITEM}"
    }
}