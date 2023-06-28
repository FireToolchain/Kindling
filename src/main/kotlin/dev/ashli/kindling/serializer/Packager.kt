package dev.ashli.kindling.serializer

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import dev.ashli.kindling.DFProgram
import kotlinx.coroutines.delay
import dev.ashli.kindling.output.logInfo
import dev.ashli.kindling.output.logOutput
import dev.ashli.kindling.transpiler.codeblocks.header.DFHeader
import dev.ashli.kindling.transpiler.values.DFValue
import dev.ashli.kindling.transpiler.values.Tag
import dev.ashli.kindling.utils.encode


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
        client.webSocket(HttpMethod.Get, "localhost", 31371, "/codeutilities/item") {
            val out = lineIterator(program, maxSize)
                .asSequence()
                .map { (blockType, itemTag) ->
                    val itemData = """{"id":"minecraft:${blockType}","Count":1,"tag":$itemTag}"""
                    val packet = """{"source":"Kindling","type":"nbt","data":${itemData.serialize()}}"""

                    packet
                }
                .toList()

            val totalLines = out.count()

            out.forEachIndexed { currentLine, packet ->
                send(Frame.Text(packet))
                delay(100)
                logInfo("Sending ${currentLine + 1} of $totalLines...")
                if (verbose) logInfo(packet)
            }

            logInfo("Finished.")
        }
    }
    client.close()
}

fun sendPackageVanilla(program: DFProgram, maxSize: Int) {
    val output = lineIterator(program, maxSize)
        .asSequence()
        .map { (blockType, itemTag) -> "/give @p minecraft:${blockType}$itemTag" }

    for (command in output) {
        logOutput(command)
    }

    logInfo("Finished.")
}

fun sendPackageCodeClient(program: DFProgram, maxSize: Int, verbose: Boolean) {
    val output = lineIterator(program, maxSize)
        .asSequence()
        .map { it.templateData }
        .toList()

    val totalLines = output.count()

    val client = HttpClient(Java) {
        install(WebSockets)
    }

    runBlocking {
        client.webSocket(HttpMethod.Get, "localhost", 31375, "/") {
            logInfo("Please authenticate using `/auth` ingame.")

            incoming.receive() // waiting for auth

            logInfo("Authenticated.")

            send("clear")
            if (verbose) logInfo("Cleared plot.")
            incoming.receive()
            incoming.receive()
            send("spawn")
            if (verbose) logInfo("Teleporting to dev spawn.")
            incoming.receive()
            incoming.receive()
            send("place")
            if (verbose) logInfo("Placing started.")
            incoming.receive()

            output.forEachIndexed { currentLine, templateData ->
                send("place $templateData")
                if (verbose) {
                    logInfo("Sending ${currentLine + 1} of $totalLines (Data: $templateData)")
                } else {
                    logInfo("Sending ${currentLine + 1} of $totalLines")
                }
                incoming.receive()
            }

            send("place")
            if (verbose) logInfo("Finished placing.")
            incoming.receive()
        }
    }

    logInfo("Finished.")
}

fun lineIterator(program: DFProgram, maxSize: Int) = iterator {
    val author = "myname"
    val lines = program.lines.toMutableList() // Normal codelines that have not been smooshed yet
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
        yield(Line(blockType, itemTag, compressed))
    }
}

data class Line(val blockType: String, val itemTag: String, val templateData: String)