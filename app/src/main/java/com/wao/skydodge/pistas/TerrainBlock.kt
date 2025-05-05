package com.wao.skydodge.pistas

data class TerrainBlock(
    val type: BlockType,
    val width: Int,
    val height: Int
)

enum class BlockType {
    FLAT, SMALL_HILL, BIG_HILL, GAP, DESCENT
}
