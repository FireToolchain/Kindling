package serializer

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.internal.ignoreIoExceptions
import transpiler.DFProgram
import transpiler.DFValue
import utils.encode
import java.util.concurrent.TimeUnit


interface DFSerializable {
    fun serialize(): String
}

fun serializeString(s: String): String {
    return '"' + toInner(s) + '"'
}

fun toInner(s: String): String {
    return s.replace("\\", "\\\\").replace("\"", "\\\"")
}

fun serializeArgs(args: List<DFValue>): String {
    val out = mutableListOf<String>()
    var mainIndex = 0
    var endIndex = 26
    for (arg in args) {
        if (endIndex <= mainIndex) {
            throw SerializationError("Total chest parameters exceed 27.", arg)
        }
        if (arg is DFValue.Tag) {
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
    val client = HttpClient(OkHttp) {
        install(WebSockets)
        engine {
            preconfigured = OkHttpClient.Builder()
                .pingInterval(20, TimeUnit.SECONDS)
                .build()
        }
    }
    runBlocking {
        client.webSocket(method = HttpMethod.Get, host = "localhost", port = 31371, path = "/codeutilities/item") {
            val author = "myname"
            for (line in program.lines) {
                val compressed = encode(line.serialize())
                val templateName = "Name" // Name with color codes; change later
                val templateData = """{"author":"${toInner(author)}","name":"${toInner(templateName)}","version":1,"code":"${toInner(compressed)}"}"""
                val itemName = "hello" // JSON tellraw name; changelater
                val itemTag = """{display:{Name:"${toInner(itemName)}"},PublicBukkitValues:{"hypercube:codetemplatedata":"${toInner(templateData)}"}}"""
                val itemData = """{"id":"minecraft:ender_chest","Count":1,"tag":$itemTag}"""
                val packet = """{"source":"Kindling","type":"nbt","data":"${toInner(itemData)}"}"""

                send(Frame.Text(packet))
            }
            println("Sent successfully.")
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

