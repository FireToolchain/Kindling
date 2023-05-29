package serializer

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import DFProgram
import kotlinx.coroutines.delay
import transpiler.values.DFValue
import transpiler.values.Tag
import utils.encode


interface DFSerializable {
    fun serialize(): String
}

fun String.serialize() = """"${toInner(this)}""""

fun toInner(s: String?): String {
    return s?.replace("\\", "\\\\")?.replace("\"", "\\\"") ?: "null"
}

fun serializeArgs(args: List<DFValue>): String {
    val out = mutableListOf<String>()
    var mainIndex = 0
    var endIndex = 26
    for (arg in args) {
        if (endIndex <= mainIndex) {
            throw SerializationError("Total chest parameters exceed 27.", arg)
        }
        if (arg is Tag) {
            out.add("""{"item":${arg.serialize()},"slot":$endIndex}""")
            endIndex--
        } else {
            out.add("""{"item":${arg.serialize()},"slot":$mainIndex}""")
            mainIndex++
        }
    }
    return """{"items":[${out.joinToString(",")}]}"""
}

fun sendPackageRecode(program: DFProgram, verbose: Boolean) {
    val client = HttpClient(Java) {
        install(WebSockets)
    }
    runBlocking {
        client.webSocket(HttpMethod.Get,"localhost",31371,"/codeutilities/item") {
            val author = "myname"
            val totalLines = program.lines.size
            var currentLine = 0
            for (line in program.lines) {
                currentLine++
                val uncompressed = line.serialize();
                val compressed = encode(uncompressed)
                val templateData =
                    """{"author":${author.serialize()},"name":"template","version":1,"code":${
                        compressed.serialize()
                    }}"""
                val itemName = line.getItemName()
                val itemTag =
                    """{display:{Name:${itemName.serialize()}},PublicBukkitValues:{"hypercube:codetemplatedata":${
                        templateData.serialize()
                    }}}"""
                val itemData = """{"id":"minecraft:${line.getItemType()}","Count":1,"tag":$itemTag}"""
                val packet = """{"source":"Kindling","type":"nbt","data":${itemData.serialize()}}"""

                send(Frame.Text(packet))
                delay(100)
                println("Sending $currentLine of $totalLines...")
                if (verbose) println(uncompressed)
                if (verbose) println(packet)
            }
            println("Finished.")
        }
    }
    client.close()
}

fun sendPackageVanilla(program: DFProgram) {
    val author = "myname"
    for (line in program.lines) {
        val uncompressed = line.serialize()
        val compressed = encode(uncompressed)
        val templateData =
            """{"author":${author.serialize()},"name":"template","version":1,"code":${
                compressed.serialize()
            }}"""
        val itemName = line.getItemName()
        val itemTag =
            """{display:{Name:${itemName.serialize()}},PublicBukkitValues:{"hypercube:codetemplatedata":${
                templateData.serialize()
            }}}"""

        // Minecraft
        println("""/give @p minecraft:${line.getItemType()}$itemTag""")
        println()
    }
}

