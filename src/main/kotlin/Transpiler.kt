fun transpile(input: List<Value>): DFProgram {
    val lines = mutableListOf<DFLine>()
    for (v in input)
        lines.add(parseLine(v))
    return DFProgram(lines)
}
fun parseLine(input: Value): DFLine {
    TODO()
}
fun parseHeader(input: Value): DFHeader {
    TODO()
}

fun parseBlock(input: Value, header: DFHeader): DFBlock {
    TODO()
}

fun parseVal(input: Value, header: DFHeader): DFBlock {
    TODO()
}

