package com.wao.skydodge.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas

 import com.wao.skydodge.R

private const val fl = 9.0f

class Roda(context: Context,
           var largura :Float= 112.5f,
           var altura :Float= 112.5f) {
    var reduzindo = false
    var x = 0f
    var y =  0f

     var giro = 0f
    private var speed = 20f
    var velocidadedoGiro = 0f
    var velocityY = 0f  // Velocidade vertical (gravidade)
    var gravity = 5.0f  // Constante de gravidade

    // Levantamento para impulsionar para cima
    val options = BitmapFactory.Options().apply {
        inPreferredConfig = Bitmap.Config.RGB_565
    }
    private var bitmapx: Bitmap =  BitmapFactory.decodeResource(context.resources, R.drawable.rodac,options)

    var bitmap = Bitmap.createScaledBitmap(
    bitmapx,
    (largura).toInt(),
    (altura).toInt(),
    false
    )
    var screenHeight =0
    private var lift = -9f
    init {
        // Redimensiona a imagem do avião para o tamanho padrão
        bitmap = resizeBitmap(bitmap, (largura).toInt(), (altura).toInt())
         lift = -(altura*0.70f)
    }



    // Método chamado para aplicar o impulso para cima
    fun applyLift() {
        velocityY = lift
        gravity = 5.0f
        //giroUP()
    }

    fun giroUP() {

        if(giro>-30) {
            giro -= velocidadedoGiro
        }
    }

    // Atualiza a posição do avião com base na velocidade e na gravidade
    fun update(deltaTime: Float) {
        // Aplica a gravidade: aumenta a velocidade vertical

        velocityY += gravity

        // Atualiza a posição vertical com base na velocidade
        y += velocityY

        if(reduzindo){
            velocidadedoGiro-=  0.5f
            if(velocidadedoGiro<=0){
                velocidadedoGiro=0f
                reduzindo = false
            }
        }

        if(velocidadedoGiro>30){
            velocidadedoGiro = 30f
        }
        if(giro<360) {
            giro += velocidadedoGiro
        }else{
            giro = velocidadedoGiro
        }
        // Impede que o avião caia para fora da tela (evitar que saia da tela para baixo)
        if (y > screenHeight - bitmap.height && screenHeight>0) {
            y = screenHeight - bitmap.height.toFloat()
            velocityY = 0f  // Para de cair ao atingir o fundo
        }

    }

    fun moveLeft() {
        x -= speed
    }

    fun moveRight() {
        x += speed
    }

    fun draw(canvas: Canvas) {



        // Salvando o estado do canvas
        canvas.save()

        // Digamos que você queira rotacionar em torno do centro do player
        val centerX = x +  bitmap.width / 2
        val centerY =  y +  bitmap.height / 2

        // 1. Translada o canvas para o centro do objeto
        canvas.translate(centerX, centerY)

        // 2. Rotaciona
        canvas.rotate(giro)

        // 3. Desenha o bitmap centralizado
        canvas.drawBitmap(bitmap,  (-bitmap.width / 2).toFloat(), (-bitmap.height / 2).toFloat(), null)

        // 4. Restaura o canvas
        canvas.restore()
    }


    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }
}
