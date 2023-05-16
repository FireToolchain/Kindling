sealed interface Value {
    data class VList(val inner: List<Value>) : Value
    data class Ident(val str: String) : Value
    data class Str(val str: String) : Value
    data class Num(val num: Float) : Value
}

fun parse(tokens: Tokens): List<Value> {
    val values = ArrayList<Value>()
    while (tokens.length() > 0) {
        values.add(parseValue(tokens))
    }
    return values
}
fun parseValue(tokens: Tokens): Value {
    if (tokens.length() == 0) throw MissingToken("Expected value.", tokens)
    val first = tokens.peek()
    return when (first.token) {
        is TokenType.OpenParen -> parseList(tokens)
        is TokenType.CloseParen -> throw UnexpectedToken("Expected number, string, identifier, or list.", first)
        is TokenType.Num -> {
            tokens.next()
            Value.Num(first.token.num)
        }
        is TokenType.Str -> {
            tokens.next()
            Value.Str(first.token.str)
        }
        is TokenType.Ident -> {
            tokens.next()
            Value.Ident(first.token.str)
        }
    }
}
fun parseList(tokens: Tokens): Value.VList {
    if (tokens.length() == 0) throw MissingToken("Expected beginning of list, '('.", tokens)
    tokens.next() // Skip (
    val out = ArrayList<Value>()
    while (tokens.length() > 0) {
        val next = tokens.peek()
        if (next.token is TokenType.CloseParen) {
            tokens.next() // Skip )
            return Value.VList(out)
        } else {
            out.add(parseValue(tokens)) // Add next value
        }
    }
    throw MissingToken("Expected another value or end of list, ')'.", tokens)
}
