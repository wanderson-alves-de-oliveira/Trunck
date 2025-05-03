package com.wao.skydodge.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.wao.skydodge.R
import com.wao.skydodge.egine.Carro

class Selecao(val context: Context, val w: Int, val h: Int) {

    var carro = Carro(context)
    var offset: Offset = Offset(0f, 0f)
    var listaMonters: MutableList<Bitmap> = mutableListOf()
    var sair = false
    var index = 0

    var fundo: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.base)
    var oficina: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.fundooficina)
    var esquerda: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.bt)


    var btmL = BotaoBitmap(
        this.context,
        ((this.w * 0.8)).toFloat(),
        (this.h * 0.7).toFloat(),
        (this.w * 0.12).toInt(),
        (this.w * 0.12).toInt(),
        esquerda
        )
    var btmR = BotaoBitmap(
        this.context,
        ((this.w * 0.1)).toFloat(),
        (this.h * 0.7).toFloat(),
        (-this.w * 0.12).toInt(),
        (this.w * 0.12).toInt(),
        esquerda
    )
    init {

        carro.rotacao=0f
        carro.rodaT.x=(w/2)+carro.largura/2
        carro.estacionado = true
        carro.rodaT.y=-100f
        carro.rodaF.y=-100f
        carregarlita()


        var nx: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.nuven)
        var n = Bitmap.createScaledBitmap(
            nx,
            (w).toInt(),
            (h).toInt(),
            false
        )




        fundo = Bitmap.createScaledBitmap(
            fundo,
            (w/2).toInt(),

            (h).toInt(),
            false
        )
        oficina = Bitmap.createScaledBitmap(
            oficina,
            (w * 1.20).toInt(),

            (h).toInt(),
            false
        )


        esquerda = Bitmap.createScaledBitmap(
            esquerda,
            (w * 0.15).toInt(),

            (w * 0.15).toInt(),
            false
        )
        offset =  Offset( ((w/2)-fundo.width/2).toFloat(),0f)


    }

    fun update() {


        carro.update(fundo,offset)

    }
    private fun carregarlita() {

        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassia))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassib))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassic))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassid))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassie))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassif))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassig))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassih))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassii))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassij))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassik))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassil))


    }

    fun inserirIMG(bitmap: Bitmap) {
        var bitmapx = Bitmap.createScaledBitmap(
            bitmap,
            (carro.largura).toInt(),
            (carro.altura).toInt(),
            false
        )
        listaMonters.add(bitmapx)
    }


    fun draw(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.White.toArgb()
//        val shader = BitmapShader(coinp!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
//        paint.shader = shader
        canvas?.drawRoundRect(
            0f,
            0f,
            (w * 1.20).toFloat(),
            h.toFloat(),
            0f,
            0f,
            paint
        )
        canvas.drawBitmap(oficina,  0f,0f, null)

        canvas.drawBitmap(fundo,  offset.x,offset.y, null)


        carro.draw(canvas)

        btmL.draw(canvas)
        btmR.draw(canvas)
    }

    fun onTouchEvent(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {

            if (btmL.containsTouch(
                    event.x,
                    event.y
                )
            ) {
                sair = true
            }else {

                index++
                carro.rodaT.y = -100f
                carro.rodaF.y = -100f
                carro.rodaT.velocityY = 140f
                carro.rodaF.velocityY = 150f

                if (index >= listaMonters.size) {
                    index = 0
                }
                carro.bitmap = listaMonters[index]

            }
        }
    }
}