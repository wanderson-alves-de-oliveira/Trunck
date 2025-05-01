package com.wao.skydodge.view

import android.content.Context
import android.graphics.Bitmap
 import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.TypedValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

class ItemLoja(
    var context: Context, var img: Bitmap, var x: Float, var y: Float,
    var w: Int,
    var h: Int, var descri: String, var qtd: Int, var preco: String, var tipo: Int
) {


    private val paint = Paint()
    val icon = Bitmap.createScaledBitmap(
        img,
        ((w * 0.13f)).toInt(),
        ((w * 0.13f)).toInt(),
        false
    )

    var espaco = 0f

    var btm = BotaoM(
        this.context,
        (x + (this.w * 0.6)).toFloat(),
        (y + this.h * 0.055).toFloat(),
        (this.w * 0.3).toInt(),
        (this.h * 0.085).toInt(),
        4,
        preco.toString()
    )


    private var tam = 5f
    var liberado = false

    fun draw(canvas: Canvas) {


            liberado = true


        paint.color = Color(0xFF000000).toArgb()
        paint.alpha = 150
        val b3: Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvasB = Canvas(b3)



        if (tipo == 0) {
            paint.color = Color(0xFF4CAF50).toArgb()


            paint.color = Color(0xFF673AB7).toArgb()
            btm.stt = preco


        } else {
            when (tipo) {
                1 -> {


                    paint.color = Color(0xFFE91E63).toArgb()
                    btm.stt = preco
                }
                2 -> {


                    paint.color = Color(0xFFFFFFFF).toArgb()
                    btm.stt = preco
                }
            }
        }

        // paint.alpha = 150
        desenharRetanguloArredondadoCentralizado(
            canvasB,
            (this.w * 0.16f) * tam,
            (this.h * 0.018f) * tam,
            70f,
            1f,
            paint
        )

        if(tipo==2) {
            paint.color = Color(0xFF000000).toArgb()
        }else  {
            paint.color = Color(0xFFFFFFFF).toArgb()
        }
        paint.textSize = spToPx(w * 0.015f)

        canvasB.drawText(
            "$qtd",
            x + (w * 0.4).toFloat(),
            y + (h * 0.1).toFloat(),
            paint
        )

        paint.color = Color(0xFFFF9800).toArgb()

        paint.textSize = spToPx(w * 0.012f)
        canvasB.drawText(
            descri,
            x + (w * 0.4).toFloat(),
            y + (h * 0.12).toFloat(),
            paint
        )


        canvasB.drawBitmap(
            icon,
            x + (w * 0.12).toFloat(),
            y + (h * 0.06).toFloat(),
            paint
        )
        paint.color = Color(0xFFFFFFFF).toArgb()

        canvas.drawBitmap(b3, 0f, 0f, paint)
        if (tipo == 0) {

            btm.draw(canvas)


        } else {
            when (tipo) {
                1 -> {

                    btm.draw(canvas)

                }
                2 -> {
                    btm.camada = 5
                    btm.draw(canvas)

                }
            }

        }
    }

    private fun desenharRetanguloArredondadoCentralizado(
        canvas: Canvas,
        largura: Float,
        altura: Float,
        raio: Float,
        alt: Float,
        paint: Paint
    ) {
        // Calcula o centro da tela
        val centroX = canvas.width / 2f
        val centroY = canvas.height / 2f

        // Calcula os limites do retângulo arredondado
        val esquerda = centroX - largura / 2f
        val topo = (y + this.h * 0.05).toFloat()
        val direita = centroX + largura / 2f
        val inferior = ((y + this.h * 0.05) + altura).toFloat()

        // Cria um objeto RectF com os limites calculados
        val rect = RectF(esquerda, topo, direita, inferior)

        canvas.drawRoundRect(rect, raio, raio, paint)
    }


    private fun spToPx(sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
    }
}