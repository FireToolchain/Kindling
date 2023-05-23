import kotlinx.coroutines.runBlocking
import serializer.sendPackageRecode
import serializer.sendPackageVanilla
import transpiler.transpile
import java.io.File
import java.io.IOException

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Input kindling expression or file path.")
        return
    }
    val code = getInput(args[0])
    println("Input kindling code:")
    println(code)
    val tokens = tokenize(code)
    val parsed = parse(tokens)
    val transpiled = transpile(parsed)
    println()
    println("Pretty code:")
    println(transpiled.toString())
    println()
    sendPackageRecode(transpiled)
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