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
    val tw = w/1.5f
    val th = w*0.35f
    var carro = Carro(context,w,h)
    var offset: Offset = Offset(0f, 0f)
    var listaMonters: MutableList<Bitmap> = mutableListOf()
    var listaPneus: MutableList<Bitmap> = mutableListOf()
    var sair = false
    var index = 0
    var indexP = 0
    var selectPneu = false

    val options = BitmapFactory.Options().apply {
        inPreferredConfig = Bitmap.Config.RGB_565
    }
    var fundo: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.base,options)
    var oficina: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.fundooficina,options)
    var esquerda: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.btl,options)
    var direita: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.btr,options)
    var start: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.start,options)
    var pneu: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.pneu,options)
    var truck: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.truck,options)


    var loja: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.loja,options)


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
        carro.rodaT.x = (w / 2) -75f
        carro.estacionado = true
        carro.rodaT.y = (h * 0.4f)
        carro.rodaF.y = (h * 0.5f)
        carro.rodaF.x = carro.rodaT.x+(tw*0.085f)

        carro.garagem= true
        carregarlita()
        carro.bitmap = listaMonters[index]

        var nx: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.nuven,options)
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

        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassia,options))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassib,options))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassic,options))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassid,options))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassie,options))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassif,options))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassig,options))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassih,options))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassii,options))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassij,options))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassik,options))
        inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassil,options))
        //  inserirIMG(BitmapFactory.decodeResource(context.resources, R.drawable.chassim))

        inserirIMGRoda(BitmapFactory.decodeResource(context.resources, R.drawable.rodac,options))
        inserirIMGRoda(BitmapFactory.decodeResource(context.resources, R.drawable.rodab,options))
        inserirIMGRoda(BitmapFactory.decodeResource(context.resources, R.drawable.rodaa,options))
        inserirIMGRoda(BitmapFactory.decodeResource(context.resources, R.drawable.rodad,options))

    }
    fun getRoda() : Bitmap{
       return  Bitmap.createScaledBitmap(
            listaPneus[indexP],
            (carro.rodaT.altura).toInt(),
            (carro.rodaT.altura).toInt(),
            false
        )

    }
    fun getRoda(i:Int) : Bitmap{
        return  Bitmap.createScaledBitmap(
            listaPneus[i],
            (carro.rodaT.altura).toInt(),
            (carro.rodaT.altura).toInt(),
            false
        )

    }
    fun inserirIMGR(bitmap: Bitmap):Bitmap {

        val tw = carro.largura/1.5f
        val th = carro.largura*0.35f
        return   Bitmap.createScaledBitmap(
            bitmap,
            (tw).toInt(),
            -(th).toInt(),
            false
        )

    }
    fun inserirIMG(bitmap: Bitmap) {

        val tw = carro.largura/1.5f
        val th = carro.largura*0.35f
        var bitmapx = Bitmap.createScaledBitmap(
            bitmap,
            (tw).toInt(),
            -(th).toInt(),
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