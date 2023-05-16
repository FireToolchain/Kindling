fun main(args: Array<String>) {
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.

    println(parse(tokenize("(word 'string' 2 1.5 .2 (b)) ; Comment~!")).joinToString(" "))
}