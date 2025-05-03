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

class BotaoBitmap(
    val contexte: Context,
    var x: Float,
    var y: Float,
    var w: Int,
    var h: Int,
    var btm: Bitmap
) {
    private val paint = Paint()

 init {
     btm = Bitmap.createScaledBitmap(
         btm,
         (w).toInt(),
         (h).toInt(),
         false
     )

 }
    fun draw(canvas: Canvas ) {
         paint.textSize = spToPx(w*0.04f)


        canvas.drawBitmap(btm,x,y,paint)
    }

    fun containsTouch(touchX: Float, touchY: Float): Boolean {
        return ((touchX >= x && touchX <= x + w && touchY >= y && touchY <= y + h) )
    }



     fun spToPx(sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            contexte.resources.displayMetrics
        )
    }



}
