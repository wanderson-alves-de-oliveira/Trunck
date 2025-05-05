package com.wao.skydodge.pistas

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

fun drawTrack(canvas: Canvas, track: List<TerrainBlock>) {
    var currentX = 0f
    var baseY = 800  // Linha do solo

    for (block in track) {
        val paint = Paint().apply {
            color = Color.rgb(100, 200, 100)
        }

        val topY = baseY - block.height
        canvas.drawRect(
            currentX.toFloat(),
            topY.toFloat(),
            (currentX + block.width).toFloat(),
            baseY.toFloat(),
            paint
        )

        currentX += block.width
    }
}
