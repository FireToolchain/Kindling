package transpiler.values

import serializer.DFSerializable

/**
 * Represents a value in DF, such as Text, Variables, or Items.
 */
sealed interface DFValue : DFSerializable