package transpiler.values

import serializer.serializeString

data class Particle(val type: String) : DFValue {
    var amount = 1
    var spreadX = 0f
    var spreadY = 0f
    var motionX: Float? = null
    var motionY: Float? = null
    var motionZ: Float? = null
    var size: Float? = null
    var roll: Float? = null
    var variationSize: Int? = null
    var variationColor: Int? = null
    var variationMotion: Int? = null
    var material: String? = null
    var color: Int? = null // RGB number: R * 256^2 + G * 256 + B

    override fun serialize(): String {
        val settings = mutableListOf<String>()
        if (motionX != null) settings.add(""""x":$motionX""")
        if (motionY != null) settings.add(""""y":$motionY""")
        if (motionZ != null) settings.add(""""z":$motionZ""")
        if (size != null) settings.add(""""size":$size""")
        if (roll != null) settings.add(""""roll":$roll""")
        if (variationSize != null) settings.add(""""sizeVariation":$variationMotion""")
        if (variationColor != null) settings.add(""""colorVariation":$variationMotion""")
        if (variationMotion != null) settings.add(""""motionVariation":$variationMotion""")
        if (material != null) settings.add(""""material":${serializeString(material)}""")
        if (color != null) settings.add(""""rgb":$color""")
        return """{"id":"part","data":{""" +
                    """"particle":${serializeString(type)},""" +
                    """"cluster":{""" +
                        """"amount":$amount,""" +
                        """"horizontal":$spreadX,""" +
                        """"vertical":$spreadY""" +
                    """},"data":{${settings.joinToString(",")}}}}"""
    }
    override fun toString(): String {
        val settings = mutableListOf("amount = $amount", "spreadX = $spreadX", "spreadY = $spreadY")
        if (motionX != null) settings.add("motionX = $motionX")
        if (motionY != null) settings.add("motionY = $motionY")
        if (motionZ != null) settings.add("motionZ = $motionZ")
        if (size != null) settings.add("size = $size")
        if (roll != null) settings.add("rol = $roll")
        if (variationSize != null) settings.add("variationSize = $variationSize")
        if (variationColor != null) settings.add("variationColor = $variationColor")
        if (variationMotion != null) settings.add("variationMotion = $variationMotion")
        if (material != null) settings.add("material = $material")
        if (color != null) settings.add("color = $color")
        return "Par[$type: ${ settings.joinToString(", ") }]"
    }
}