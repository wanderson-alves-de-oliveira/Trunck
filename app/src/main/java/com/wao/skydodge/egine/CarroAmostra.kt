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
    var rodaF : Bitmap =  BitmapFactory.decodeResource(context.resources, R.drawable.rodaa)
    var rodaT : Bitmap =  BitmapFactory.decodeResource(context.resources, R.drawable.rodaa)
    var bitmap: Bitmap =  BitmapFactory.decodeResource(context.resources, R.drawable.chassia)
    var rotacao = 0f

    private val display: DisplayMetrics = context.resources.displayMetrics
    private val h = display.heightPixels
    private val w = display.widthPixels
    var largura = -550f
    var altura = 250f
    val rodaTx = (w / 2).toFloat() + (largura *0.42f)
    val rodaFx = rodaTx - (largura *0.55f)
    val rodaFy = (h / 2).toFloat() - (altura / 2)
    val rodaTy = (h / 2).toFloat() - (altura / 2)

    init {
        rodaF = Bitmap.createScaledBitmap(
            rodaF,
            (200).toInt(),
            (200).toInt(),
            false
        )

        rodaT = Bitmap.createScaledBitmap(
            rodaT,
            (200).toInt(),
            (200).toInt(),
            false)

        bitmap = Bitmap.createScaledBitmap(
            bitmap,
            (largura).toInt(),
            (altura).toInt(),
            false
        )

    }
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





        val centerX = (w / 2).toFloat() + (largura/2 )
        val centerY = (h / 2).toFloat() - (altura / 2)
        val pontoChassiFrente = pontoNoChassi(rodaFx-(w*0.03f), centerY-(altura/1.8f), 60f, 30f, rotacao*-1.8f)
        val  pontoChassiTras = pontoNoChassi(rodaTx+(w*0.08f), centerY-(altura/1.8f), -60f, 30f, rotacao*-1.8f)

        canvas.save()
        canvas.rotate(rotacao*-1, centerX, centerY)

        //   paintAmortecedor.color = Color.RED
        canvas.drawLine(
            pontoChassiTras.x,
            pontoChassiTras.y,
            rodaTx+(200*0.5f),
            rodaTy+(200*0.5f),
            paintAmortecedor
        )
        // paintAmortecedor.color = Color.BLUE
// Desenha amortecedor dianteiro
        canvas.drawLine(
            pontoChassiFrente.x,
            pontoChassiFrente.y,
            rodaFx+(200*0.5f),
            rodaFy+(200*0.5f),
            paintAmortecedor
        )

        canvas.restore()
        canvas.save()
        canvas.rotate(rotacao*-1.8f, centerX, centerY)

        canvas.drawBitmap(rodaT, rodaTx, rodaTy, null)
        canvas.drawBitmap(rodaF, rodaFx, rodaFy, null)

        canvas.drawBitmap(bitmap, centerX, centerY-(altura*0.8f), null)

        canvas.restore()


    }


}