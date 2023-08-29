package dev.ashli.kindling.transpiler.values.param

/**
 * An enum encompassing all types that DiamondFire natively supports.
 * This is useful as Function Parameters require you to specify their type.
 * @param internalName The internal name of the type
 */
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