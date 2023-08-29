package dev.ashli.kindling.transpiler.values.param

enum class DFType(val internalName: String) {
    STRING("txt"),
    STYLED_TEXT("comp"),
    NUMBER("num"),
    LOCATION("loc"),
    POTION_EFFECT("pot"),
    PARTICLE("part"),
    SOUND("snd"),
    TAG("bl_tag"),
    VARIABLE("var"),
    VECTOR("vec"),
    ANY_VALUE("any"),
    LIST("list"),
    DICTIONARY("dict"),
}