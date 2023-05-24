package transpiler.values

import MalformedList
import UnexpectedValue
import Value
import serializer.serialize
import transpiler.*

data class Particle(val type: String) : DFValue {
    companion object {
        fun transpileFrom(input: Value, context: CheckContext): Particle {
            val inpList = checkList(input)
            if (inpList.size != 3) throw MalformedList("Value", "(par String<Name> List<Settings>)", input)
            val par = Particle(checkStr(inpList[1]))
            for (s in checkList(inpList[2])) {
                val setting = checkList(s)
                if (setting.size != 2) throw MalformedList("Particle Setting", "(Identifier<Name> <Value>)", s)
                when (checkIdent(setting[0])) {
                    "amount" -> par.amount = checkInt(setting[1])
                    "spread-x" -> par.spreadX = checkNum(setting[1])
                    "spread-y" -> par.spreadY = checkNum(setting[1])
                    "motion-x" -> par.motionX = checkNum(setting[1])
                    "motion-y" -> par.motionY = checkNum(setting[1])
                    "motion-z" -> par.motionZ = checkNum(setting[1])
                    "roll" -> par.roll = checkNum(setting[1])
                    "size" -> par.size = checkNum(setting[1])
                    "color" -> par.color = checkInt(setting[1])
                    "material" -> par.material = checkStr(setting[1])
                    "variation-color" -> par.variationColor = checkInt(setting[1])
                    "variation-motion" -> par.variationMotion = checkInt(setting[1])
                    "variation-size" -> par.variationSize = checkInt(setting[1])
                    else -> throw UnexpectedValue("a valid particle setting", s)
                }
            }
            return par
        }
    }
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
        if (material != null) settings.add(""""material":${material!!.serialize()}""")
        if (color != null) settings.add(""""rgb":$color""")
        return """{"id":"part","data":{""" +
                    """"particle":${type.serialize()},""" +
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