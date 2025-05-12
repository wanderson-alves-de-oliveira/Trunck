package com.wao.skydodge.egine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import com.wao.skydodge.R
import com.wao.skydodge.ferramentas.Colisao
import com.wao.skydodge.view.Fundo
import com.wao.skydodge.view.Roda
import kotlin.random.Random

class Obstacle( bitmap:Bitmap,
                var x: Float,
                var y: Float) {
    var colisao = Colisao()

    var bitmap: Bitmap= bitmap

    var width: Int=0
    var height: Int=0

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
    var causa = 0
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

    fun girarRand() {

                giro =  (Random.nextInt(0, 361)).toFloat()


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

    fun update(scrollSpeed: Float,lastTimeMillis:Long) {
        val deltaTime = lastTimeMillis / 1000f

        if (voar) {

            x += snowmanVelocityX* deltaTime
            y += snowmanVY* deltaTime

            snowmanVY += gravity
            snowmanVelocityX *= 0.98f


            // Se a velocidade for baixa, parar movimento
            if (kotlin.math.abs(snowmanVelocityX) < 0.1f) {
                voar = false
                snowmanVelocityX = 0f
            }

        } else {
            x -= scrollSpeed * deltaTime
        }

        if (sumindo) {
            paint.alpha -= 15
            if (paint.alpha <= 150) {
                x = -200f
            }

        }


    }

    fun pegarObj(context: Context, modo: Int): Bitmap {
        var obstacleBitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.caixa
        ) // substitua pela sua imagem

        height =100
        width =100

        obstacleBitmap = Bitmap.createScaledBitmap(
            obstacleBitmap,
            (100).toInt(),
            (100).toInt(),
            false
        )
        when (modo) {
            0 -> {

                val i = Random.nextInt(0, 4)
                when (i) {
                    0 -> {
                        causa = 0
                        height =180
                        width =100
                        var obstacleBitmap = BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.boneco
                        ) // substitua pela sua imagem
                        obstacleBitmap = Bitmap.createScaledBitmap(
                            obstacleBitmap,
                            (100).toInt(),
                            (180).toInt(),
                            false
                        )
                        return obstacleBitmap
                    }

                    1 -> {
                        causa = 1
                        height =100
                        width =100
                        var obstacleBitmap = BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.caixa
                        ) // substitua pela sua imagem
                        obstacleBitmap = Bitmap.createScaledBitmap(
                            obstacleBitmap,
                            (100).toInt(),
                            (100).toInt(),
                            false
                        )
                        return obstacleBitmap
                    }

                    2 -> {
                        causa = 2
                        height =110
                        width =90
                        var obstacleBitmap = BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.barril
                        ) // substitua pela sua imagem
                        obstacleBitmap = Bitmap.createScaledBitmap(
                            obstacleBitmap,
                            (90).toInt(),
                            (110).toInt(),
                            false
                        )
                        return obstacleBitmap
                    }


                    3 -> {
                        causa = 3
                        height =90
                        width =70
                        var obstacleBitmap = BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.gas
                        ) // substitua pela sua imagem
                        obstacleBitmap = Bitmap.createScaledBitmap(
                            obstacleBitmap,
                            (70).toInt(),
                            (90).toInt(),
                            false
                        )
                        return obstacleBitmap
                    }

                }

            }


        }

return obstacleBitmap
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
