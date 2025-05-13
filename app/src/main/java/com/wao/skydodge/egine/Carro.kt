package com.wao.skydodge.egine

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
 import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.DisplayMetrics
import androidx.compose.ui.geometry.Offset
import com.wao.skydodge.R
import com.wao.skydodge.ferramentas.Colisao
import com.wao.skydodge.view.Fundo
import com.wao.skydodge.view.Roda
import java.lang.StrictMath.toDegrees
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Carro(context: Context,
            var h :Int ,
            var w :Int ) {
    var colisao = Colisao()
      var rodaF = Roda(context,(w*0.12f).toFloat(),(w*0.12f).toFloat())
      var rodaT = Roda(context,(w*0.12f).toFloat(),(w*0.12f).toFloat())
     var screenHeight = 0
    var reduzindo = false
    var parou = false
    var acelerando = false
 var estacionado = false
    var garagem = false
    var rotacao = 0f
    var largura = 0f
    var altura = 0f
    val options = BitmapFactory.Options().apply {
        inPreferredConfig = Bitmap.Config.RGB_565
    }
    var bitmap: Bitmap =  BitmapFactory.decodeResource(context.resources, R.drawable.chassia,options)
    var alturaY = 112.5f


    init {
        rodaF.x =w*0.56f
        rodaT.x = w*0.4f

         largura = (rodaT.x - (rodaF.x+(rodaT.largura*1.05f))).toFloat()
         altura = (rodaT.altura ).toFloat()
        alturaY = altura

         bitmap = Bitmap.createScaledBitmap(
            bitmap,
            (largura).toInt(),
            (altura).toInt(),
            false
        )
    }
    var centerX = (rodaT.x + rodaF.x) / 2
    var centerY = (rodaT.y + rodaF.y) / 2
    var    pontoChassiFrente = pontoNoChassi(rodaF.x-(w*0.03f), centerY-(altura/1.8f), 60f, 30f, rotacao*-1.8f)
    var    pontoChassiTras = pontoNoChassi(rodaT.x+(w*0.08f), centerY-(altura/2.5f), -60f, 30f, rotacao*-1.8f)


    fun iniciarRodas() {
        rodaF.screenHeight = screenHeight
        rodaT.screenHeight= screenHeight



    }

    fun update(fundo: Bitmap,offset : Offset) {
        if(rodaF.x<rodaT.x){
            rodaF.x = rodaT.x+200
        }
        var lastTime = System.currentTimeMillis()
        val currentTime = System.currentTimeMillis()
        val deltaTime = currentTime - lastTime


    rodaF.update(deltaTime/ 1000f )
        rodaT.update(deltaTime/ 1000f )
        if(reduzindo){

            rodaF.reduzindo = true
            rodaT.reduzindo = true
            if(rodaT .velocidadedoGiro<=0){

                reduzindo = false
            }

        }

            rotacao = calcularRotacaoEntreRodas(rodaT.y, rodaF.y, rodaT.x, rodaF.x)

        verirficarColisao(fundo,offset, rodaF)
        verirficarColisao(fundo,offset, rodaT)
        ajustDraw()

    }

    fun update(fundo: Fundo,lastTimeMillis:Long) {

        val deltaTime = lastTimeMillis.toFloat()
        if(rodaF.x<rodaT.x){
            rodaF.x = rodaT.x+150
        }
   rodaF.update(deltaTime)
        rodaT.update(deltaTime)
        if(reduzindo){

            rodaF.reduzindo = true
            rodaT.reduzindo = true
            if(rodaT .velocidadedoGiro<=0){

                reduzindo = false
            }

        }

        if((fundo.mountainsSpeed<1 && rotacao > 0) && !estacionado){
            rotacao -=1f
            parou = true
            if(rotacao<0){
                rotacao =0f
            }

        }else if(!parou){
            rotacao = calcularRotacaoEntreRodas(rodaT.y, rodaF.y, rodaT.x, rodaF.x)*0.8f
        }

        verirficarColisao(fundo, rodaF)
        verirficarColisao(fundo, rodaT)
        ajustDraw()

    }

    @SuppressLint("SuspiciousIndentation")
    fun calcularRotacaoEntreRodas(yTraseira: Float, yDianteira: Float, xTraseira: Float, xDianteira: Float): Float {
        val deltaX = xDianteira - xTraseira
        val deltaY = yTraseira - yDianteira // importante: eixo Y é invertido
          return toDegrees(atan2(deltaY.toDouble(), deltaX.toDouble())).toFloat()
    }
    fun verirficarColisao(fundo: Bitmap,offset: Offset, roda: Roda) {


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
                fundo,
                offset.x.toInt(),
                offset.y.toInt()
            )

        ) {

            //  gameouver = true

            colidiu(roda)

        } else {
            roda.gravity = 5.0f

        }

    }

    private fun ajustDraw(){

        centerX = (rodaT.x + rodaF.x) / 2
        centerY = (rodaT.y + rodaF.y) / 2
        pontoChassiFrente = pontoNoChassi(rodaF.x-(w*0.02f), centerY-(altura/1.9f), w*0.03f, h*0.03f, -rotacao )
        pontoChassiTras = pontoNoChassi(rodaT.x+(w*0.08f), centerY-(altura/1.875f), -(w*0.03f), h*0.03f, -rotacao  )
        alturaY = pontoChassiTras.y
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

            colidiu(roda,fundo)

        }
        else if (colisao.colideComMapa(
                rect,
                fundo.backgroundMountains2,
                (fundo.mountainsX2.toInt()),
                fundo.mountainsY.toInt()
            )
        ) {

            //  gameouver = true
            colidiu(roda,fundo)
        }
        else {
            roda.gravity = 5.0f

        }

    }

    private fun colidiu(roda: Roda, fundo: Fundo? =null) {

        val terrainY = colisao.ultimoY
        if (roda.y + roda.altura > terrainY ) {
            roda.y = (terrainY - (roda.altura*0.8f)).toFloat()
            roda.gravity = 0f
            roda.velocityY = 0f
        }



    }
    fun pontoNoChassi(carroX: Float, carroY: Float, offsetX: Float, offsetY: Float, angulo: Float): PointF {
        val rad = Math.toRadians(angulo.toDouble())
        val x = carroX + offsetX * cos(rad) - offsetY * sin(rad)
        val y = carroY + offsetX * sin(rad) + offsetY * cos(rad)
        return PointF(x.toFloat(), y.toFloat())
    }
    val paintAmortecedor = Paint().apply {
        color = Color.DKGRAY
        strokeWidth = 30f
        style = Paint.Style.STROKE
    }
    val pp = Paint().apply{
        color = Color.DKGRAY
    }


    fun draw(canvas: Canvas) {


       if(rotacao*-1<=40 && rotacao*-1>=20) {

           canvas.save()
           canvas.rotate(rotacao * -0.2f, centerX, centerY)
           if(!garagem) {

               canvas!!.drawRoundRect(
                   RectF(
                       pontoChassiTras.x-(w*0.01f),
                       pontoChassiFrente.y-(w*0.01f),
                       pontoChassiTras.x+(w*0.02f),
                       pontoChassiFrente.y+(w*0.04f)
                   ), 40f, 40f, pp
               )
               canvas!!.drawRoundRect(
                   RectF(
                       pontoChassiTras.x+(w*0.01f),
                       pontoChassiFrente.y-(w*0.01f),
                       pontoChassiFrente.x+(w*0.02f),
                       pontoChassiFrente.y+(w*0.03f)
                   ), 40f, 40f, pp
               )



               canvas.drawLine(
                   pontoChassiTras.x,
                   pontoChassiTras.y,
                   (rodaT.x + (rodaT.largura * 0.5f)).toFloat(),
                   (rodaT.y + (rodaT.largura * 0.5f)).toFloat(),
                   paintAmortecedor
               )

               canvas.drawLine(
                   pontoChassiFrente.x,
                   pontoChassiFrente.y,
                   (rodaF.x + (rodaT.largura * 0.5f)).toFloat(),
                   (rodaF.y + (rodaT.largura * 0.5f)).toFloat(),
                   paintAmortecedor
               )
           }
           rodaT.draw(canvas)
           rodaF.draw(canvas)

           canvas.restore()
       }else{
           canvas!!.drawRoundRect(
               RectF(
                   pontoChassiTras.x-(w*0.01f),
                   pontoChassiFrente.y-(w*0.01f),
                   pontoChassiTras.x+(w*0.02f),
                   pontoChassiFrente.y+(w*0.04f)
               ), 40f, 40f, pp
           )
           canvas!!.drawRoundRect(
               RectF(
                   pontoChassiTras.x+(w*0.01f),
                   pontoChassiFrente.y-(w*0.01f),
                   pontoChassiFrente.x+(w*0.02f),
                   pontoChassiFrente.y+(w*0.03f)
               ), 40f, 40f, pp
           )
           canvas.drawLine(
               pontoChassiTras.x,
               pontoChassiTras.y,
               (rodaT.x + (rodaT.largura * 0.5f)).toFloat(),
               (rodaT.y + (rodaT.largura * 0.5f)).toFloat(),
               paintAmortecedor
           )

           canvas.drawLine(
               pontoChassiFrente.x,
               pontoChassiFrente.y,
               (rodaF.x + (rodaT.largura * 0.5f)).toFloat(),
               (rodaF.y + (rodaT.largura * 0.5f)).toFloat(),
               paintAmortecedor
           )
           rodaT.draw(canvas)
           rodaF.draw(canvas)
       }


        canvas.save()
        canvas.rotate(rotacao*-1.0f, centerX, centerY)
        if(!garagem) {
            canvas.drawBitmap(bitmap, rodaT.x, centerY - (altura ), null)
        }else{
            canvas.drawBitmap(bitmap, rodaT.x+(w*0.02f), centerY - (altura * 0.8f), null)

        }

        canvas.restore()


    }
    fun pulo() {

        rodaF.applyLift()
        rodaT.applyLift()


    }
    fun applyLift() {

    rodaF.velocidadedoGiro += 1
    rodaT.velocidadedoGiro += 1


    }

}