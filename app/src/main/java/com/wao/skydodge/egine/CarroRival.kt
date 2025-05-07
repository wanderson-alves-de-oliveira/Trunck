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

class CarroRival(context: Context) {
    var colisao = Colisao()
      var rodaF = Roda(context)
      var rodaT = Roda(context)
     var screenHeight = 0
    var reduzindo = false
    var reduzindoR = false
    var parou = false
 var estacionado = false
   var  inicio = false
    var km = 0f
    var velocidadeX = 70f
    var velocidadeR = 0f
    var rotacao = 0f
    var largura = 0f
    var altura = 112.5f
    val options = BitmapFactory.Options().apply {
        inPreferredConfig = Bitmap.Config.RGB_565
    }
    var bitmap: Bitmap =  BitmapFactory.decodeResource(context.resources, R.drawable.chassia,options)
    var alturaY = 112.5f
    private val display: DisplayMetrics = context.resources.displayMetrics
    private val h = display.heightPixels
    private val w = display.widthPixels
   var  f= 800f
    var t = 650f
    init {
        rodaF.x = f
        rodaT.x = t

         largura = (rodaT.x - (rodaF.x+(rodaT.largura*1.05f))).toFloat()
         altura = 112.5f

         bitmap = Bitmap.createScaledBitmap(
            bitmap,
            (largura).toInt(),
            (altura).toInt(),
            false
        )
    }

fun moverX(vel : Float){
    rodaF.x+=vel
    rodaT.x+=vel

    rodaF.velocidadedoGiro += 1
    rodaT.velocidadedoGiro += 1
}
    fun iniciarRodas() {
        rodaF.screenHeight
        rodaT.screenHeight
    }

    var centerX = (rodaT.x + rodaF.x) / 2
    var centerY = (rodaT.y + rodaF.y) / 2
    var    pontoChassiFrente = pontoNoChassi(rodaF.x-(w*0.03f), centerY-(altura/1.8f), 60f, 30f, rotacao*-1.8f)
    var    pontoChassiTras = pontoNoChassi(rodaT.x+(w*0.08f), centerY-(altura/2.5f), -60f, 30f, rotacao*-1.8f)


    fun update(fundo: Bitmap,offset : Offset) {

        rodaF.update()
        rodaT.update()
        if(rodaF.x<rodaT.x){
            rodaF.x = rodaT.x+150
        }

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
    fun update(fundo: Fundo) {

        if(inicio) {
            if(rodaF.x<rodaT.x){
                rodaF.x = rodaT.x+150
            }



            verificarPosiçãoPista(fundo)

            if(reduzindoR){
                if(velocidadeX>velocidadeR){
                    velocidadeX-=1

                }else{

                    reduzindoR=false
                }

            }else{
                velocidadeX+=1
                if(velocidadeX>90){
                    velocidadeR=( (70..90).random()).toFloat()
                    reduzindoR=true
                }
            }


            moverX(velocidadeX)
            km += velocidadeX

        }
        rodaF.update()
        rodaT.update()
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
            rotacao = calcularRotacaoEntreRodas(rodaT.y, rodaF.y, rodaT.x, rodaF.x)
        }

        verirficarColisao(fundo, rodaF)
        verirficarColisao(fundo, rodaT)
        ajustDraw()
    }

      fun verificarPosiçãoPista(fundo: Fundo) {

        rodaF.x=fundo.mountainsXR+(f+km)
        rodaT.x=fundo.mountainsXR+(t+km)
        if(rodaF.x> fundo.backgroundMountains.width||
            rodaT.x+(fundo.backgroundMountains.width/2)<0f){
            rodaT.gravity = 0f
            rodaF.gravity = 0f
            rodaT.y = 50f
            rodaF.y = 50f

        }else{
            rodaT.gravity = 4.0f
            rodaF.gravity = 4.0f
        }


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
            roda.gravity = 4.0f

        }

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

        }
        else if (colisao.colideComMapa(
                rect,
                fundo.backgroundMountains2,
                (fundo.mountainsX2.toInt()),
                fundo.mountainsY.toInt()
            )
        ) {

            //  gameouver = true
            colidiu(roda)
        }
        else {
            roda.gravity = 4.0f

        }

    }

    private fun colidiu(roda: Roda) {

        val terrainY = colisao.ultimoY
        if (roda.y + roda.altura > terrainY ) {
            roda.y = (terrainY - (roda.altura*0.8f)).toFloat()
            roda.gravity = 0f
            roda.velocityY = 0f
        }


    }

    private fun ajustDraw(){

        centerX = (rodaT.x + rodaF.x) / 2
        centerY = (rodaT.y + rodaF.y) / 2
        pontoChassiFrente = pontoNoChassi(rodaF.x-(w*0.02f), centerY-(altura/1.9f), 60f, 30f, rotacao *0.16f)
        pontoChassiTras = pontoNoChassi(rodaT.x+(w*0.08f), centerY-(altura/1.875f), -60f, 30f, rotacao *0.2f)
        alturaY = pontoChassiTras.y
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
    fun draw(canvas: Canvas) {




       if(rotacao*-1<=50) {

           canvas.save()
           canvas.rotate(rotacao * -0.2f, centerX, centerY)
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

           canvas.restore()
       }else{
           rodaT.draw(canvas)
           rodaF.draw(canvas)
       }


        canvas.save()
        canvas.rotate(rotacao*-1.0f, centerX, centerY)

        canvas.drawBitmap(bitmap, rodaT.x, centerY-(altura*0.8f), null)


        canvas.restore()


    }

    fun applyLift() {

    rodaF.velocidadedoGiro += 1
    rodaT.velocidadedoGiro += 1


    }

}