package dev.ashli.kindling

import dev.ashli.kindling.serializer.sendPackageRecode
import dev.ashli.kindling.transpiler.Selector
import dev.ashli.kindling.transpiler.codeblocks.header.Function
import dev.ashli.kindling.transpiler.codeblocks.header.PlayerEvent
import dev.ashli.kindling.transpiler.codeblocks.normal.PlayerAction
import dev.ashli.kindling.transpiler.values.Parameter
import dev.ashli.kindling.transpiler.values.StyledText
import dev.ashli.kindling.transpiler.values.param.DFType

fun main(inputs: Array<String>) {
    val dfp = DFProgram(listOf(
        PlayerEvent("Join", listOf(
            PlayerAction("SendMessage", Selector.DEFAULT, listOf(StyledText.fromMiniMessage("<blue>Hi %default!")))
        )),
        Function("RemoveWhitespace", listOf(), null, listOf(
            Parameter("string", DFType.STRING)
        ))
    ))
    sendPackageRecode(dfp, 1, true)
}
