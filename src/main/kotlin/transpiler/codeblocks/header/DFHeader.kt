package transpiler.codeblocks.header

import serializer.DFSerializable
import serializer.serialize
import transpiler.codeblocks.CodeHolder
import transpiler.codeblocks.normal.*

/**
 * Represents a DF Header, which is an Event, Process, or Function.
 */
sealed class DFHeader(val code: List<DFBlock>) : DFSerializable {
    companion object {
        const val bundledItemsName = """{"extra":[""" +
                """{"italic":false,"color":"#DD9955","text":"Crate"},""" +
                """{"italic":false,"bold":true,"color":"#666666","text":" » | "},""" +
                """{"italic":false,"color":"#FFCC88","text":"???"}""" +
                """],"text":""}"""
        const val bundledItemsType = "barrel"
    }
    abstract fun technicalName(): String
    abstract fun getItemName(): String
    abstract fun getItemType(): String

    abstract fun cloneWith(code: List<DFBlock>): DFHeader
}