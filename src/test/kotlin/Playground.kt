import dev.ashli.kindling.DFProgram
import dev.ashli.kindling.serializer.sendPackageRecode
import dev.ashli.kindling.transpiler.Selector
import dev.ashli.kindling.transpiler.codeblocks.header.PlayerEvent
import dev.ashli.kindling.transpiler.codeblocks.normal.PlayerAction
import dev.ashli.kindling.transpiler.values.Text
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class Playground {
    @Test
    fun playground() {
        // Put playground code here
        sendPackageRecode(DFProgram(
            listOf(
                PlayerEvent("Join", listOf(
                    PlayerAction("SendMessage", Selector.ALL_PLAYERS, listOf(Text.fromMiniMessage("<blue>%default joined!")))
                ))
            )
        ), 36, true)
    }
}