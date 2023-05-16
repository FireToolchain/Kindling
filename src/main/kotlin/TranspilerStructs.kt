data class DFProgram(val lines: List<DFLine>)
data class DFLine(val header: DFHeader, val code: List<DFBlock>) // A line has a header and a list of statements
sealed interface DFHeader { // A function, process, or event
    data class PlayerEvent(val type: String) : DFHeader
    data class EntityEvent(val type: String) : DFHeader
    data class Function(val name: String) : DFHeader
    data class Process(val name: String) : DFHeader
}

sealed interface DFBlock { // A regular DF block
    data class PlayerAction(val type: String, val selector: String, val params: List<DFValue>) : DFBlock
    data class EntityAction(val type: String, val selector: String, val params: List<DFValue>) : DFBlock
    data class GameAction(val type: String, val params: List<DFValue>) : DFBlock
    data class SetVar(val type: String, val params: List<DFValue>) : DFBlock
    data class Control(val type: String, val params: List<DFValue>) : DFBlock
    data class SelectObject(val type: String, val subtype: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock
    data class CallFunction(val name: String, val params: List<DFValue>) : DFBlock
    data class StartProcess(val name: String, val params: List<DFValue>) : DFBlock
    data class IfPlayer(val type: String, val selector: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock
    data class IfVariable(val type: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock
    data class IfEntity(val type: String, val selector: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock
    data class IfGame(val type: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock
    object Else : DFBlock
    object EndIf : DFBlock
    object EndRepeat : DFBlock
    data class Repeat(val type: String, val subtype: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock
}
enum class VariableScope {
    SAVE, GLOBAL, LOCAL
}
enum class Selector {
    DEFAULT, ALL_PLAYERS, ALL_ENTITIES, LAST_SPAWNED_ENTITY, SELECTED,
}

sealed interface DFValue { // A DF value such as a block tag, number, sound, item, etc
    data class Text(val text: String) : DFValue
    data class Number(val num: Float): DFValue
    data class Location(val x: Float, val y: Float, val z: Float, val pitch: Float, val yaw: Float) : DFValue
    data class Vector(val x: Float, val y: Float, val z: Float) : DFValue
    data class Sound(val type: String, val pitch: Float, val volume: Float) : DFValue
    data class PotionEffect(val type: String, val duration: Int, val leve: Int) : DFValue
    data class Variable(val name: String, val scope: VariableScope) : DFValue
    data class GameValue(val type: String, val selector: String) : DFValue
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
    }
}