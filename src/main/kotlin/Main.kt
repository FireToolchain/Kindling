import output.logError
import output.logInfo
import serializer.sendPackageRecode
import serializer.sendPackageVanilla
import transpiler.transpile
import java.io.File
import java.io.IOException

fun main(inputs: Array<String>) {
    if (inputs.isEmpty()) {
        logInfo("No input. Run with the -help flag for help.")
        return
    }

    // Args and flags
    val input = inputs.groupBy { if (it.startsWith("-")) "flags" else "arguments" }
    val args = input["arguments"] ?: emptyList()
    val flags = input["flags"]?.filter{ it.startsWith("--") } ?: emptyList()

    val shorthandFlags = input["flags"]
        ?.filter{ it.length >= 2 && it[0] == '-' && it[1] != '-' }
        ?.map { it.replace("-", "").toCharArray().toList() }
        ?.flatten() ?: emptyList()

    // Read flags
    val help = shorthandFlags.contains('h') || flags.contains("--help")
    val recode = shorthandFlags.contains('r') || flags.contains("--recode")
    val verbose = shorthandFlags.contains('v') || flags.contains("--verbose")
    val debug = shorthandFlags.contains('d') || flags.contains("--debug")
    val group = shorthandFlags.contains('g') || flags.contains("--group")
    val small = shorthandFlags.contains('B') || flags.contains("--basic")
    val large = shorthandFlags.contains('L') || flags.contains("--large")
    val massive = shorthandFlags.contains('M') || flags.contains("--massive")
    val file = shorthandFlags.contains('f') || flags.contains("--file")
    if ((small && large) || (small && massive) || (large && massive)) {
        logError("Only one of --basic, --large, and --massive may be chosen")
        return
    }
    val plotSize = if (massive) 300 else if (large) 100 else 50
    val grouping = if (group) plotSize else 0

    // Handle code
    if (help) {
        printHelp()
        return
    } else if (args.size == 1) {
        try {
            val code = if (file) getFile(args[0]) else args[0]
            if (verbose) {
                logInfo("Input Code:")
                logInfo(code)
                logInfo()
            }
            val tokens = tokenize(code)
            val parsed = parse(tokens)
            val unoptimizedTranspiled = transpile(parsed)
            logInfo("Compiling with plot size $plotSize:")
            val transpiled = unoptimizedTranspiled.optimized(plotSize)
            if (recode) {
                sendPackageRecode(transpiled, grouping, verbose)
            } else {
                sendPackageVanilla(transpiled, grouping)
            }
        } catch (e: Exception) {
            if (debug) {
                e.printStackTrace()
            } else {
                logError(e.message)
            }
        }
    } else {
        logError("Unexpected input. Run with the -help flag for help.")
    }
}

/**
 * If input is a file location, read from it.
 * Otherwise, return input string.
 * May throw IOException.
 */
fun getFile(s: String): String {
    val file = File(s)
    return if (file.exists()) {
        if (!file.isFile) throw IOException("Specified path does not point to file.")
        if (!file.canRead()) throw IOException("Specified file is unable to be read.")
        file.readText()
    } else throw IOException("Specified file does not exist.")
}

fun printHelp() {

    println("""
    |Kindling is a program that compiles Kindling script files into DiamondFire code.
    |You can view the documentation for Kindling here: https://github.com/ashlikatt/Kindling/wiki
    |
    |Usage: java -jar Kindling.jar [FLAGS] <PROGRAM>
    |PROGRAM:
    |  Either a raw Kindling script, or if using -f, a file path pointing to a file containing plaintext Kindling code.
    |
    |FLAGS:
    |  --help -h:
    |    Shows this menu.
    |  --recode -r:
    |    Sends output to client via recode instead of printing commands.
    |  --debug -d:
    |    Errors will print their full stacktrace.
    |  --verbose -v:
    |    Additional output.
    |  --group -g:
    |    Group lines together to minimize the number of templates generated.
    |  --file -f:
    |    Treat input as file path instead of literal code.
    |  --basic -B:
    |    Compiles for a basic plot. Incompatible with --large or --massive.
    |  --large -L:
    |    Compiles for a large plot. Incompatible with --basic or --massive.
    |  --massive -M:
    |    Compiles for a massive plot. Incompatible with --basic or --large.
    """.trimMargin())
}