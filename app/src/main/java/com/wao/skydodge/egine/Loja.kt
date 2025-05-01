package com.wao.skydodge.egine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.RectF
import android.util.TypedValue
import android.view.MotionEvent
 import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.wao.skydodge.R
import com.wao.skydodge.db.BDSky
import com.wao.skydodge.view.BotaoM
import com.wao.skydodge.view.GameView
import com.wao.skydodge.view.ItemLoja


class Loja(val context: Context, val gameView: GameView, val w: Int, val h: Int) {

    private val paint = Paint()
    var semanuncio = false

    val bd = BDSky(context)
    var base = BDSky(context).buscar()

    var abrirLoja = false
    var atualizar = false

    var wal = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(
            context.resources,
            R.drawable.muralha
        ), (w), (this.h * 1.2f).toInt(), false
    )

    var moedas = -1
    val coin = BitmapFactory.decodeResource(context.resources, R.drawable.moeda)
    val coinP = Bitmap.createScaledBitmap(
        coin,
        ((w * 0.08f)).toInt(),
        ((w * 0.08f)).toInt(),
        false
    )

    val coin5 = BitmapFactory.decodeResource(context.resources, R.drawable.coinscinco)
    val coin5P = Bitmap.createScaledBitmap(
        coin5,
        ((w * 0.08f)).toInt(),
        ((w * 0.08f)).toInt(),
        false
    )
    val coin10 = BitmapFactory.decodeResource(context.resources, R.drawable.coinsdez)
    val coin10P = Bitmap.createScaledBitmap(
        coin10,
        ((w * 0.08f)).toInt(),
        ((w * 0.08f)).toInt(),
        false
    )
    val noads = BitmapFactory.decodeResource(context.resources, R.drawable.noads)
    val noadsP = Bitmap.createScaledBitmap(
        noads,
        ((w * 0.08f)).toInt(),
        ((w * 0.08f)).toInt(),
        false
    )
    val imgluz = BitmapFactory.decodeResource(context.resources, R.drawable.lampada)
    val imgluzP = Bitmap.createScaledBitmap(
        imgluz,
        ((w * 0.08f)).toInt(),
        ((w * 0.08f)).toInt(),
        false
    )

    val imgima = BitmapFactory.decodeResource(context.resources, R.drawable.ima)
    val imgimaP = Bitmap.createScaledBitmap(
        imgima,
        ((w * 0.08f)).toInt(),
        ((w * 0.08f)).toInt(),
        false
    )


    val imgisufle = BitmapFactory.decodeResource(context.resources, R.drawable.giro)
    val imgisufleP = Bitmap.createScaledBitmap(
        imgisufle,
        ((w * 0.08f)).toInt(),
        ((w * 0.08f)).toInt(),
        false
    )


    private var semAds =
        ItemLoja(this.context, noadsP, 0f, h * 0.05f, (w), (h), "No ADS", 1, "", 1)
    private var coien1000 =
        ItemLoja(this.context, coin, 0f, h * 0.15f, (w), (h), "Coins", 1000,  "", 0)
    private var coien5000 =
        ItemLoja(this.context, coin5P, 0f, h * 0.25f, (w), (h), "Coins", 5000,  "", 0)
    private var coien10000 =
        ItemLoja(this.context, coin10P, 0f, h * 0.35f, (w), (h), "Coins", 10000,  "", 0)
   var listaPreco : MutableList<String> = mutableListOf()
    fun precos(){
        semAds =
            ItemLoja(this.context, noadsP, 0f, h * 0.05f, (w), (h), "No ADS", 1, this.listaPreco[3], 1)
        coien1000 =
            ItemLoja(this.context, coin, 0f, h * 0.15f, (w), (h), "Coins", 1000,  listaPreco[0], 0)
        coien5000 =
            ItemLoja(this.context, coin5P, 0f, h * 0.25f, (w), (h), "Coins", 5000,  listaPreco[2], 0)

        coien10000 =
            ItemLoja(this.context, coin10P, 0f, h * 0.35f, (w), (h), "Coins", 10000,  listaPreco[1], 0)
    }




    private var luzP = ItemLoja(this.context, imgluzP, 0f, h * 0.45f, (w), (h), "Find", 1, "100", 2)
    private var imaP =
        ItemLoja(this.context, imgimaP, 0f, h * 0.55f, (w), (h), "Magnet", 1, "100", 2)
    private var sufleP =
        ItemLoja(this.context, imgisufleP, 0f, h * 0.65f, (w), (h), "Shuffle", 1, "100", 2)

    var fecharLoja = BotaoM(
        this.context,
        ((this.w * 0.7)).toFloat(),
        (this.h * 0.0).toFloat(),
        (this.w * 0.15).toInt(),
        (this.h * 0.07).toInt(),
        6,
        "X"
    )


    fun draw(canvas: android.graphics.Canvas?) {


        if (moedas < 0) {
            base = BDSky(context).buscar()
            moedas = bd.buscar().pontos.toInt()
        }
        paint.color = Color.Black.toArgb()
//        val shader = BitmapShader(coinp!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
//        paint.shader = shader
        canvas?.drawRoundRect(
            0f,
            0f,
            w.toFloat(),
            h.toFloat(),
            20f,
            20f,
            this.paint
        )

        if (canvas != null) {

            canvas.drawBitmap(wal, 0f, 0f, paint)

            paint.color = Color.Blue.toArgb()
            paint.alpha = 150
            canvas.drawRoundRect(
                RectF(
                    (w * 0.12).toFloat(),
                    ((h * 0.02).toFloat() - (w * 0.025).toFloat()),
                    (w * 0.45).toFloat(),
                    ((h * 0.02)).toFloat() + ((((w * 0.05)).toInt()) * 1.22f)
                ), 40f, 40f, paint
            )
            paint.color = Color.LightGray.toArgb()
            paint.alpha = 150
            canvas.drawRoundRect(
                RectF(
                    (w * 0.125).toFloat(),
                    ((h * 0.02).toFloat() - (w * 0.02).toFloat()),
                    (w * 0.445).toFloat(),
                    ((h * 0.02).toFloat()) + ((((w * 0.05)).toInt()) * 1.15f)
                ), 40f, 40f, paint
            )
            paint.color = Color.White.toArgb()
            paint.textSize = spToPx(w * 0.015f)

            canvas.drawText(
                " $moedas",
                w * 0.2f,
                h * 0.04f,
                paint
            )








            canvas.drawBitmap(
                coinP,
                (w * 0.12).toFloat(),
                (h * 0.01).toFloat(),
                paint
            )





            fecharLoja.draw(canvas)
      if (!semanuncio) {
          semAds.draw(canvas)
      }

            coien1000.draw(canvas)
            coien5000.draw(canvas)
            coien10000.draw(canvas)




            if (base.pontos >= 100) {

                luzP.draw(canvas)
                imaP.draw(canvas)
                sufleP.draw(canvas)

                paint.color = Color.Magenta.toArgb()
                paint.alpha = 255

                canvas.drawRoundRect(
                    RectF(
                        (w * 0.24f),
                        h * 0.53f,
                        (w * 0.29f),
                        ((h * 0.555)).toFloat()
                    ), 60f, 60f, paint
                )

                paint.textSize = spToPx((this.w * 0.012f))
                paint.color = Color.White.toArgb()
                canvas.drawText(
                    base.luz.toString(),
                    (w * 0.25f),
                    h * 0.55f,
                    paint
                )





                paint.color = Color.Magenta.toArgb()
                paint.alpha = 255

                canvas.drawRoundRect(
                    RectF(
                        ((w * 0.24f)),
                        h * 0.63f,
                        (w * 0.29f),
                        ((h * 0.655)).toFloat()
                    ), 60f, 60f, paint
                )

                paint.textSize = spToPx((this.w * 0.012f))
                paint.color = Color.White.toArgb()
                canvas.drawText(
                    base.ima.toString(),
                    ((w * 0.25f)),
                    h * 0.65f,
                    paint
                )





                paint.color = Color.Magenta.toArgb()
                paint.alpha = 255

                canvas.drawRoundRect(
                    RectF(
                        ((w * 0.24f)),
                        h * 0.73f,
                        (w * 0.29f),
                        ((h * 0.755)).toFloat()
                    ), 60f, 60f, paint
                )

                paint.textSize = spToPx((this.w * 0.012f))
                paint.color = Color.White.toArgb()
                canvas.drawText(
                    base.sufle.toString(),
                    ((w * 0.25f)),
                    h * 0.75f,
                    paint
                )
            }


        }


        if (semAds.btm.liberar > 3) {
            semAds.btm.liberar = 0

            gameView.comprar("remove_ads")
            atualizar = true

            abrirLoja = false


        }else if (coien1000.btm.liberar > 3) {
            coien1000.btm.liberar = 0

            atualizar = true

            abrirLoja = false

            gameView.comprar("coins_1000")
        }else if (coien5000.btm.liberar > 3) {
            coien5000.btm.liberar = 0
            atualizar = true

            abrirLoja = false

            gameView.comprar("coins_5000")

        }else if (coien10000.btm.liberar > 3) {
            coien10000.btm.liberar = 0
            atualizar = true

            abrirLoja = false

            gameView.comprar("coins_10000")


        }


    }

    fun onTouchEvent(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {


            if (fecharLoja.containsTouch(
                    event.x,
                    event.y
                )
            ) {

                fecharLoja.liberar = 0
                atualizar = true
                abrirLoja = false

            } else if (semAds.btm.containsTouch(
                    event.x,
                    event.y
                ) && !semanuncio
            ) {

                semAds.btm.animar = true

            } else if (coien1000.btm.containsTouch(
                    event.x,
                    event.y
                )
            ) {

                coien1000.btm.animar = true

            } else if (coien5000.btm.containsTouch(
                    event.x,
                    event.y
                )
            ) {
                coien5000.btm.animar = true

            } else if (coien10000.btm.containsTouch(
                    event.x,
                    event.y
                )
            ) {
                coien10000.btm.animar = true
            }


            if (base.pontos >= 100) {
                if (luzP.btm.containsTouch(
                        event.x,
                        event.y
                    )
                ) {
                    luzP.btm.liberar = 0
                    base = bd.buscar()
                    base.luz++
                    base.pontos -= 100
                    moedas = base.pontos.toInt()
                    bd.atualizar(base)


                } else if (imaP.btm.containsTouch(
                        event.x,
                        event.y
                    )
                ) {
                    imaP.btm.liberar = 0
                    base = bd.buscar()
                    base.ima++
                    base.pontos -= 100
                    moedas = base.pontos.toInt()
                    bd.atualizar(base)


                } else if (sufleP.btm.containsTouch(
                        event.x,
                        event.y
                    )
                ) {
                    sufleP.btm.liberar = 0
                    base = bd.buscar()
                    base.sufle++
                    base.pontos -= 100
                    moedas = base.pontos.toInt()
                    bd.atualizar(base)


                }

            }


        }
    }






    fun spToPx(sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
    }

}


