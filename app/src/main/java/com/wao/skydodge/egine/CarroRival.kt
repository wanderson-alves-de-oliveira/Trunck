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
    var velocidadeX = 0f
    var velocidadeR = 0f
    var rotacao = 0f
    var largura = 0f
    var altura = 150f
    var bitmap: Bitmap =  BitmapFactory.decodeResource(context.resources, R.drawable.chassia)
    var alturaY = 150f
    private val display: DisplayMetrics = context.resources.displayMetrics
    private val h = display.heightPixels
    private val w = display.widthPixels
   val  f= 800f
    val t = 600f
    init {
        rodaF.x = 800f
        rodaT.x = 600f

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
    fun update(fundo: Bitmap,offset : Offset) {

        rodaF.update()
        rodaT.update()

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

    }
    fun update(fundo: Fundo) {

        if(inicio) {
          verificarPosiçãoPista(fundo)

            if(reduzindoR){
                if(velocidadeX>velocidadeR){
                    velocidadeX-=2

                }else{

                    reduzindoR=false
                }

            }else{
                velocidadeX+=2
                if(velocidadeX>140){
                    velocidadeR=( (125..140).random()).toFloat()
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

    }

    private fun verificarPosiçãoPista(fundo: Fundo) {

        rodaF.x=fundo.mountainsXR+(f+km)
        rodaT.x=fundo.mountainsXR+(t+km)
        if(rodaF.x> fundo.backgroundMountains.width||
            rodaT.x+(fundo.backgroundMountains.width/2)<0f){
            rodaT.gravity = 0f
            rodaF.gravity = 0f
            rodaT.y = 50f
            rodaF.y = 50f

        }else{
            rodaT.gravity = 3.0f
            rodaF.gravity = 3.0f
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
            roda.gravity = 3.0f

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
            roda.gravity = 3.0f

        }

    }

    private fun colidiu(roda: Roda) {

        val terrainY = colisao.ultimoY
        if (roda.y + roda.altura > terrainY ) {
            roda.y = terrainY - (roda.altura*0.8f)
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

    fun draw(canvas: Canvas) {

        if(rodaF.x<rodaT.x){
            rodaF.x = rodaT.x+200
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
       val pontoChassiFrente = pontoNoChassi(rodaF.x-(w*0.03f), centerY-(altura/1.8f), 60f, 30f, rotacao*-1.8f)
       val  pontoChassiTras = pontoNoChassi(rodaT.x+(w*0.08f), centerY-(altura/2.5f), -60f, 30f, rotacao*-1.8f)
        alturaY = pontoChassiTras.y

       if(rotacao*-1<=50) {

           canvas.save()
           canvas.rotate(rotacao * -1, centerX, centerY)
           canvas.drawLine(
               pontoChassiTras.x,
               pontoChassiTras.y,
               rodaT.x + (rodaT.largura * 0.5f),
               rodaT.y + (rodaT.largura * 0.5f),
               paintAmortecedor
           )

           canvas.drawLine(
               pontoChassiFrente.x,
               pontoChassiFrente.y,
               rodaF.x + (rodaT.largura * 0.5f),
               rodaF.y + (rodaT.largura * 0.5f),
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
        canvas.rotate(rotacao*-1.8f, centerX, centerY)

        canvas.drawBitmap(bitmap, rodaT.x, centerY-(altura*0.8f), null)


        canvas.restore()


    }

    fun applyLift() {

    rodaF.velocidadedoGiro += 1
    rodaT.velocidadedoGiro += 1


    }

}