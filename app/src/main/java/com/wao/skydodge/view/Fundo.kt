package com.wao.skydodge.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas

import com.wao.skydodge.pistas.TrackRenderer


class Fundo(val context: Context) {

    var mountainsX = 0f
    var mountainsXR = 0f

    var mountainsY = 0f
    var mountainsSpeed = 0f
    var reduzindo = false
    var distancia = 0
    var index2 = 1
    var index = 0
    lateinit var backgroundMountains: Bitmap
    lateinit var backgroundMountains2: Bitmap
    lateinit var texturaBitmap: Bitmap
    val trackRenderer = TrackRenderer(context)

    var mountainsX2 = 0f
    fun update(trackRenderer: TrackRenderer) {


// No método de atualização:


        if (reduzindo) {
            mountainsSpeed -= mountainsSpeed * 0.1f
            if (mountainsSpeed <= 0) {
                mountainsSpeed = 0f
                reduzindo = false
            }
        } else if (mountainsSpeed > 90) {
            mountainsSpeed = 90f

        }



        mountainsX -= mountainsSpeed
        mountainsX2 -= mountainsSpeed
        mountainsXR -= mountainsSpeed

        trackRenderer.updateScroll(mountainsX)

        distancia += mountainsSpeed.toInt()

        if (mountainsX <= -(backgroundMountains.width)) {
            //   backgroundMountains = gerarBitmapOnduladoComTextura(context,(backgroundMountains.width).toInt(),backgroundMountains.height,texturaBitmap)
            index += 2
            if (index > 12) {
                index = 0
            }
            backgroundMountains = trackRenderer.trackSegments[index]
            //   backgroundMountains2 = drawTrack(canvas, track)
            mountainsX = mountainsX2 + backgroundMountains.width.toFloat()
            //   mountainsX = 0f

        }

        if (mountainsX2 <= -(backgroundMountains2.width)) {
            index2 += 2
            if (index2 > 11) {
                index2 = 1
            }
            backgroundMountains2 = trackRenderer.trackSegments[index2]
            mountainsX2 = mountainsX + backgroundMountains2.width.toFloat()
            // mountainsX2 = 0f


        }


    }

    fun draw(canvas: Canvas) {


        canvas.drawBitmap(
            backgroundMountains,
            mountainsX,
            mountainsY,
            null
        ) // 400f é um exemplo de altura
        canvas.drawBitmap(backgroundMountains2, mountainsX2, mountainsY, null)


        // ... desenha o restante do jogo
    }


}