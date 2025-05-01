package com.wao.skydodge.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.util.Random
import kotlin.math.sin

class Fundo (val context: Context){
    // Em GameView
    var skyX = 0f
    var cloudsX = 0f
    var mountainsX = 0f


    var mountainsY = 0f
    val skySpeed = 1f
    val cloudsSpeed = 3f
    var mountainsSpeed = 0f
    var reduzindo = false
    var distancia = 0
    lateinit var backgroundSky: Bitmap
    lateinit var backgroundClouds: Bitmap
    lateinit var backgroundMountains: Bitmap
    lateinit var backgroundMountains2: Bitmap
    lateinit var texturaBitmap: Bitmap


    var mountainsX2 = 0f
    var yBaseAux = 400f
    fun update() {

        skyX -= skySpeed
        cloudsX -= cloudsSpeed


        if(reduzindo){
            mountainsSpeed-= mountainsSpeed*0.1f
            if(mountainsSpeed<=0){
                mountainsSpeed=0f
                reduzindo = false
            }
        }else if(mountainsSpeed>90){
                mountainsSpeed=90f

            }



        mountainsX -= mountainsSpeed
        mountainsX2 -= mountainsSpeed
        distancia+=mountainsSpeed.toInt()
        if (skyX <= -backgroundSky.width) {
            skyX = 0f
        }
        if (cloudsX <= -backgroundClouds.width) {
            cloudsX = 0f
        }
        if (mountainsX <= -(backgroundMountains.width)) {
            backgroundMountains = gerarBitmapOnduladoComTextura(context,(backgroundMountains.width).toInt(),backgroundMountains.height,texturaBitmap)
            mountainsX = mountainsX2+backgroundMountains.width.toFloat()

        }

        if (mountainsX2 <= -(backgroundMountains2.width)) {
            backgroundMountains2 = gerarBitmapOnduladoComTextura(context,(backgroundMountains2.width).toInt(),backgroundMountains.height,texturaBitmap)
            mountainsX2 = mountainsX+backgroundMountains2.width.toFloat()

        }




    }

      fun draw(canvas: Canvas) {

          canvas.drawBitmap(backgroundSky, skyX, 0f, null)
          canvas.drawBitmap(backgroundSky, skyX + backgroundSky.width, 0f, null)

          // Desenha as nuvens
          canvas.drawBitmap(backgroundClouds, cloudsX, 100f, null) // 100f é um exemplo de altura
          canvas.drawBitmap(backgroundClouds, cloudsX + backgroundClouds.width, 100f, null)

          // Desenha as montanhas

          canvas.drawBitmap(backgroundMountains, mountainsX, mountainsY, null) // 400f é um exemplo de altura
         canvas.drawBitmap(backgroundMountains2, mountainsX2, mountainsY, null)

        // ... desenha o restante do jogo
    }

    fun gerarBitmapOnduladoComTextura(context: Context, largura: Int, altura: Int, texturaBitmap: Bitmap): Bitmap {
        val bitmap = Bitmap.createBitmap(largura, altura, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.Transparent.toArgb())

        val random = java.util.Random()
        val linhas = 1

        // Criar o shader da textura
        val shader = BitmapShader(texturaBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)

        val paintFill = Paint().apply {
            shader?.let { this.shader = it }
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val paintStroke = Paint().apply {
            color = Color.Black.toArgb()
            style = Paint.Style.STROKE
            strokeWidth =10f
            isAntiAlias = true
        }

        for (i in 0 until linhas) {
         //   var yBase = altura / (linhas + 1) * (i + 1)
            val amplitude = random.nextInt(230) + 10
            val ciclos = 2 + random.nextInt(2)
            val freq = (2 * Math.PI * ciclos) / largura
            var yBase = 0
            if(i==0){
                yBase = (altura *0.5f).toInt()
            }else{
                yBase = 0

            }
            val path = Path()
            path.moveTo(0f, yBase.toFloat())



            var lastX = 0f
            var lastY = yBase.toFloat()

            for (x in 0..largura step 20) {
                val y = yBase + sin(x * freq).toFloat() * amplitude
                val midX = (lastX + x) / 2
                val midY = (lastY + y) / 2
                path.quadTo(lastX, lastY, midX, midY)
                lastX = x.toFloat()
                lastY = y.toFloat()
            }

//            path.lineTo(largura.toFloat(), (yBase + 100).toFloat())
//            path.lineTo(0f, (yBase + 100).toFloat())
//            path.close()

            if(i==1){
                val num = (altura/3)
                path.lineTo((largura-100).toFloat(), (yBase - num).toFloat())
                path.lineTo(0f, (yBase - num).toFloat())
            }else{
                val num = (altura/2)
                path.lineTo((largura+100).toFloat(), (yBase + num).toFloat())
                path.lineTo(0f, (yBase + num).toFloat())
            }
            canvas.drawPath(path, paintFill)
            canvas.drawPath(path, paintStroke)
        }

        return bitmap
    }



}