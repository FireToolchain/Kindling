package dev.ashli.kindling.output

enum class Color(private val s: String) {
    RESET("\u001b[0m"),
    RED("\u001b[91m"),
    BLUE("\u001b[94m"),
    GREEN("\u001b[92m"),
    YELLOW("\u001b[93m"),
    WHITE("\u001B[97m");

    override fun toString() = s
}

/**
 * Log an output in console.
 */
fun logOutput(vararg s: Any?) {
    println(Color.GREEN.toString() + s.joinToString("") + Color.RESET)
}

/**
 * Logs information in console.
 */
fun logInfo(vararg s: Any?) {
    println(Color.WHITE.toString() + s.joinToString("") + Color.RESET)
}

/**
 * Logs a warning in console.
 */
fun logWarning(vararg s: Any?) {
    println(Color.YELLOW.toString() + s.joinToString("") + Color.RESET)
}

/**
 * Logs a fatal error that causes the program to stop and compilation to fail.
 */
fun logError(vararg s: Any?) {
    println(Color.RED.toString() + s.joinToString("") + Color.RESET)
}