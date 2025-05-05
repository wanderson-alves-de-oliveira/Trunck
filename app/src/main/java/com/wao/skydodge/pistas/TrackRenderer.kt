package com.wao.skydodge.pistas

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect

class TrackRenderer(private val context: Context) {

    private var segmentWidth = 1024  // Largura do segmento em pixels
    private var segmentHeight = 720  // Altura da tela ou do segmento
    private var segmentCount = 2    // Número de segmentos da pista

    private val trackSegments = mutableListOf<Bitmap>()
    private var scrollX = 0f         // Controle de rolagem

//    init {
//        loadTrackSegments()
//    }

     fun loadTrackSegments(w: Int, h: Int,list: MutableList<Int>) {
         segmentCount = list.size
         segmentWidth=w
         segmentHeight=h
        for (i in 0..segmentCount-1) {
          //  val resId = context.resources.getIdentifier("pathi$i", "drawable", context.packageName)
            var bmp = BitmapFactory.decodeResource(context.resources, list[i])
              bmp = Bitmap.createScaledBitmap(
                  bmp,
                (w).toInt(),

                (h).toInt(),
                false
            )
            trackSegments.add(bmp)
        }
    }

    fun updateScroll(dx: Float) {
        scrollX += dx
        // Impede que role além dos limites da pista
        val maxScroll = segmentWidth * segmentCount - segmentWidth
        scrollX = scrollX.coerceIn(0f, maxScroll.toFloat())
    }

    fun draw(canvas: Canvas) {
        val screenWidth = canvas.width
        val screenHeight = canvas.height

        val firstSegmentIndex = (scrollX / segmentWidth).toInt()
        val offsetX = -(scrollX % segmentWidth)

        for (i in 0..1) {
            val index = firstSegmentIndex + i
            if (index >= segmentCount) continue

            val bitmap = trackSegments[index]
            val dstRect = Rect(
                (offsetX + i * segmentWidth).toInt(),
                0,
                (offsetX + (i + 1) * segmentWidth).toInt(),
                screenHeight
            )
            canvas.drawBitmap(bitmap, null, dstRect, null)
        }
    }
}
