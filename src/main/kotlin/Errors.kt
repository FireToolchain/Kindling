class SyntaxException(msg: String, row: Int, col: Int) : Exception("$msg\n At: line $row, column $col.")

class UnexpectedToken(msg: String, token: Token) : Exception("Unexpected token. $msg\n At: line ${token.line}, column ${token.col}")
class MissingToken(msg: String, tokens: Tokens) : Exception("Missing token. $msg\n At: line ${tokens.line}, column ${tokens.column}")