package com.wao.skydodge.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.wao.skydodge.R
import kotlin.random.Random


class MahjongTile(
    var image: Bitmap,
    val context: Context,
    var x: Float,
    private var yp: Float,
    var w: Int,
    var h: Int,
    var camada: Int,
    var id: Int,
    var girando: Boolean = false
) {


    private val paint = Paint()
    private val paint2 = Paint()
    var y: Float = if (id >= 1000) yp else Random.nextInt((-100 * id), 0).toFloat()
    var isSelected = false
    private var intro = true
    var bloqueado = false
    var ref = -1

    var naLista = false

    var ty = true
    var espaco = 0f
    private var giroc = 0f
    private val VALLIMITG = 10
    private val VALLIMITGINTRO = 20
    private var coinp: Bitmap? = null


    private var timeG = if (intro) VALLIMITGINTRO else VALLIMITG
    @SuppressLint("SuspiciousIndentation")
    fun draw(canvas: Canvas) {
        paint.textSize = 80f

        paint2.color = Color.Black.toArgb()

        paint.color = Color.Red.toArgb()

        if (w <= 0 || h <= 0) {
            w = 100
            h = 100
        }
        val b: Bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        espaco = if (!isSelected) {
            0f
        } else {
            w * 0.06f

        }

        val img = Bitmap.createScaledBitmap(
            image,
            ((w * 0.9f) - espaco).toInt(),
            ((h * 0.9f) - espaco).toInt(),
            false
        )
        if (naLista) {

            val coin = BitmapFactory.decodeResource(context.resources, R.drawable.moedaprata)
            coinp = Bitmap.createScaledBitmap(
                coin!!,
                ((w * 0.9f) - espaco).toInt(),
                ((h * 0.9f) - espaco).toInt(),
                false
            )

        }

        val canvas2 = Canvas(b)

        //canvas2.drawRGB(250, 250, 0)
        when (camada) {
            0 -> Color.Green
            1 -> Color.Yellow
            2 -> Color.Blue
            else -> Color.Magenta
        }


        val width = img.width
        val height = img.height
        if (camada <=-3 && naLista) {
            val shader = BitmapShader(coinp!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.shader = shader

            canvas2.drawRoundRect(
                RectF(1f, 1f, (width).toFloat(), (height).toFloat()),
                0f,
                0f,
                paint
            )
        } else {

            if (isSelected) {
                paint2.color = Color.Gray.toArgb()
            }
            paint2.alpha = 180

            canvas2.drawRoundRect(
                0f,
                0f,
                img.width.toFloat(),
                b.height.toFloat() - espaco,
                20f,
                20f,
                paint2
            )


        val matrix = ColorMatrix()

            when (camada) {
                0 -> {
                    matrix.set(
                        floatArrayOf(
                            0.6f, 0f, 0f, 0f, 0f, // Red
                            0f, 0.6f, 0f, 0f, 0f, // Green
                            0f, 0f, 0.6f, 0f, 0f, // Blue
                            0f, 0f, 0f, 1f, 0f  // Alpha
                        )
                    )
                }
                1 -> {
                    matrix.set(
                        floatArrayOf(
                            0.8f, 0f, 0f, 0f, 0f, // Red
                            0f, 0.8f, 0f, 0f, 0f, // Green
                            0f, 0f, 0.8f, 0f, 0f, // Blue
                            0f, 0f, 0f, 1f, 0f  // Alpha
                        )
                    )
                }
                else -> {
                    matrix.set(
                        floatArrayOf(
                            1.0f, 0f, 0f, 0f, 0f, // Red
                            0f, 1.0f, 0f, 0f, 0f, // Green
                            0f, 0f, 1.0f, 0f, 0f, // Blue
                            0f, 0f, 0f, 1f, 0f  // Alpha
                        )
                    )
                }
            }


        paint.colorFilter = ColorMatrixColorFilter(matrix)

        // Obtenha as dimensões da View


        // Crie um BitmapShader com a imagem



            val shader = BitmapShader(img, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            paint.shader = shader

            val cornerRadius = 20f
            paint.color = Color.LightGray.toArgb()
            canvas2.drawRoundRect(
                RectF(1f, 1f, (width).toFloat(), (height).toFloat()),
                cornerRadius,
                cornerRadius,
                paint
            )

        }
        if (intro) {
            if (girando) {
                giroc--
                timeG--
                if (timeG <= 0 && giroc <= 0) {
                    girando = false
                    giroc = 0f
                    timeG = VALLIMITG
                    intro = false
                    y = yp
                    ty = false

                }
                if (y < yp) {
                    val vel = h * (timeG / 8)
                    y += vel

                    if (y > yp) {
                        y = yp
                        girando = false
                        giroc = 0f
                        timeG = VALLIMITG
                        intro = false
                        y = yp
                        ty = false


                    }
                }


            } else {
                intro = false

            }
            canvas.drawBitmap(b, x + espaco, y + espaco, paint)


        } else {
            if (girando) {
                giroc -= 20f
                timeG--
                if (timeG <= 0 && giroc <= 0) {
                    girando = false
                    giroc = 0f
                    timeG = VALLIMITG
                }
//                canvas.save()
//                canvas.translate((x - (w * 0.5f)), (y + espaco) + giroc)
//                //  canvas.rotate(giroc,centerX,centerY)
//                canvas.drawBitmap(b, x + espaco, y + espaco, paint)
//                canvas.restore()

                canvas.save()
                val centroX = x + b.width / 2f
                val centroY = y + b.height / 2f

                // Rotaciona o canvas em torno do centro da imagem
                canvas.rotate(giroc, centroX, centroY)
                canvas.translate((x - (w * 0.5f)), (y + espaco) + giroc)
                 canvas.drawBitmap(b, x + espaco, y + espaco, paint)
                canvas.restore()

            } else {

                canvas.drawBitmap(b, x + espaco, y + espaco, paint)
            }
        }


    }

    fun containsTouch(touchX: Float, touchY: Float): Boolean {
        return touchX >= x && touchX <= x + w && touchY >= y && touchY <= y + h
    }

    fun containsCamada(touchX: Float, touchY: Float): Boolean {
        val t1 = touchX > x && touchX < x + w && touchY > y && touchY < y + h
        val t2 = touchX + w > x && touchX + w < x + w && touchY > y && touchY < y + h
        val t3 = touchX > x && touchX < x + w && touchY + h > y && touchY + h < y + h
        val t4 = touchX + w > x && touchX + w < x + w && touchY + h > y && touchY + h < y + h
        return t1 || t2 || t3 || t4
    }
}
