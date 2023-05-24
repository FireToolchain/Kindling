package transpiler.values

import Value
import serializer.DFSerializable
import transpiler.CheckContext

/**
 * Represents a value in DF, such as Text, Variables, or Items.
 */
sealed interface DFValue : DFSerializable