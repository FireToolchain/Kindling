package transpiler

import serializer.DFSerializable
import serializer.serializeString

/**
 * Represents a DF Program, which is a list of DFLines.
 */
data class DFProgram(val lines: List<DFLine>) {
    override fun toString() = lines.joinToString("\n") { it.toString() }
}

/**
 * Represents a DFLine, which is a header and a list of codeblocks.
 */
data class DFLine(val header: DFHeader, val code: List<DFBlock>) : DFSerializable {
    override fun serialize() = """{"blocks":[${header.serialize()},${code.joinToString(",") { it.serialize() }}]}"""

    // A line has a header and a list of statements
    override fun toString(): String {
        var out = "$header:\n"
        var indent = 0
        for (s in code) {
            if (s is DFBlock.EndIf || s is DFBlock.EndRepeat || s is DFBlock.Else) {
                indent--
                if (indent < 0) indent = 0
            }
            out += "${"  ".repeat(indent + 1)}$s\n"
            if (s is DFBlock.IfPlayer || s is DFBlock.IfGame || s is DFBlock.IfEntity || s is DFBlock.IfVariable || s is DFBlock.Else) {
                indent++
            }
        }
        return out
    }
}

/**
 * Represents a DF Header, which is an Event, Process, or Function.
 */
sealed interface DFHeader : DFSerializable {
    fun technicalName(): String
    data class PlayerEvent(val type: String) : DFHeader {
        override fun serialize() = "{" +
                """"id":"block",""" +
                """"block":"event",""" +
                """"args":{"items":[]},""" +
                """"action":${serializeString(type)}}"""
        override fun technicalName() = type
        override fun toString() = "PlayerEvent[$type]"
    }
    data class EntityEvent(val type: String) : DFHeader {
        override fun serialize() = """{"id":"block",""" +
                """"block":"entity_event",""" +
                """"args":{"items":[]},""" +
                """"action":${serializeString(type)}}"""
        override fun technicalName() = type
        override fun toString() = "PlayerEvent[$type]"
    }
    data class Function(val name: String) : DFHeader {
        override fun serialize() = "{" +
                """"id":"block",""" +
                """"block":"func",""" +
                """"args":{"items":[{"item":{"id":"bl_tag","data":{"option":"False","tag":"Is Hidden","action":"dynamic","block":"func"}},"slot":26}]},""" +
                """"data":${serializeString(name)}}"""
        override fun technicalName() = name
        override fun toString() = "PlayerEvent[$name]"
    }
    data class Process(val name: String) : DFHeader {
        override fun serialize() = "{" +
                """"id":"block",""" +
                """"block":"process",""" +
                """"args":{},""" +
                """"data":${serializeString(name)}}"""
        override fun technicalName() = name
        override fun toString() = "PlayerEvent[$name]"
    }
}

