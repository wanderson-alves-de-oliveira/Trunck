package com.wao.skydodge.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
 import android.graphics.Canvas
 import android.graphics.Paint
 import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.wao.skydodge.R

class MainView(   var context: Context,
                  var w: Int,
                  var h: Int) {


    private val paint = Paint()
    private val paint2 = Paint()
    var y: Float = 0f
    private var isSelected = false

     private var inicio = true
     private var xp = w*11f


    var espaco = 0f



    @SuppressLint("SuspiciousIndentation")
    fun draw(canvas: Canvas) {
        paint.textSize = 80f

        paint2.color = Color.Magenta.toArgb()

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
        val options = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.RGB_565
        }
        val tileImage = BitmapFactory.decodeResource(context.resources, R.drawable.titulo,options)
        val img = Bitmap.createScaledBitmap(
            tileImage
        ,
            ((w * 0.45f) - espaco).toInt(),
            ((h * 0.05f) - espaco).toInt(),
            false
        )
        val x: Float = ((w/2)-(img.width/2)).toFloat()

        val main = BitmapFactory.decodeResource(context.resources, R.drawable.main,options)
        val mainp = Bitmap.createScaledBitmap(
            main
            ,
            ((w ) ),
            ((h ) ),
            false
        )

        val canvas2 = Canvas(b)
      paint.color = Color.Red.toArgb()


        paint.color = Color.LightGray.toArgb()


        canvas2.drawBitmap(mainp,0f,0f,paint)


        if(inicio){

            if(xp>x){
                val dif = (xp-x)/2
                xp-=dif
                if(dif<=0.5f){
                    xp = x
                }

            }else{
                inicio = false
            }

        }

            canvas2.drawBitmap(img,xp,h*0.10f,paint)



                canvas.drawBitmap(b, 0f, 0f, paint)




    }



}