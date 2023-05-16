fun main(args: Array<String>) {
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    val str = """
        ((def 'myFunc') 
          (player-action 'GiveItems' default ((raw-item '{id:"minecraft:stone"}')))
          (player-action 'Teleport' default ((loc 25 55 25 0 0)))
          (if-player 'HasPermission' default norm ((tag 'Permissions' 'Developer')))
            (player-action 'SendMessage' all-players ((text '&l%default joined!')))
          (else)
            (player-action 'SendMessage' all-players ((text '%default joined!')))
          (end-if)
        )
    """
    println(str)
    val tokens = tokenize(str)
    //println(tokens.list.joinToString(" ") { it.token.toString() })
    val parsed = parse(tokens)
    //println(parsed.joinToString("\n") { it.toString() })
    val transpiled = transpile(parsed)
    println(transpiled.toString())
}