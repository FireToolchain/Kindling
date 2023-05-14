class Tokens(list: List<Token>) {

}

// Token Data + Position
class Token(token: TokenType, x: Int, y: Int);
sealed class TokenType {
    object OpenParen : TokenType()
    object CloseParen : TokenType()
    class Ident(str: String) : TokenType()
    class Str(str: String) : TokenType()
    class Num(num: Float) : TokenType()
}

fun tokenize(str: String): Tokens {
    val out = ArrayList<Token>()
    var i = 0
    var x = 0
    var y = 1
    while (i < str.length) {
        val char = str[i];
        if (i + 1 < str.length && char == '/' && str[i+1] == '/') { // Two slashes together
            while (i < str.length && str[i] != '\n') i++

        }
        else if (char == '(') out.add(Token(TokenType.OpenParen, x, y))
        else if (char == ')') out.add(Token(TokenType.CloseParen, x, y))
        else if (char == '\n') {
            x = 0
            y++
        } else if (char.isDigit()) {

        } else if (char == '.') {

        } else if (char == '"' || char == '\'') {

        } else if (char.isLetter()) {

        } else {
            // Error
        }
        i++
        x++
    }
    return Tokens(out)
}