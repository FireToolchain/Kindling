package serializer

class SerializationError(type: String, at: DFSerializable) : Error("Fatal: $type at $at")