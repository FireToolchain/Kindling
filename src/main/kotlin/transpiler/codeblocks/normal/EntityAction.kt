package transpiler.codeblocks.normal

import serializer.serializeArgs
import serializer.serializeString
import transpiler.Selector
import transpiler.values.DFValue

data class EntityAction(val type: String, val selector: Selector, val params: List<DFValue>) : DFBlock {
    override val technicalName: String
        get() = "entity_action"
    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"entity_action",""" +
            """"args":${serializeArgs(params)},""" +
            """"target":"${selector.serialize()}",""" +
            """"action":${serializeString(type)}""" +
            "}"
    override fun toString() = "EntityAction.$type<$selector>(${ params.joinToString( ", ") { it.toString() } })"
}