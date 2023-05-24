package serializer

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import DFProgram
import transpiler.values.DFValue
import transpiler.values.Tag
import utils.encode


interface DFSerializable {
    fun serialize(): String
}

fun serializeString(s: String?): String {
    return '"' + toInner(s) + '"'
}

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

fun sendPackageRecode(program: DFProgram) {
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
                val templateName = """§6🔥 §e$currentLine"""
                val templateData =
                    """{"author":"${toInner(author)}","name":"${toInner(templateName)}","version":1,"code":"${
                        toInner(compressed)
                    }"}"""
                val itemName = """'{"extra":[""" +
                        """{"bold":true,"italic":false,"underlined":false,"strikethrough":false,"obfuscated":false,"color":"gold","text":"🔥 "},""" +
                        """{"bold":false,"italic":false,"color":"yellow","text":"$currentLine"}""" +
                        """],"text":""}'"""
                val itemTag =
                    """{display:{Name:"${toInner(itemName)}"},PublicBukkitValues:{"hypercube:codetemplatedata":"${
                        toInner(templateData)
                    }"}}"""
                val itemData = """{"id":"minecraft:ender_chest","Count":1,"tag":$itemTag}"""
                val packet = """{"source":"Kindling","type":"nbt","data":"${toInner(itemData)}"}"""

                send(Frame.Text(packet))
                println("Sent $currentLine of $totalLines")
                println(uncompressed)
            }
            println("Finished.")
        }
    }
    client.close()
}

fun sendPackageVanilla(program: DFProgram) {
    val author = "myname"
    for (line in program.lines) {
        val compressed = encode(line.serialize())
        val templateName = "Name" // Name with color codes
        val templateData = """{"author":"${toInner(author)}","name":"${toInner(templateName)}","version":1,"code":"${toInner(compressed)}"}"""
        val itemName = "hello" // JSON tellraw name
        val itemTag = """{display:{Name:"${toInner(itemName)}"},PublicBukkitValues:{"hypercube:codetemplatedata":"${toInner(templateData)}"}}"""

        // Minecraft
        println("""/give @p minecraft:ender_chest$itemTag""")
        println()
    }
}

