package transpiler.codeblocks.header

import serializer.DFSerializable
import transpiler.codeblocks.normal.*

/**
 * Represents a DF Header, which is an Event, Process, or Function.
 */
sealed class DFHeader(val code: List<DFBlock>) : DFSerializable {
    abstract fun technicalName(): String
    abstract fun getItemName(): String
    abstract fun getItemType(): String

    fun serializeLine(s: String): String {
        return if (code.isEmpty()) {
            """{"blocks":[$s]}"""
        } else {
            """{"blocks":[$s,${code.joinToString(",") { it.serialize() }}]}"""
        }
    }
}