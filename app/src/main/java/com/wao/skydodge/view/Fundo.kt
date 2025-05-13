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

    var bateu = false
    var distancia = 0
    var index2 = 1
    var index = 0
    lateinit var backgroundMountains: Bitmap
    lateinit var backgroundMountains2: Bitmap
    lateinit var texturaBitmap: Bitmap
    val trackRenderer = TrackRenderer(context)
    var trackOffsetX = 0f
    var mountainsX2 = 0f
    fun update(trackRenderer: TrackRenderer, lastTimeMillis: Long) {
        val deltaTime = lastTimeMillis / 100f // converte para segundos
         if (reduzindo) {
            mountainsSpeed -= mountainsSpeed * 0.1f
            if (mountainsSpeed <= 0) {
                mountainsSpeed = 0f
                reduzindo = false
            }
        } else if (mountainsSpeed > 90f) {
            mountainsSpeed = 90f
        }

        if (bateu) {
            mountainsSpeed += 0.5f
            if (mountainsSpeed >= 0f) {
                bateu = false
            }
        }

        val displacement = mountainsSpeed * deltaTime

        // Atualiza os deslocamentos com base no tempo real
        trackOffsetX -= displacement
        mountainsX -= displacement
        mountainsX2 -= displacement
        mountainsXR -= displacement

        trackRenderer.updateScroll(mountainsX)

        distancia += displacement.toInt()

        if (mountainsX <= -backgroundMountains.width) {
            index += 2
            if (index > 12) index = 0
            backgroundMountains = trackRenderer.trackSegments[index]
            mountainsX = mountainsX2 + backgroundMountains.width.toFloat()
        }

        if (mountainsX2 <= -backgroundMountains2.width) {
            index2 += 2
            if (index2 > 11) index2 = 1
            backgroundMountains2 = trackRenderer.trackSegments[index2]
            mountainsX2 = mountainsX + backgroundMountains2.width.toFloat()
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