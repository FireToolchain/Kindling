package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.transpiler.values.param.DFType

class Parameter(
    val name: String,
    val type: DFType,
    val plural: Boolean = false,
    val optional: Boolean = false,
    val default: DFValue? = null,
    val description: String? = null,
    val note: String? = null,

    ) : DFValue {
    override fun serialize() = """
{
    "id": "pn_el",
    "data": {
        "name": ${name.serialize()},
        "type": ${type.internalName.serialize()},
        "plural": $plural,
        "optional": $optional
        ${if(default != null) ""","default": true,"default_value":"${default.serialize()}"""" else ""","default":"false""""}
        ${if(description != null) ""","description":"${description.serialize()}"""" else ""}
        ${if(note!= null) ""","note":"${note.serialize()}"""" else ""}
    }
}
    """.trimIndent().replace('\n', ' ')
}

