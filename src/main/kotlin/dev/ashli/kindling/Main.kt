package dev.ashli.kindling

import dev.ashli.kindling.serializer.sendPackageRecode
import dev.ashli.kindling.transpiler.Selector
import dev.ashli.kindling.transpiler.codeblocks.header.PlayerEvent
import dev.ashli.kindling.transpiler.codeblocks.normal.PlayerAction
import dev.ashli.kindling.transpiler.values.Text

fun main(inputs: Array<String>) {
    val dfp = DFProgram(listOf(
        PlayerEvent("Join", listOf(
            PlayerAction("SendMessage", Selector.DEFAULT, listOf(Text("hi player")))
        ))
    ))
    sendPackageRecode(dfp, 1, true)
}
