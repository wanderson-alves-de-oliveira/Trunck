package com.wao.skydodge.egine

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
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

    var voar = false
    var caiu = false
    var snowmanVelocityX = 0f
    var snowmanVY = 0f
    val gravity = 0.5f
    var isSnowmanMoving = false
    var yR = 0f
    var giro = 0f
    var chegou = false
    var paint = Paint()
    var sumindo = false
    fun verirficarColisao(fundo: Fundo): Boolean {

        if (y > fundo.backgroundMountains.height || y < 0) return true
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
            if ((y + height > terrainY)) yR = terrainY
            return (y + height > terrainY)

        } else if (colisao.colideComMapa(
                rect,
                fundo.backgroundMountains2,
                (fundo.mountainsX2.toInt()),
                fundo.mountainsY.toInt()
            )
        ) {
            val terrainY = colisao.ultimoY
            yR = terrainY
            return (y + height > terrainY)


        }

        return false
    }

    fun girar() {
        if (!chegou) {
            if (giro < 360) {
                giro += 10f
            } else {
                giro = 10f
            }
        }

    }

    fun update(scrollSpeed: Float) {


        if (voar) {

            x += snowmanVelocityX
            y += snowmanVY

            snowmanVY += gravity
            snowmanVelocityX *= 0.98f


            // Se a velocidade for baixa, parar movimento
            if (kotlin.math.abs(snowmanVelocityX) < 0.1f) {
                voar = false
                snowmanVelocityX = 0f
            }

        } else {
            x -= scrollSpeed
        }

        if (sumindo) {
            paint.alpha -= 15
            if (paint.alpha <= 150) {
                x = -200f
            }

        }


    }

    fun draw(canvas: Canvas) {
        if (caiu) {
            val centerX = x + bitmap.width / 2
            val centerY = y + bitmap.height / 2
            canvas.save()
            canvas.rotate(giro, centerX, centerY)
            canvas.drawBitmap(bitmap, null, RectF(x, y, x + width, y + height), paint)
            canvas.restore()
        }
    }

    fun isOffScreen(): Boolean = x + width < 0
}
