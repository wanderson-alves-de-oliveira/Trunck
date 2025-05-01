package com.wao.skydodge.egine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
 import android.graphics.PointF
import android.graphics.Rect
 import com.wao.skydodge.R
import com.wao.skydodge.ferramentas.Colisao
 import com.wao.skydodge.view.Fundo
import com.wao.skydodge.view.Roda
import java.lang.StrictMath.toDegrees
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Carro(context: Context) {
    var colisao = Colisao()
    private var rodaF = Roda(context)
    private var rodaT = Roda(context)
    var velocidadedoGiro = 0f
    var screenHeight = 0

//     private val wheelDistance = 120f // distância entre rodas
//
//
//    var rearWheelY = 0f
//    var frontWheelY = 0f
//
//    var avgY = 0f
//    var deltaX = 0f
//    var deltaY = 0f
//
//    var angle = 0f
    var rotacao = 0f
    var largura = 0f
    var altura = 150f
    private var bitmap: Bitmap =  BitmapFactory.decodeResource(context.resources, R.drawable.chassi)



    init {
        rodaF.x = 500f
        rodaT.x = 300f
//        rearWheelY = rodaT.y
//        frontWheelY = rodaF.y
//
//        avgY = (rearWheelY + frontWheelY) / 2f
//        deltaX = rodaF.x - rodaT.x
//        deltaY = rearWheelY - frontWheelY
         largura = rodaT.x - (rodaF.x+(rodaT.largura*1.05f))
         altura = 150f




      //  angle = atan2(deltaY, deltaX) // inclinação do terreno
         bitmap = Bitmap.createScaledBitmap(
            bitmap,
            (largura).toInt(),
            (altura).toInt(),
            false
        )
    }

//  fun   calculoR(){
//      rearWheelY = rodaT.y
//      frontWheelY = rodaF.y
//
//      avgY = (rearWheelY + frontWheelY) / 2f
//      deltaX = rodaF.x - rodaT.x
//      deltaY = rearWheelY - frontWheelY
//
//      angle = atan2(deltaY, deltaX)
//  }
    fun iniciarRodas() {
        rodaF.screenHeight
        rodaT.screenHeight
    }

    fun update(fundo: Fundo) {

       // calculoR()
        rodaF.update()
        rodaT.update()


//        val deltaX = rodaF.x - rodaT.x
//        val deltaY = rodaT.y - rodaF.y
        rotacao = calcularRotacaoEntreRodas(rodaT.y,rodaF.y,rodaT.x,rodaF.x)

        verirficarColisao(fundo, rodaF)
        verirficarColisao(fundo, rodaT)

        // verirficarDDistanciDeEixo()


    }

    fun calcularRotacaoEntreRodas(yTraseira: Float, yDianteira: Float, xTraseira: Float, xDianteira: Float): Float {
        val deltaX = xDianteira - xTraseira
        val deltaY = yTraseira - yDianteira // importante: eixo Y é invertido
          return toDegrees(atan2(deltaY.toDouble(), deltaX.toDouble())).toFloat()
    }
    fun verirficarColisao(fundo: Fundo, roda: Roda) {


        val novoX = roda.x
        val novoY = roda.y
        val rect = Rect(
            (novoX).toInt(),
            (novoY).toInt(),
            (novoX + (roda.largura)).toInt(),
            (novoY + (roda.altura)).toInt()
        )



        if (colisao.colideComMapa(
                rect,
                fundo.backgroundMountains,
                fundo.mountainsX.toInt(),
                fundo.mountainsY.toInt()
            )

        ) {

            //  gameouver = true

            colidiu(roda)

        } else if (colisao.colideComMapa(
                rect,
                fundo.backgroundMountains2,
                (fundo.mountainsX2.toInt()),
                fundo.mountainsY.toInt()
            )
        ) {

            //  gameouver = true
            colidiu(roda)
        } else {
            roda.gravity = 2.0f
        }

    }

    private fun colidiu(roda: Roda) {

        val terrainY = colisao.ultimoY
        if (roda.y + roda.altura > terrainY) {
            roda.y = terrainY - (roda.altura*0.8f)
            roda.gravity = 0f

        }


    }
    fun pontoNoChassi(carroX: Float, carroY: Float, offsetX: Float, offsetY: Float, angulo: Float): PointF {
        val rad = Math.toRadians(angulo.toDouble())
        val x = carroX + offsetX * cos(rad) - offsetY * sin(rad)
        val y = carroY + offsetX * sin(rad) + offsetY * cos(rad)
        return PointF(x.toFloat(), y.toFloat())
    }

    fun draw(canvas: Canvas) {

        val paintRoda = Paint().apply { color = Color.BLACK }
        val paintChassi = Paint().apply {
            color = Color.rgb(139, 69, 19) // marrom
            style = Paint.Style.FILL
        }
        val paintContorno = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }

        val paintAmortecedor = Paint().apply {
            color = Color.DKGRAY
            strokeWidth = 30f
            style = Paint.Style.STROKE
        }

// Desenha amortecedor traseiro



        // Chassi
        val centerX = (rodaT.x + rodaF.x) / 2
        val centerY = (rodaT.y + rodaF.y) / 2
       val pontoChassiFrente = pontoNoChassi(rodaF.x*0.95f, centerY-(altura/1.5f), 60f, 30f, rotacao*-1.8f)
       val  pontoChassiTras = pontoNoChassi(rodaT.x*1.5f, centerY-(altura/1.8f), -60f, 30f, rotacao*-1.8f)

        canvas.save()
        canvas.rotate(rotacao*-1, centerX, centerY)


        canvas.drawLine(
            pontoChassiTras.x,
            pontoChassiTras.y,
            rodaT.x+(rodaT.largura*0.5f),
            rodaT.y+(rodaT.largura*0.5f),
            paintAmortecedor
        )

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

        canvas.drawBitmap(bitmap, rodaT.x*0.95f, centerY-altura, null)


        canvas.restore()


    }

}