package com.wao.skydodge.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.TypedValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.wao.skydodge.R

class BotaoM(
    val contexte: Context,
    var x: Float,
    var y: Float,
    var w: Int,
    var h: Int,
    var camada: Int,
    var stt: String
) {
    private val paint = Paint()

var animar = false
    var liberar = 0
    var t = 0f
    var inicio = true
    var yp = h*11f
var xFix = x
    fun draw(canvas: Canvas ) {
         paint.textSize = spToPx(w*0.04f)
        val options = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.RGB_565
        }
       val b: Bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888)
        val coin = BitmapFactory.decodeResource(contexte.resources, R.drawable.moeda,options)
        val coinP = Bitmap.createScaledBitmap(
            coin,
            ((w * 0.1f)).toInt(),
            ((w * 0.1f)).toInt(),
            false
        )

        val vid = BitmapFactory.decodeResource(contexte.resources, R.drawable.ad,options)
        val videP = Bitmap.createScaledBitmap(
            vid,
            ((w * 0.25f)).toInt(),
            ((w * 0.2f)).toInt(),
            false
        )


        val canvas2 = Canvas(b)

        if(animar){
            canvas2.scale(t,t)
            if(liberar==0) {
                t -= 0.1f
                x+=(w*0.01f)
            }else   if(liberar==1) {
                t += 0.1f
              //  x-=(w*0.02f)
            }
            if(t<=0.9f && liberar==0){
                 liberar = 1
            }else if(t>=1f && liberar==1){
                animar = false
                liberar = 2
            }

//            else{
//                animar = false
//            }


        }else{

            if(inicio){

                if(camada<4) {
                    if (yp > y) {
                        val dif = (yp - y) / 2
                        yp -= dif
                        if (dif <= 0.5f) {
                            yp = y
                        }

                    } else {
                        inicio = false
                    }
                }else{
                    yp = y
                    inicio = false
                }

            }else {
                b.width = w
                b.height = h
                x = xFix
                t = 1f
                if (liberar >= 2) {
                    liberar++
                }
            }



        }

        paint.alpha = 200
        if(camada==0) {
            paint.color = Color(0xFF4169E1).toArgb()
        }else  if(camada==1) {
            paint.color = Color(0xFF8BC34A).toArgb()

        }else  if(camada==2) {
            paint.color = Color(0xFFF44336).toArgb()

        }else  if(camada==3) {
            paint.color = Color(0xFF8BC34A).toArgb()

        }else  if(camada==4) {
            paint.color = Color(0xFF8BC34A).toArgb()

        }else  if(camada>=5) {
            paint.color = Color(0xFFF44336).toArgb()

        }
         canvas2.drawRoundRect(RectF(b.width.toFloat()*0.05f, b.height.toFloat()*0.05f, b.width.toFloat()*0.95f, b.height.toFloat()*0.95f), 70f, 70f, paint)

        if(camada==0) {
            paint.color = Color(0xFF836FFF).toArgb()
        }else  if(camada==1) {
            paint.color = Color(0xFF4CAF50).toArgb()

        }else  if(camada==2) {
            paint.color = Color(0xFFE91E63).toArgb()

        }else  if(camada==3) {
            paint.color = Color(0xFF4CAF50).toArgb()

        }else  if(camada==4) {
            paint.color = Color(0xFF4CAF50).toArgb()

        }else  if(camada>=5) {
            paint.color = Color(0xFFE91E63).toArgb()

        }
        canvas2.drawRoundRect(RectF(b.width.toFloat()*0.05f, b.height.toFloat()*0.1f, b.width.toFloat()*0.95f, b.height.toFloat()*0.95f), 70f, 70f, paint)

        paint.textSize =  spToPx(w*0.04f)

         paint.color = Color(0xFFffffff).toArgb()
        if(camada==0) {
            canvas2.drawText(
                stt,
                (w * 0.1).toFloat(),
                (h * 0.6).toFloat(),
                paint
            )
        }else   if(camada==1) {
            canvas2.drawText(
                stt,
                (w * 0.25).toFloat(),
                (h * 0.4).toFloat(),
                paint
            )
            canvas2.drawBitmap(coinP, (w * 0.35).toFloat(),  (h * 0.55).toFloat(), paint)

            canvas2.drawText(
                "300",
                (w * 0.45).toFloat(),
                (h * 0.65).toFloat(),
                paint
            )
        }else   if(camada==2) {
            canvas2.drawText(
                stt,
                (w * 0.25).toFloat(),
                (h * 0.4).toFloat(),
                paint
            )
            canvas2.drawBitmap(videP, (w * 0.35).toFloat(),  (h * 0.55).toFloat(), paint)

        }else   if(camada==3) {
            canvas2.drawText(
                stt,
                (w * 0.25).toFloat(),
                (h * 0.4).toFloat(),
                paint
            )
            canvas2.drawBitmap(videP, (w * 0.35).toFloat(),  (h * 0.55).toFloat(), paint)
            canvas2.drawBitmap(coinP, (w * 0.82).toFloat(),  (h * 0.25).toFloat(), paint)

        }else   if(camada==4) {
            paint.textSize =  spToPx(w*0.06f)

            canvas2.drawText(
                stt,
                (w * 0.1).toFloat(),
                (h * 0.6).toFloat(),
                paint
            )

        }else   if(camada==5) {

            canvas2.drawBitmap(coinP, (w * 0.35).toFloat(),  (h * 0.55).toFloat(), paint)

            canvas2.drawText(
                "100",
                (w * 0.45).toFloat(),
                (h * 0.65).toFloat(),
                paint
            )
        }else   if(camada==6) {

            paint.textSize =  spToPx(w*0.12f)

            canvas2.drawText(
                "X",
                (w * 0.4).toFloat(),
                (h * 0.65).toFloat(),
                paint
            )
        }




        canvas.drawBitmap(b,x,yp,paint)
    }

    fun containsTouch(touchX: Float, touchY: Float): Boolean {
        return ((touchX >= x && touchX <= x + w && touchY >= y && touchY <= y + h) && !inicio)
    }



     fun spToPx(sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            contexte.resources.displayMetrics
        )
    }



}
