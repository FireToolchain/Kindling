package dev.ashli.kindling

/**
 * Represents a Kindling parsed value, with its position in the source code.
 */
data class Value(val type: ValueType, val line: Int, val column: Int) {
    override fun toString() = this.type.toString()
}

/**
 * Represents a type of Kindling parser value.
 */
sealed interface ValueType {
    val name: String

    /**
     * Represents a list value.
     */
    data class VList(val inner: List<Value>) : ValueType {
        override val name = "List"
        override fun toString() = "(${ this.inner.joinToString(" ") { it.toString() } })"

    }

    /**
     * Represents an identifier value (no quotes around it)
     */
    data class Ident(val str: String) : ValueType {
        override val name = "Identifier"
        override fun toString() = this.str
    }

    /**
     * Represents a string value.
     */
    data class Str(val str: String) : ValueType {
        override val name = "String"
        override fun toString() = "'${this.str}'"
    }

    /**
     * Represents a number value.
     */
    data class Num(val num: Float) : ValueType {
        override val name = "Number"
        override fun toString() = num.toString()
    }
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
            Value(ValueType.Num(first.token.num), first.line, first.col)
        }
        is TokenType.Str -> {
            tokens.next()
            Value(ValueType.Str(first.token.str), first.line, first.col)
        }
        is TokenType.Ident -> {
            tokens.next()
            Value(ValueType.Ident(first.token.str), first.line, first.col)
        }
    }
}
fun parseList(tokens: Tokens): Value {
    if (tokens.length() == 0) throw MissingToken("Expected beginning of list, '('.", tokens)
    val openParen = tokens.next() // Skip (
    val out = ArrayList<Value>()
    while (tokens.length() > 0) {
        val next = tokens.peek()
        if (next.token is TokenType.CloseParen) {
            tokens.next() // Skip )
            return Value(ValueType.VList(out), openParen.line, openParen.col)
        } else {
            out.add(parseValue(tokens)) // Add next value
        }
    }
    throw MissingToken("Expected another value or end of list, ')'.", tokens)
}
