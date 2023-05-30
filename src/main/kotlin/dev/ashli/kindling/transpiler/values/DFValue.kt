package dev.ashli.kindling.transpiler.values

import dev.ashli.kindling.serializer.DFSerializable

/**
 * Represents a value in DF, such as Text, Variables, or Items.
 */
sealed interface DFValue : DFSerializable