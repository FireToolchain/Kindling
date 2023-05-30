package dev.ashli.kindling.transpiler

import dev.ashli.kindling.serializer.DFSerializable

/**
 * Represents a Variable's scope
 */
enum class VariableScope : DFSerializable {
    SAVE {
        override fun serialize() = """saved"""
    },
    GLOBAL {
        override fun serialize() = """unsaved"""
    },
    LOCAL {
        override fun serialize() = """local"""
    }
}

