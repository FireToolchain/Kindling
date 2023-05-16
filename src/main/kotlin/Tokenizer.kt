data class Tokens(val list: List<Token>): Iterator<Token> {
    var line = 1
        private set
    var column = 0
        private set
    private var index = 0
    override operator fun hasNext() = index <= list.lastIndex
    override operator fun next(): Token {
        val token = peek()
        line = token.line
        column = token.col + token.width
        index++
        return token
    }
    fun peek(i: Int = 0) = list[index + i]
    fun length() = list.size - index
}

// Token Data + Position
data class Token(val token: TokenType, val col: Int, val line: Int, val width: Int)
sealed interface TokenType {
    data object OpenParen : TokenType
    data object CloseParen : TokenType
    data class Ident(val str: String) : TokenType
    data class Str(val str: String) : TokenType
    data class Num(val num: Float) : TokenType
}

fun tokenize(str: String): Tokens {
    val out = ArrayList<Token>()
    var i = 0
    var column = 0
    var line = 1
    while (i < str.length) {
        val char = str[i];
        if (char == ';') {
            while (i < str.length && str[i] != '\n') i++
        }
        else if (char == '(') out.add(Token(TokenType.OpenParen, column, line, 1))
        else if (char == ')') out.add(Token(TokenType.CloseParen, column, line, 1))
        else if (char == '\n') {
            column = 0
            line++
        } else if (char.isDigit()) {
            val colPos = column
            var num = 0f
            while (str[i].isDigit()) {
                num *= 10
                num += str[i].digitToInt() // Safe
                i++; column++
            }
            if (str[i] == '.') {
                var place = 1f
                i++; column++
                while (str[i].isDigit()) {
                    place /= 10f
                    num += place * str[i].digitToInt() // Safe
                    i++; column++
                }
            }
            out.add(Token(TokenType.Num(num), colPos, line, column - colPos))
            continue
        } else if (char == '.') {
            val colPos = column
            var num = 0f
            var place = 1f
            i++; column++
            while (str[i].isDigit()) {
                place /= 10f
                num += place * str[i].digitToInt() // Safe
                i++; column++
            }
            out.add(Token(TokenType.Num(num), colPos, line, column - colPos))
            continue
        } else if (char == '"' || char == '\'') {
            i++; column++
            val colPos = column
            var s = ""
            var escaped = false
            while (i < str.length) {
                val c = str[i]
                if (c == '\n') throw SyntaxException("Unclosed string.", colPos, line)
                else if (escaped) {
                    s += when (c) {
                        '\\' -> '\\'
                        'n' -> '\n'
                        'r' -> '\r'
                        't' -> '\t'
                        'b' -> '\b'
                        char -> char
                        else -> throw SyntaxException("Unknown escape sequence '\\$c'.", column - 1, line)
                    }
                    escaped = false
                } else {
                    if      (c == '\\') escaped = true
                    else if (c == char) break
                    else                s += c
                }
                i++; column++
            }
            out.add(Token(TokenType.Str(s), colPos, line, column - colPos))
        } else if (char.isLetter() || char == '-') {
            val colPos = column
            var s = ""
            while (i < str.length && (str[i].isLetter() || str[i] == '-')) {
                s += str[i]
                i++; column++
            }
            out.add(Token(TokenType.Ident(s), colPos, line, column - colPos))
            continue
        } else if (!char.isWhitespace()) {
            throw SyntaxException("Unexpected character '$char'.", column, line)
        }
        i++; column++
    }
    return Tokens(out)
}