package com.wao.skydodge.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toArgb
import com.wao.skydodge.R
import com.wao.skydodge.egine.Carro

class Venceu(
    var context: Context,
    var w: Int,
    var h: Int, var tipo: Int,
    val offset: Offset,
    val carro: Carro
) {

    val options = BitmapFactory.Options().apply {
        inPreferredConfig = Bitmap.Config.RGB_565
    }
    var next: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.next, options)
    var nextb: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.nextb, options)

    var cartaz: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.cartazp, options)

    val largura = w
    val altura = h
    var carroX = Bitmap.createBitmap(largura, altura, Bitmap.Config.ARGB_8888)
    val canvas2 = Canvas(carroX)
    var tirouFoto = false
    private val cartazP = Bitmap.createScaledBitmap(
        cartaz,
        ((w * 0.4f)).toInt(),
        ((w * 0.5f)).toInt(),
        false
    )
    var btm = BotaoBitmap(
        this.context,
        (offset.x * 0.5f) - ((this.w * 0.12) / 2).toFloat(),
        (h * 0.76).toFloat(),
        (this.w * 0.12).toInt(),
        (this.w * 0.10).toInt(),
        next
    )


    init {
//    carroX = Bitmap.createScaledBitmap(
//        carroX,
//        (w).toInt(),
//        (h).toInt(),
//        false
//    )
    }

    fun tirarFoto() {
        //    if(!tirouFoto){



        var pozx = (carro.centerX) - (carro.largura)
        var pozY = (carro.centerY) - (carro.altura * 4)


        canvas2.save()

        canvas2.translate(pozx, pozY) // Desloca o ponto (0,0)

        carro.draw(canvas2)
        canvas2.restore()
        carroX = copiarPixelsComAlpha(carroX)
//            tirouFoto = true
//        }
    }

    fun copiarPixelsComAlpha(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val paint = Paint()
        var xM = Int.MIN_VALUE
        var xm = Int.MAX_VALUE

        var yM = Int.MIN_VALUE
        var ym = Int.MAX_VALUE

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = bitmap.getPixel(x, y)
                val alpha = Color.alpha(pixel)

                if (alpha > 0) {
                    xm = Math.min(x, xm)
                    xM = Math.max(x, xM)

                    ym = Math.min(y, ym)
                    yM = Math.max(y, yM)

                }
            }
        }

        val largura = xM - xm
        val altura = yM - ym

        var bb = Bitmap.createBitmap(largura, altura, Bitmap.Config.ARGB_8888)

        val canvas3 = Canvas(bb)
        var xx = 0
        for (x in xm until xM) {
            var yy = 0
            for (y in ym until yM) {
                val pixel = bitmap.getPixel(x, y)
                val alpha = Color.alpha(pixel)

                if (alpha > 250) {
                    paint.color = pixel
                    canvas3.drawPoint((xx).toFloat(), (yy).toFloat(), paint)
                }
                yy++
            }

            xx++
        }

        bb = Bitmap.createScaledBitmap(
            bb,
        (bb.width*0.60f).toInt(),
        (bb.height*0.60f).toInt(),
        false
    )

        return bb
    }



    fun draw(canvas: Canvas) {
        val paint = Paint()
        // Matriz que aumenta o canal vermelho e verde (amarelo = R + G)
        val colorMatrix = ColorMatrix(
            floatArrayOf(
                1.2f, 0f, 0f, 0f, 40f,  // R
                0f, 1.2f, 0f, 0f, 30f,  // G
                0f, 0f, 1f, 0f, 0f,   // B
                1f, 1f, 1f, 1f, 1f    // A
            )
        )

        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)


        canvas.drawBitmap(
            cartazP,
            (offset.x / 2) - (cartazP.width / 2).toFloat(),
            (h / 2) - (cartazP.height / 2).toFloat(),
            null
        )
        canvas.drawBitmap(carroX, ((offset.x * 0.5f)-carroX.width/2).toFloat(), (h * 0.50f).toFloat(), paint)



        btm.draw(canvas)


    }


    private fun spToPx(sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
    }
}