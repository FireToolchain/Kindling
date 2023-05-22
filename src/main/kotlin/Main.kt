import serializer.sendPackage
import serializer.toInner
import transpiler.transpile
import java.io.File
import java.io.IOException

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Input kindling expression or file path.")
        return
    }
    val code = getInput(args[0])
    println(code)
    val tokens = tokenize(code)
    //println(tokens.list.joinToString(" ") { it.token.toString() })
    val parsed = parse(tokens)
    //println(parsed.joinToString("\n") { it.toString() })
    val transpiled = transpile(parsed)
    println(transpiled.toString())
    sendPackage(transpiled)
}

/**
 * If input is a file location, read from it.
 * Otherwise, return input string.
 * May throw IOException.
 */
fun getInput(s: String): String {
    val file = File(s)
    return if (file.exists()) {
        if (!file.isFile) throw IOException("Specified path does not point to file.")
        if (!file.canRead()) throw IOException("Specified file is unable to be read.")
        file.readText()
    } else {
        s
    }
}