package com.wao.skydodge.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.TypedValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.wao.skydodge.R

class Venceu(
    var context: Context,
    var w: Int,
    var h: Int, var tipo: Int
) {

    var fase: Int = 0
    var pontos: Int = 0
    private var valorminimo: Int = 300
    var semInternet = false
    var internetR = "Sem Internet"
    private val paint = Paint()
    private val paint2 = Paint()
    var y: Float = 0f


    var espaco = 0f
    private var tileImage: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.mahjongtile)
    private val b: Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

    var btm = BotaoM(
        this.context,
        ((this.w * 0.55)).toFloat(),
        (this.h * 0.6).toFloat(),
        (this.w * 0.3).toInt(),
        (this.h * 0.1).toInt(),
        0,
        fase.toString()
    )



    var btmCoin = BotaoM(
        this.context,
        ((this.w * 0.2)).toFloat(),
        (this.h * 0.6).toFloat(),
        (this.w * 0.3).toInt(),
        (this.h * 0.1).toInt(),
        1,
        "REVIVER"
    )


    private var tam = 0f
    var liberado = false

    fun draw(canvas: Canvas) {

        if (tam < 5) {
            tam += 1f
        } else {
            liberado = true
        }

        paint.color = Color(0xFF000000).toArgb()
        paint.alpha = 150
        val b3: Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvasB = Canvas(b3)

        desenharRetanguloArredondadoCentralizado(
            canvasB,
            (this.w * 0.15f) * tam,
            (this.h * 0.1f) * tam,
            70f,
            1f,
            paint
        )
        if (tipo == 0) {
            paint.color = Color(0xFF4CAF50).toArgb()

            btmCoin.stt = "Obter + 50"
            btmCoin.camada = 3


            paint.color = Color(0xFFFFA500).toArgb()
            btm.stt = "Nível $fase"
            btm.w = (this.w * 0.6).toInt()
            btm.camada = 0


        } else {
            when (tipo) {
                1 -> {
                    paint.color = Color(0xFFD71204).toArgb()
                    btm.stt = "REVIVER"
                    btm.camada = 2
                }

                2 -> {
                    paint.color = Color(0xFFD71204).toArgb()
                    btm.stt = "+ 3"
                    btm.camada = 2


                    btmCoin.stt = "+ 3"

                }

                3 -> {
                    paint.color = Color(0xFFD71204).toArgb()
                    btm.stt = "+ 3"
                    btm.camada = 2
                    btmCoin.stt = "+ 3"

                }

                4 -> {
                    paint.color = Color(0xFFD71204).toArgb()
                    btm.stt = "+ 3"
                    btm.camada = 2
                    btmCoin.stt = "+ 3"

                }
            }
        }

        paint.alpha = 150
        desenharRetanguloArredondadoCentralizado(
            canvasB,
            (this.w * 0.14f) * tam,
            (this.h * 0.095f) * tam,
            70f,
            1f,
            paint
        )


        if (tipo == 0) {
            paint.color = Color(0xFF5442C5).toArgb()

        } else {
            paint.color = Color(0xFFFFA500).toArgb()

        }
        paint.alpha = 150
        desenharRetanguloArredondadoCentralizado(
            canvasB,
            (this.w * 0.13f) * tam,
            (this.h * 0.065f) * tam,
            40f,
            0.8f,
            paint
        )



        if (tipo == 0) {
            paint.color = Color(0xFF7B68EE).toArgb()

        } else {
            paint.color = Color(0xFF919ACE).toArgb()

        }
        paint.alpha = 255
        desenharRetanguloArredondadoCentralizado(
            canvasB,
            (this.w * 0.12f) * tam,
            (this.h * 0.06f) * tam,
            40f,
            0.8f,
            paint
        )


        paint.color = Color(0xFFFFFFFF).toArgb()
        desenharTextoCentralizado(canvasB, (this.w * 0.14f) * tam, (this.h * 0.095f) * tam, paint)

        canvas.drawBitmap(b3, 0f, 0f, paint)
        if (tipo == 0) {
            btm.x = ((this.w * 0.2)).toFloat()
            btm.xFix = ((this.w * 0.2)).toFloat()

            btm.draw(canvas)


            btmCoin.x = ((this.w * 0.2) + (this.w * 0.15f)).toFloat()
            btmCoin.xFix = ((this.w * 0.2) + (this.w * 0.15f)).toFloat()
            btmCoin.y = (this.h * 0.52f)

            btmCoin.h = (this.h * 0.08).toInt()
            btmCoin.draw(canvas)

        } else {
            if (pontos >= valorminimo) {
                btm.h = (this.h * 0.08).toInt()
                btm.x = (this.w * 0.55f)
                btm.xFix = (this.w * 0.55f)
                btmCoin.h = (this.h * 0.08).toInt()
                btm.draw(canvas)
                btmCoin.draw(canvas)
            } else {
                btm.h = (this.h * 0.08).toInt()
                btm.x = (this.w * 0.35f)
                btm.xFix = (this.w * 0.35f)

                btm.draw(canvas)
            }


        }
    }

    private fun desenharRetanguloArredondadoCentralizado(
        canvas: Canvas,
        largura: Float,
        altura: Float,
        raio: Float,
        alt: Float,
        paint: Paint
    ) {
        // Calcula o centro da tela
        val centroX = canvas.width / 2f
        val centroY = canvas.height / 2f

        // Calcula os limites do retângulo arredondado
        val esquerda = centroX - largura / 2f
        val topo = (centroY - altura / 2f) * alt
        val direita = centroX + largura / 2f
        val inferior = (centroY + altura / 2f) * alt

        // Cria um objeto RectF com os limites calculados
        val rect = RectF(esquerda, topo, direita, inferior)

        canvas.drawRoundRect(rect, raio, raio, paint)
    }

    private fun desenharTextoCentralizado(
        canvas: Canvas,
        largura: Float,
        altura: Float,
        paint: Paint
    ) {
        // Calcula o centro da tela
        val centroX = canvas.width / 2f
        val centroY = canvas.height / 2f

        // Calcula os limites do retângulo arredondado
        val esquerda = centroX - largura / 2f
        val topo = centroY - altura / 2f
        val direita = centroX + largura / 2f
        val inferior = centroY + altura / 2f

        // Cria um objeto RectF com os limites calculados
        RectF(esquerda, topo, direita, inferior)
        paint.textSize = spToPx(altura * 0.03f)
        if (tipo == 0) {
            canvas.drawText("Nível ${fase - 1}", centroX - (largura / 6), centroY * 0.7f, paint)
            canvas.drawText("Completo", centroX - (largura / 4), centroY * 0.8f, paint)
            paint.color = Color(0xFF15072F).toArgb()

            if (liberado) {
                val imgg = BitmapFactory.decodeResource(context.resources, R.drawable.moeda)

                val imggg = Bitmap.createScaledBitmap(
                    imgg,
                    ((w * 0.12f) - espaco).toInt(),
                    ((h * 0.06f) - espaco).toInt(),
                    false
                )
                paint.color = Color(0xFFFFC107).toArgb()

                canvas.drawBitmap(imggg, ((w * 0.33f) + espaco), ((h * 0.45f) + espaco), paint)
                canvas.drawText(" $pontos", centroX, centroY, paint)

            }


        } else {


            when (tipo) {
                1 -> {
                    if (!semInternet) {
                        canvas.drawText(
                            "SEM ESPAÇO",
                            centroX - (largura / 2.5f),
                            centroY * 0.7f,
                            paint
                        )
                        canvas.drawText("Falhou", centroX - (largura / 4), centroY * 0.8f, paint)
                    } else {
                        canvas.drawText(
                            internetR,
                            centroX - (largura / 2.5f),
                            centroY * 0.7f,
                            paint
                        )
                        //canvas.drawText("Falhou", centroX - (largura / 4), centroY * 0.8f, paint)


                    }

                }

                2 -> {
                    tileImage = BitmapFactory.decodeResource(context.resources, R.drawable.lampada)

                    val img = Bitmap.createScaledBitmap(
                        tileImage,
                        ((w * 0.15f) - espaco).toInt(),
                        ((h * 0.1f) - espaco).toInt(),
                        false
                    )
                    //    canvas.drawText("+ 3 ", centroX - (largura / 2.5f), centroY * 0.7f, paint)
                    val canvas2 = Canvas(b)

                    val shader = BitmapShader(img, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                    paint2.shader = shader

                    val cornerRadius = 360f
                    paint.color = Color.LightGray.toArgb()
                    canvas2.drawRoundRect(
                        RectF(1f, 1f, (img.width).toFloat(), (img.height).toFloat()),
                        cornerRadius,
                        cornerRadius,
                        paint2
                    )


                    canvas.drawBitmap(b, ((w * 0.4f) + espaco), ((h * 0.3f) + espaco), paint)

                }

                3 -> {


                    tileImage = BitmapFactory.decodeResource(context.resources, R.drawable.ima)

                    val img = Bitmap.createScaledBitmap(
                        tileImage,
                        ((w * 0.15f) - espaco).toInt(),
                        ((h * 0.1f) - espaco).toInt(),
                        false
                    )
                    //    canvas.drawText("+ 3 ", centroX - (largura / 2.5f), centroY * 0.7f, paint)
                    val canvas2 = Canvas(b)

                    val shader = BitmapShader(img, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                    paint2.shader = shader

                    val cornerRadius = 360f
                    paint.color = Color.LightGray.toArgb()
                    canvas2.drawRoundRect(
                        RectF(1f, 1f, (img.width).toFloat(), (img.height).toFloat()),
                        cornerRadius,
                        cornerRadius,
                        paint2
                    )


                    canvas.drawBitmap(b, ((w * 0.4f) + espaco), ((h * 0.3f) + espaco), paint)


                }

                4 -> {

                    tileImage = BitmapFactory.decodeResource(context.resources, R.drawable.giro)

                    val img = Bitmap.createScaledBitmap(
                        tileImage,
                        ((w * 0.15f) - espaco).toInt(),
                        ((h * 0.1f) - espaco).toInt(),
                        false
                    )
                    //    canvas.drawText("+ 3 ", centroX - (largura / 2.5f), centroY * 0.7f, paint)
                    val canvas2 = Canvas(b)

                    val shader = BitmapShader(img, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                    paint2.shader = shader

                    val cornerRadius = 360f
                    paint.color = Color.LightGray.toArgb()
                    canvas2.drawRoundRect(
                        RectF(1f, 1f, (img.width).toFloat(), (img.height).toFloat()),
                        cornerRadius,
                        cornerRadius,
                        paint2
                    )


                    canvas.drawBitmap(b, ((w * 0.4f) + espaco), ((h * 0.3f) + espaco), paint)


                }
            }




            paint.color = Color(0xFF15072F).toArgb()
            if (liberado) {
                val imgg = BitmapFactory.decodeResource(context.resources, R.drawable.moeda)

                val imggg = Bitmap.createScaledBitmap(
                    imgg,
                    ((w * 0.12f) - espaco).toInt(),
                    ((h * 0.06f) - espaco).toInt(),
                    false
                )

                canvas.drawBitmap(imggg, ((w * 0.33f) + espaco), ((h * 0.45f) + espaco), paint)
                canvas.drawText(" $pontos", centroX, centroY, paint)

            }


        }


    }

    private fun spToPx(sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
    }
}