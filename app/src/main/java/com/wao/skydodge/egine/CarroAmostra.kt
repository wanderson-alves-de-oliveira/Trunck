package com.wao.skydodge.egine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.DisplayMetrics
import com.wao.skydodge.R
import com.wao.skydodge.view.Roda
import kotlin.math.cos
import kotlin.math.sin

class CarroAmostra(context: Context)  {
    var rodaF = Roda(context)
    var rodaT = Roda(context)
    var bitmap: Bitmap =  BitmapFactory.decodeResource(context.resources, R.drawable.chassib)
    var rotacao = 0f
    var largura = 0f
    var altura = 150f
    private val display: DisplayMetrics = context.resources.displayMetrics
    private val h = display.heightPixels
    private val w = display.widthPixels
    fun pontoNoChassi(carroX: Float, carroY: Float, offsetX: Float, offsetY: Float, angulo: Float): PointF {
        val rad = Math.toRadians(angulo.toDouble())
        val x = carroX + offsetX * cos(rad) - offsetY * sin(rad)
        val y = carroY + offsetX * sin(rad) + offsetY * cos(rad)
        return PointF(x.toFloat(), y.toFloat())
    }

    fun draw(canvas: Canvas) {


        val paintAmortecedor = Paint().apply {
            color = Color.DKGRAY
            strokeWidth = 30f
            style = Paint.Style.STROKE
        }


        val centerX = (rodaT.x + rodaF.x) / 2
        val centerY = (rodaT.y + rodaF.y) / 2
        val pontoChassiFrente = pontoNoChassi(rodaF.x-(w*0.03f), centerY-(altura/1.8f), 60f, 30f, rotacao*-1.8f)
        val  pontoChassiTras = pontoNoChassi(rodaT.x+(w*0.08f), centerY-(altura/1.8f), -60f, 30f, rotacao*-1.8f)

        canvas.save()
        canvas.rotate(rotacao*-1, centerX, centerY)

        //   paintAmortecedor.color = Color.RED
        canvas.drawLine(
            pontoChassiTras.x,
            pontoChassiTras.y,
            rodaT.x+(rodaT.largura*0.5f),
            rodaT.y+(rodaT.largura*0.5f),
            paintAmortecedor
        )
        // paintAmortecedor.color = Color.BLUE
// Desenha amortecedor dianteiro
        canvas.drawLine(
            pontoChassiFrente.x,
            pontoChassiFrente.y,
            rodaF.x+(rodaT.largura*0.5f),
            rodaF.y+(rodaT.largura*0.5f),
            paintAmortecedor
        )
        rodaT.draw(canvas)
        rodaF.draw(canvas)

        canvas.restore()
        canvas.save()
        canvas.rotate(rotacao*-1.8f, centerX, centerY)

        canvas.drawBitmap(bitmap, rodaT.x*0.95f, centerY-(altura*0.8f), null)


        canvas.restore()


    }


}