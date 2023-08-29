package dev.ashli.kindling.transpiler.codeblocks.normal

import dev.ashli.kindling.serializer.serialize
import dev.ashli.kindling.serializer.serializeArgs
import dev.ashli.kindling.transpiler.codeblocks.DoubleCodeHolder
import dev.ashli.kindling.transpiler.values.DFValue

/**
 * Represents DiamondFire's if game block.
 * @param type The action of the code block
 * @param inverse This is `true` if the block should have NOT on it's sign
 * @param params Parameters to the code block
 * @param mainBranch The branch to execute if the condition is true
 * @param elseBranch The branch to execute otherwise
 */
data class IfGame(val type: String, val inverse: Boolean, val params: List<DFValue>, val mainBranch: List<DFBlock>, val elseBranch: List<DFBlock>?) :
    DFBlock("if_game", 4 + mainBranch.sumOf { it.literalSize } + (elseBranch?.sumOf { it.literalSize }?.plus(4) ?: 0),
        elseBranch != null && (mainBranch.any { it.isFinal } && elseBranch.any { it.isFinal })), DoubleCodeHolder {

    override fun serialize() = "{" +
            """"id":"block",""" +
            """"block":"if_game",""" +
            """"args":${serializeArgs(params)},""" +
            if (inverse) { """"inverted":"NOT",""" } else { "" } +
            """"action":${type.serialize()}""" +
            """},{"id":"bracket","direct":"open","type":"norm"},""" +
            mainBranch.joinToString("") { it.serialize() + "," } +
            """{"id":"bracket","direct":"close","type":"norm"}""" +
            if (elseBranch != null) {
                """,{"id":"block","block":"else"},""" +
                """{"id":"bracket","direct":"open","type":"norm"},""" +
                elseBranch.joinToString("") { it.serialize() + "," } +
                """{"id":"bracket","direct":"close","type":"norm"}"""
            } else ""
    override fun getElseCode() = this.elseBranch
    override fun getCode() = this.mainBranch
    override fun cloneWith(code: List<DFBlock>, elseCode: List<DFBlock>?) = IfGame(type, inverse, params, code, elseCode)
    override fun cloneWith(code: List<DFBlock>) = IfGame(type, inverse, params, code, elseBranch)
    override fun flipped() = IfGame(type, !inverse, params, mainBranch, elseBranch)
}