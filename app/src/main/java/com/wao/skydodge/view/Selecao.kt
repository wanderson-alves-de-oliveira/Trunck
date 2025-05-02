package com.wao.skydodge.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.wao.skydodge.R
import com.wao.skydodge.egine.Carro
import com.wao.skydodge.egine.CarroAmostra

class Selecao(val context: Context, val w: Int, val h: Int) {

    var carro = CarroAmostra(context)
    var listaMonters: MutableList<Bitmap> = mutableListOf()
var index = 0
    init {
        carro.rodaT.x = (w / 2).toFloat() + (carro.largura / 2)
        carro.rodaF.x = carro.rodaT.x + 180f

        carro.rodaF.y = (h / 2).toFloat() - (carro.altura / 2)
        carro.rodaT.y = (h / 2).toFloat() - (carro.altura / 2)

carro.rotacao=0f
        carregarlita()
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



        carro.draw(canvas)
    }

    fun onTouchEvent(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            index++

            if (index >= listaMonters.size) {
                index = 0
            }
            carro.bitmap = listaMonters[index]


        }
    }
}