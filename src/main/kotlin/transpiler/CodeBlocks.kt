package transpiler

import serializer.DFSerializable
import serializer.serializeArgs
import serializer.serializeString

/**
 * Represents a DF Block, such as a Player Action, Set Variable, or If Game.
 */
sealed interface DFBlock : DFSerializable { // A regular DF block
    data class PlayerAction(val type: String, val selector: Selector, val params: List<DFValue>) : DFBlock {
        override fun serialize() = "{" +
                """"id":"block",""" +
                """"block":"player_action",""" +
                """"args":${serializeArgs(params)},""" +
                """"target":"${selector.serialize()}",""" +
                """"action":${serializeString(type)}""" +
                "}"
        override fun toString() = "PlayerAction.$type<$selector>(${ params.joinToString( ", ") { it.toString() } })"
    }
    data class EntityAction(val type: String, val selector: Selector, val params: List<DFValue>) : DFBlock {
        override fun serialize() = "{" +
                """"id":"block",""" +
                """"block":"entity_action",""" +
                """"args":${serializeArgs(params)},""" +
                """"target":"${selector.serialize()}",""" +
                """"action":${serializeString(type)}""" +
                "}"
        override fun toString() = "EntityAction.$type<$selector>(${ params.joinToString( ", ") { it.toString() } })"
    }
    data class GameAction(val type: String, val params: List<DFValue>) : DFBlock {
        override fun serialize() =  "{" +
                """"id":"block",""" +
                """"block":"game_action",""" +
                """"args":${serializeArgs(params)},""" +
                """"action":${serializeString(type)}""" +
                "}"
        override fun toString() = "GameAction.$type(${ params.joinToString( ", ") { it.toString() } })"
    }
    data class SetVar(val type: String, val params: List<DFValue>) : DFBlock {
        override fun serialize() = "{" +
                """"id":"block",""" +
                """"block":"set_var",""" +
                """"args":${serializeArgs(params)},""" +
                """"action":${serializeString(type)}""" +
                "}"
        override fun toString() = "SetVar.$type(${ params.joinToString( ", ") { it.toString() } })"
    }
    data class Control(val type: String, val params: List<DFValue>) : DFBlock {
        override fun serialize() = "{" +
                """"id":"block",""" +
                """"block":"control",""" +
                """"args":${serializeArgs(params)},""" +
                """"action":${serializeString(type)}""" +
                "}"
        override fun toString() = "Control.$type(${ params.joinToString( ", ") { it.toString() } })"
    }
    data class SelectObject(val type: String, val subtype: String, val inverse: Boolean, val params: List<DFValue>) :
        DFBlock {
        override fun serialize() = "{" +
                """"id":"block",""" +
                """"block":"select_obj",""" +
                """"args"":${serializeArgs(params)},""" +
                if (subtype.isEmpty()) { "" } else { """"subAction":${serializeString(type)},""" } +
                if (inverse) { """"inverted":"NOT",""" } else { "" } +
                """"action":${serializeString(type)}""" +
                """},{"id":"bracket","direct":"open","type":"norm"}"""
        override fun toString() = "SelectObject.$type.$subtype(${ params.joinToString( ", ") { it.toString() } })"
    }
    data class CallFunction(val name: String) : DFBlock {
        override fun serialize() =  "{" +
                """"id":"block",""" +
                """"block":"call_func",""" +
                """"args":{"items":[]},""" +
                """"data":${serializeString(name)}""" +
                "}"
        override fun toString() = "CallFunc $name"
    }
    data class StartProcess(val name: String, val params: List<DFValue>) : DFBlock {
        override fun serialize() = "{" +
                """"id":"block",""" +
                """"block":"start_process",""" +
                """"args":${serializeArgs(params)},""" +
                """"data":${serializeString(name)}""" +
                "}"
        override fun toString() = "StartProc $name(${ params.joinToString( ", ") { it.toString() } })"
    }
    data class IfPlayer(val type: String, val selector: Selector, val inverse: Boolean, val params: List<DFValue>) :
        DFBlock {
        override fun serialize() = "{" +
                """"id":"block",""" +
                """"block":"if_player",""" +
                """"args":${serializeArgs(params)},""" +
                """"action":${serializeString(type)},""" +
                if (inverse) { """"inverted":"NOT",""" } else { "" } +
                """"target":"${selector.serialize()}",""" +
                """},{"id":"bracket","direct":"open","type":"norm"}"""
        override fun toString() = "IfPlayer.$type<$selector>(${ params.joinToString( ", ") { it.toString() } }) {"
    }
    data class IfVariable(val type: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock {
        override fun serialize() = "{" +
                """"id":"block",""" +
                """"block":"if_var",""" +
                """"args":${serializeArgs(params)},""" +
                if (inverse) { """"inverted":"NOT",""" } else { "" } +
                """"action":${serializeString(type)}""" +
                """},{"id":"bracket","direct":"open","type":"norm"}"""
        override fun toString() = "IfVar.$type(${ params.joinToString( ", ") { it.toString() } }) {"
    }
    data class IfEntity(val type: String, val selector: Selector, val inverse: Boolean, val params: List<DFValue>) :
        DFBlock {
        override fun serialize() = "{" +
                """"id":"block",""" +
                """"block":"if_entity",""" +
                """"args":${serializeArgs(params)},""" +
                """"action":${serializeString(type)},""" +
                if (inverse) { """"inverted":"NOT",""" } else { "" } +
                """"target":"${selector.serialize()}",""" +
                """},{"id":"bracket","direct":"open","type":"norm"}"""
        override fun toString() = "IfEntity.$type<$selector>(${ params.joinToString( ", ") { it.toString() } }) {"
    }
    data class IfGame(val type: String, val inverse: Boolean, val params: List<DFValue>) : DFBlock {
        override fun serialize() = "{" +
                """"id":"block",""" +
                """"block":"if_game",""" +
                """"args":${serializeArgs(params)},""" +
                if (inverse) { """"inverted":"NOT",""" } else { "" } +
                """"action":${serializeString(type)}""" +
                """},{"id":"bracket","direct":"open","type":"norm"}"""
        override fun toString() = "IfGame.$type(${ params.joinToString( ", ") { it.toString() } }) {"
    }
    object Else : DFBlock {
        override fun serialize() =
                """{"id":"bracket","direct":"open","type":"norm"},""" +
                """{"id":"block","block":"else"},""" +
                """{"id":"bracket","direct":"close","type":"norm"}"""
        override fun toString() = "} else {"
    }
    object EndIf : DFBlock {
        override fun serialize() = """{"id":"bracket","direct":"close","type":"norm"}"""
        override fun toString() = "}"
    }
    object EndRepeat : DFBlock {
        override fun serialize() = """{"id":"bracket","direct":"close","type":"repeat"}"""
        override fun toString() = "}~"
    }
    data class Repeat(val type: String, val subtype: String, val inverse: Boolean, val params: List<DFValue>) :
        DFBlock {
        override fun serialize() = "{" +
                """"id":"block",""" +
                """"block":"repeat",""" +
                """"args":${serializeArgs(params)},""" +
                if (subtype.isEmpty()) { "" } else { """"subAction":${serializeString(type)},""" } +
                if (inverse) { """"inverted":"NOT",""" } else { "" } +
                """"action":${serializeString(type)}""" +
                """},{"id":"bracket","direct":"open","type":"repeat"}"""
        override fun toString() = "Repeat.$type.$subtype(${ params.joinToString( ", ") { it.toString() } }) ~{"
    }
}