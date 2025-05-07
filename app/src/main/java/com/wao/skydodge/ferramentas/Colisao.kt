package com.wao.skydodge.ferramentas

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect

class Colisao() {

    var ultimoY = 0f
   // var modo = ModoDeslise.P
   @SuppressLint("SuspiciousIndentation")
   fun colideComMapa(rect: Rect, mapa: Bitmap, offsetX: Int, offsetY: Int): Boolean {
        for (y in rect.top until rect.bottom) {
            for (x in rect.left until rect.right) {
                var mapaX = x +( offsetX*-1)

                val mapaY = y - offsetY

                    if (mapaX in 0 until mapa.width && mapaY in 0 until mapa.height) {
                        if (Color.alpha(mapa.getPixel(mapaX, mapaY)) > 50) {
                            ultimoY=mapaY.toFloat()
                          //  modo = detectarDirecaoDoTerreno(mapa, x, y)
                                return true

                        }
                    }


            }
        }
        return false
    }


    fun detectarDirecaoDoTerreno(bitmapColisao: Bitmap, x: Int, y: Int): ModoDeslise {
        // Verifica a altura do solo no ponto x
        val alturaAtual = encontrarAlturaDoChao(bitmapColisao, x, y)
        val alturaFrente = encontrarAlturaDoChao(bitmapColisao, x + 1, y)

        return when {
            alturaFrente> alturaAtual+3  -> {
                ModoDeslise.S
            }
            alturaFrente< alturaAtual+3 -> {
                ModoDeslise.D
            }
            else ->  ModoDeslise.P
        }
    }

    // Encontra a altura do chão no eixo Y baseado na cor
    fun encontrarAlturaDoChao(bitmap: Bitmap, x: Int, yInicial: Int): Int {
        for (i in yInicial until bitmap.height) {
            val cor = bitmap.getPixel(x.coerceIn(0, bitmap.width - 1), i)
            if (cor != Color.TRANSPARENT) return i
        }
        return bitmap.height // caso não encontre colisão
    }

}

