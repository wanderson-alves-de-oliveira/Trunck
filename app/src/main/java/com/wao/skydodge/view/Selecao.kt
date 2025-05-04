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
    var listaPneus: MutableList<Bitmap> = mutableListOf()
    var sair = false
    var index = 0
    var indexP = 0
    var selectPneu = false


    var fundo: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.base)
    var oficina: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.fundooficina)
    var esquerda: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.btl)
    var direita: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.btr)
    var start: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.start)
    var pneu: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pneu)
    var truck: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.truck)
    var loja: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.loja)


    var btmL = BotaoBitmap(
        this.context,
        ((this.w * 0.1)).toFloat(),
        (this.h * 0.7).toFloat(),
        (this.w * 0.15).toInt(),
        (this.w * 0.12).toInt(),
        esquerda
    )
    var btmR = BotaoBitmap(
        this.context,
        ((this.w * 0.8)).toFloat(),
        (this.h * 0.7).toFloat(),
        (this.w * 0.12).toInt(),
        (this.w * 0.12).toInt(),
        direita
    )
    var btmStart = BotaoBitmap(
        this.context,
        ((this.w * 0.8)).toFloat(),
        (this.h * 0.2).toFloat(),
        (this.w * 0.12).toInt(),
        (this.w * 0.1).toInt(),
        start
    )
    var btmPneu = BotaoBitmap(
        this.context,
        ((this.w * 0.7)).toFloat(),
        (this.h * 0.3).toFloat(),
        (this.w * 0.08).toInt(),
        (this.w * 0.08).toInt(),
        pneu
    )
    var btmLoja = BotaoBitmap(
        this.context,
        ((this.w * 0.6)).toFloat(),
        (this.h * 0.2).toFloat(),
        (this.w * 0.08).toInt(),
        (this.w * 0.08).toInt(),
        loja
    )


    init {

        carro.rotacao = 0f
        carro.rodaT.x = (w / 2) + carro.largura / 2
        carro.estacionado = true
        carro.rodaT.y = (h * 0.4f)
        carro.rodaF.y = (h * 0.5f)
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
            (w / 2).toInt(),

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
        direita = Bitmap.createScaledBitmap(
            direita,
            (w * 0.15).toInt(),

            (w * 0.15).toInt(),
            false
        )

        offset = Offset(((w / 2) - fundo.width / 2).toFloat(), 0f)


    }

    fun update() {


        carro.update(fundo, offset)

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
        //  inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassim))

        inserirIMGRoda(BitmapFactory.decodeResource(context.resources, R.drawable.rodac))
        inserirIMGRoda(BitmapFactory.decodeResource(context.resources, R.drawable.rodab))
        inserirIMGRoda(BitmapFactory.decodeResource(context.resources, R.drawable.rodaa))
        inserirIMGRoda(BitmapFactory.decodeResource(context.resources, R.drawable.rodad))

    }
    fun getRoda() : Bitmap{
       return  Bitmap.createScaledBitmap(
            listaPneus[indexP],
            (carro.rodaT.altura).toInt(),
            (carro.rodaT.altura).toInt(),
            false
        )

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

    fun inserirIMGRoda(bitmap: Bitmap) {
        var bitmapx = Bitmap.createScaledBitmap(
            bitmap,
            (carro.rodaT.altura*3).toInt(),
            (carro.rodaT.altura*3).toInt(),
            false
        )
        listaPneus.add(bitmapx)
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
        canvas.drawBitmap(oficina, 0f, 0f, null)

        canvas.drawBitmap(fundo, offset.x, offset.y, null)

        if (!selectPneu) {
            carro.draw(canvas)


        } else {
            canvas.drawBitmap(listaPneus[indexP], ((this.w /2)-listaPneus[indexP].width/2).toFloat(), (this.h * 0.3).toFloat(), null)

        }

        btmL.draw(canvas)
        btmR.draw(canvas)
        btmPneu.draw(canvas)
        btmStart.draw(canvas)
        btmLoja.draw(canvas)

    }

    fun onTouchEvent(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {

            if (btmStart.containsTouch(
                    event.x,
                    event.y
                )
            ) {
                sair = true
            } else {
                if (btmR.containsTouch(
                        event.x,
                        event.y
                    )
                ) {
                    if (selectPneu) {
                        indexP++

                    } else {
                        index++
                    }


                } else if (btmL.containsTouch(
                        event.x,
                        event.y
                    )
                ) {
                    if (selectPneu) {
                        indexP--

                    } else {
                        index--
                    }
                }else if (btmPneu.containsTouch(
                        event.x,
                        event.y
                    )
                ) {
                   if(selectPneu){
                       selectPneu = false
                       btmPneu = BotaoBitmap(
                           this.context,
                           ((this.w * 0.7)).toFloat(),
                           (this.h * 0.3).toFloat(),
                           (this.w * 0.08).toInt(),
                           (this.w * 0.08).toInt(),
                           pneu
                       )
                   }else{
                       selectPneu = true
                       btmPneu = BotaoBitmap(
                           this.context,
                           ((this.w * 0.7)).toFloat(),
                           (this.h * 0.3).toFloat(),
                           (this.w * 0.08).toInt(),
                           (this.w * 0.08).toInt(),
                           truck
                       )
                   }
                }
                carro.rodaT.y = -100f
                carro.rodaF.y = -100f
                carro.rodaT.velocityY = 140f
                carro.rodaF.velocityY = 150f
                if (!selectPneu) {
                    if (index >= listaMonters.size) {
                        index = 0
                    } else if (index < 0) {
                        index = listaMonters.size - 1
                    }



                } else {
                    if (indexP >= listaPneus.size) {
                        indexP = 0
                    } else if (indexP < 0) {
                        indexP = listaPneus.size - 1
                    }


                    val bitmapx = Bitmap.createScaledBitmap(
                        listaPneus[indexP],
                        (carro.rodaT.altura).toInt(),
                        (carro.rodaT.altura).toInt(),
                        false
                    )
                    carro.rodaT.bitmap = bitmapx
                    carro.rodaF.bitmap = bitmapx


                }



                carro.bitmap = listaMonters[index]



            }
        }
    }
}