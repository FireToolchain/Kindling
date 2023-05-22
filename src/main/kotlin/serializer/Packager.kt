package serializer

import transpiler.DFProgram
import transpiler.DFValue
import utils.encode

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
    val author = "myname"
    for (line in program.lines) {
        val compressed = encode(line.serialize())
        val templateName = "Name" // Name with color codes
        val templateData = """{"author":"${toInner(author)}","name":"${toInner(templateName)}","version":1,"code":"${toInner(compressed)}"}"""
        val itemName = "hello" // JSON tellraw name
        val itemTag = """{display:{Name:"${toInner(itemName)}"},PublicBukkitValues:{"hypercube:codetemplatedata":"${toInner(templateData)}"}}"""
        val itemData = """{"id":"minecraft:ender_chest","Count":1,"tag":$itemTag}"""

        println("""{"source":"Kindling","type":"nbt","data":"${toInner(itemData)}"}""")
        println()
    }
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
        println("""/give @s minecraft:ender_chest 1 $itemTag""")
        println()
    }
}

