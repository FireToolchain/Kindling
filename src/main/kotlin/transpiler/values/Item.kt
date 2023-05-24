package transpiler.values

data class Item(val data: String) : DFValue {
    override fun serialize() = "{}" // TODO
    override fun toString() = "{ITEM}"
}