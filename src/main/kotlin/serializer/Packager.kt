package serializer

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import DFProgram
import kotlinx.coroutines.delay
import output.logInfo
import output.logOutput
import transpiler.codeblocks.header.DFHeader
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

fun sendPackageRecode(program: DFProgram, maxSize: Int, verbose: Boolean) {
    val client = HttpClient(Java) {
        install(WebSockets)
    }
    runBlocking {
        client.webSocket(HttpMethod.Get,"localhost",31371,"/codeutilities/item") {
            val author = "myname"
            val lines = program.lines.toMutableList() // Normal codelines that have not been smooshed yet
            val out = mutableListOf<String>() // List of smooshed codelines
            while (lines.isNotEmpty()) {
                val beginBlock = lines.removeAt(0) // Header block we're looking at
                val curLine = mutableListOf<DFSerializable>(beginBlock) // Current blocks to smoosh
                var currentSize = 2 + beginBlock.code.sumOf { it.literalSize }

                var index = 0
                while (index < lines.size) {
                    val size = 2 + lines[index].code.sumOf { it.literalSize }
                    if (currentSize + size < maxSize) {
                        currentSize += size
                        curLine.add(lines.removeAt(index))
                        index--
                    } else break
                    index++
                }

                val blocks = """{"blocks":[${curLine.joinToString(",") { it.serialize() }}]}"""
                val compressed = encode(blocks)
                val templateData =
                    """{"author":${author.serialize()},"name":"template","version":1,"code":${
                        compressed.serialize()
                    }}"""
                val itemName = if (curLine.size == 1) beginBlock.getItemName() else DFHeader.bundledItemsName
                val blockType = if (curLine.size == 1) beginBlock.getItemType() else DFHeader.bundledItemsType
                val itemTag =
                    """{display:{Name:${itemName.serialize()}},PublicBukkitValues:{"hypercube:codetemplatedata":${
                        templateData.serialize()
                    }}}"""
                val itemData = """{"id":"minecraft:${blockType}","Count":1,"tag":$itemTag}"""
                val packet = """{"source":"Kindling","type":"nbt","data":${itemData.serialize()}}"""
                out.add(packet)
            }
            val totalLines = out.size
            var currentLine = 0
            for (packet in out) {
                currentLine++

                send(Frame.Text(packet))
                delay(100)
                logInfo("Sending $currentLine of $totalLines...")
                if (verbose) logInfo(packet)
            }
            logInfo("Finished.")
        }
    }
    client.close()
}

fun sendPackageVanilla(program: DFProgram, maxSize: Int) {
    val author = "myname"
    val lines = program.lines.toMutableList() // Normal codelines that have not been smooshed yet
    val out = mutableListOf<String>() // List of smooshed codelines
    while (lines.isNotEmpty()) {
        val beginBlock = lines.removeAt(0) // Header block we're looking at
        val curLine = mutableListOf<DFSerializable>(beginBlock) // Current blocks to smoosh
        var currentSize = 2 + beginBlock.code.sumOf { it.literalSize }

        var index = 0
        while (index < lines.size) {
            val size = 2 + lines[index].code.sumOf { it.literalSize }
            if (currentSize + size < maxSize) {
                currentSize += size
                curLine.add(lines.removeAt(index))
                index--
            } else break
            index++
        }

        val blocks = """{"blocks":[${curLine.joinToString(",") { it.serialize() }}]}"""
        val compressed = encode(blocks)
        val templateData =
            """{"author":${author.serialize()},"name":"template","version":1,"code":${
                compressed.serialize()
            }}"""
        val itemName = if (curLine.size == 1) beginBlock.getItemName() else DFHeader.bundledItemsName
        val blockType = if (curLine.size == 1) beginBlock.getItemType() else DFHeader.bundledItemsType
        val itemTag =
            """{display:{Name:${itemName.serialize()}},PublicBukkitValues:{"hypercube:codetemplatedata":${
                templateData.serialize()
            }}}"""
        val command = """/give @p minecraft:${blockType}$itemTag"""
        out.add(command)
    }
    for (command in out) {
        logOutput(command)
    }
    logInfo("Finished.")
}

