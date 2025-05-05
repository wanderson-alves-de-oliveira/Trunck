package com.wao.skydodge.pistas

fun generateTrack(blockCount: Int): List<TerrainBlock> {
    val blocks = mutableListOf<TerrainBlock>()
    val types = BlockType.values()

    repeat(blockCount) {
        val randomType = types.random()
        val block = when (randomType) {
            BlockType.FLAT -> TerrainBlock(randomType, 300, 0)
            BlockType.SMALL_HILL -> TerrainBlock(randomType, 300, 30)
            BlockType.BIG_HILL -> TerrainBlock(randomType, 300, 60)
            BlockType.GAP -> TerrainBlock(randomType, 200, -50)
            BlockType.DESCENT -> TerrainBlock(randomType, 300, -30)
        }
        blocks.add(block)
    }
    return blocks
}
