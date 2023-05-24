package transpiler.codeblocks.header

import serializer.DFSerializable

/**
 * Represents a DF Header, which is an Event, Process, or Function.
 */
sealed interface DFHeader : DFSerializable {
    fun technicalName(): String
}