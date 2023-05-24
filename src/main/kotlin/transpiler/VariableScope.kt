package transpiler

import serializer.DFSerializable

/**
 * Represents a Variable's scope
 */
enum class VariableScope : DFSerializable {
    SAVE {
        override fun serialize() = """saved"""
    },
    GLOBAL {
        override fun serialize() = """game"""
    },
    LOCAL {
        override fun serialize() = """local"""
    }
}

