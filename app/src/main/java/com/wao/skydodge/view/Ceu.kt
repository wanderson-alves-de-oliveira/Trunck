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
import com.wao.skydodge.pistas.TrackRenderer

import kotlin.math.sin

class Ceu (val context: Context){
    // Em GameView
    var skyX = 0f
    var cloudsX = 0f


     var skySpeed = 1f
    var cloudsSpeed = 3f
     var corremdo = false
    var distancia = 0
    lateinit var backgroundSky: Bitmap
    lateinit var backgroundClouds: Bitmap

    val trackRenderer = TrackRenderer(context)

    var yBaseAux = 400f
    fun update() {
if(corremdo) {
    skyX -= skySpeed
    cloudsX -= cloudsSpeed
}





        if (skyX <= -backgroundSky.width) {
            skyX = 0f
        }
        if (cloudsX <= -backgroundClouds.width) {
            cloudsX = 0f
        }





    }

      fun draw(canvas: Canvas) {

          canvas.drawBitmap(backgroundSky, skyX, 0f, null)
          canvas.drawBitmap(backgroundSky, skyX + backgroundSky.width, 0f, null)

          // Desenha as nuvens
          canvas.drawBitmap(backgroundClouds, cloudsX, 100f, null) // 100f é um exemplo de altura
          canvas.drawBitmap(backgroundClouds, cloudsX + backgroundClouds.width, 100f, null)



        // ... desenha o restante do jogo
    }




}