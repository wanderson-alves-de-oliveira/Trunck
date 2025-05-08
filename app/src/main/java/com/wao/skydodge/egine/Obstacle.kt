package com.wao.skydodge.egine

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import com.wao.skydodge.ferramentas.Colisao
import com.wao.skydodge.view.Fundo
import com.wao.skydodge.view.Roda

class Obstacle(
    val bitmap: Bitmap,
    var x: Float,
    var y: Float,
    val width: Int,
    val height: Int
) {
    var colisao = Colisao()
    fun verirficarColisao(fundo: Fundo): Boolean {


        val novoX = x
        val novoY = y
        val rect = Rect(
            (novoX).toInt(),
            (novoY).toInt(),
            (novoX + (width)).toInt(),
            (novoY + (width)).toInt()
        )



        if (colisao.colideComMapa(
                rect,
                fundo.backgroundMountains,
                fundo.mountainsX.toInt(),
                fundo.mountainsY.toInt()
            )

        ) {

            val terrainY = colisao.ultimoY
            return (y + height > terrainY)

        } else if (colisao.colideComMapa(
                rect,
                fundo.backgroundMountains2,
                (fundo.mountainsX2.toInt()),
                fundo.mountainsY.toInt()
            )
        ) {
            val terrainY = colisao.ultimoY
            return (y + height > terrainY)


        }

        return false
    }


    fun update(scrollSpeed: Float) {
        x -= scrollSpeed
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, null, RectF(x, y, x + width, y + height), null)
    }

    fun isOffScreen(): Boolean = x + width < 0
}
