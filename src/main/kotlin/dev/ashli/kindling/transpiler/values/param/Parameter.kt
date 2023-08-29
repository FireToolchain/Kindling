package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.transpiler.values.param.DFType
import kotlin.String

/**
 * Represents a parameter of a function.
 * @param name Name of the parameter
 * @param type The type the parameter accepts
 * @param plural Put true if it supports multiple values
 * @param default Default value of the parameter if optional
 * @param description Description of the parameter
 * @param note Note of the parameter
 */
class Parameter(
    val name: String,
    val type: DFType,
    val plural: Boolean = false,
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
        "plural": $plural
        ${if(default != null) ""","optional":"true","default": true,"default_value":"${default.serialize()}"""" else ""","optional":"false""""}
        ${if(description != null) ""","description":"${description.serialize()}"""" else ""}
        ${if(note!= null) ""","note":"${note.serialize()}"""" else ""}
    }
}
    """.trimIndent().replace('\n', ' ')
}

