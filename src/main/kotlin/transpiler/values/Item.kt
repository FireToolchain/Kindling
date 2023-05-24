package transpiler.values

import Value
import transpiler.CheckContext

data class Item(val data: String) : DFValue {
    companion object {
        fun transpileFrom(input: Value, context: CheckContext): Item {
            TODO("Not yet implemented")
        }
    }
    override fun serialize() = "{}" // TODO
    override fun toString() = "{ITEM}"
}