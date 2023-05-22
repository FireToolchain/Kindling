import serializer.sendPackage
import transpiler.transpile

fun main(args: Array<String>) {
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    val str = """
((def "factorial")  
  (if-var "<" norm ((param 0) (num 2))) 
    (return (num 1)) 
  (else)   
    (set-var "-" ((var "dif") (param 0) (num 1))) 
    (call "factorial" ((var "dif")))
    (set-var "=" ((var "first") (ret))) 
    (set-var "-" ((var "dif") (param 0) (num 2))) 
    (call "factorial" ((var "dif")))
    (set-var "=" ((var "second") (ret))) 
    (set-var "+" ((var "sum") (var "first") (var "second")))
    (return (var "sum"))
  (end-if)) 
    """
    println(str)
    val tokens = tokenize(str)
    //println(tokens.list.joinToString(" ") { it.token.toString() })
    val parsed = parse(tokens)
    //println(parsed.joinToString("\n") { it.toString() })
    val transpiled = transpile(parsed)
    println(transpiled.toString())
    sendPackage(transpiled)
}