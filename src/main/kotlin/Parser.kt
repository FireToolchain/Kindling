sealed class Value {
    class VList(inner: List<Value>)
    class Ident(str: String)
    class Str(str: String)
    class Num(num: Float)
}

fun parse(tokens: Tokens): Value {
    TODO();
}