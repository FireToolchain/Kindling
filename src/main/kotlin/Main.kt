import serializer.sendPackageRecode
import serializer.sendPackageVanilla
import transpiler.transpile
import java.io.File
import java.io.IOException

fun main(inputs: Array<String>) {
    if (inputs.isEmpty()) {
        println("No input. Run with the -help flag for help.")
        return
    }

    // Args and flags
    val input = inputs.groupBy { if (it.startsWith("-")) "flags" else "arguments" }
    val args = input["arguments"] ?: emptyList()
    val flags = input["flags"] ?: emptyList()

    // Read flags
    val help = flags.contains("-h") || flags.contains("-help")
    val recode = flags.contains("-r") || flags.contains("-recode")
    val verbose = flags.contains("-v") || flags.contains("-verbose")
    val debug = flags.contains("-d") || flags.contains("-debug")

    // Handle code
    if (help) {
        printHelp()
        return
    } else if (args.size == 1) {
        try {
            val code = getInput(args[0])
            if (verbose) {
                println("Input Code:")
                println(code)
                println()
            }
            val tokens = tokenize(code)
            val parsed = parse(tokens)
            val transpiled = transpile(parsed)
            if (verbose) {
                println("Formatted Blockcode:")
                println(transpiled.toString())
                println()
            }
            if (recode) {
                sendPackageRecode(transpiled, verbose)
            } else {
                sendPackageVanilla(transpiled)
            }
        } catch (e: Exception) {
            if (debug) {
                e.printStackTrace()
            } else {
                println(e.message)
            }
        }
    } else {
        println("Unexpected input. Run with the -help flag for help.")
    }
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

fun printHelp() {
    println("Kindling is a program that compiles Kindling script files into DiamondFire code.")
    println("You can view the documentation for Kindling here: https://github.com/ashlikatt/Kindling/wiki")
    println()
    println("Usage: java -jar Kindling.jar [FLAGS] <PROGRAM>")
    println("PROGRAM:")
    println("  Either a raw Kindling script, or a file path pointing to a plaintext containing one.")
    println()
    println("FLAGS:")
    println("  -help:")
    println("    Shows this menu.")
    println("  -recode:")
    println("    Sends output to client via recode instead of printing commands.")
}