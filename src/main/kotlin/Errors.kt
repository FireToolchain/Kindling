class SyntaxException(msg: String, row: Int, col: Int) : Exception("$msg\n At: line $row, column $col.")

class UnexpectedToken(msg: String, token: Token) : Exception("Unexpected token. $msg\n At: line ${token.line}, column ${token.col}")
class MissingToken(msg: String, tokens: Tokens) : Exception("Missing token. $msg\n At: line ${tokens.line}, column ${tokens.column}")

class UnexpectedValue(type: String, value: Value) : Exception("Unexpected value. Expected $type, got ${valueName(value)}.\n At line ${value.line}, column ${value.column}.")
class MalformedList(struct: String, expected: String, violated: Value) : Exception("Malformed $struct. Expected format $expected, got ${
    if (violated.type is ValueType.VList) {
        "(${violated.type.inner.joinToString(" ") { valueName(it) }})"
    } else "received ${valueName(violated)} instead."
}.\n At line ${violated.line}, column ${violated.column}")

fun valueName(v: Value): String {
    return when (v.type) {
        is ValueType.Ident -> "<Identifier>"
        is ValueType.Num -> "<Number>"
        is ValueType.Str -> "<String>"
        is ValueType.VList -> "<List>"
    }
}