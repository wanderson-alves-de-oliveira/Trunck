package com.wao.skydodge.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.wao.skydodge.MainActivity
import com.wao.skydodge.R

class SplashScreenView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private val thread = SplashThread(holder, this)
    private var logo: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.icon)
    private var startTime = 0L
    private val duration = 3000L // 3 segundos

    init {
        holder.addCallback(this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        startTime = System.currentTimeMillis()
        thread.running = true
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        thread.running = false
        while (retry) {
            try {
                thread.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun update() {
        if (System.currentTimeMillis() - startTime >= duration) {
            (context as Activity).startActivity(Intent(context, MainActivity::class.java))
            (context as Activity).finish()
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawColor(Color.White.toArgb())
        val centerX = width / 2 - logo.width / 2
        val centerY = height / 2 - logo.height / 2
        canvas.drawBitmap(logo, centerX.toFloat(), centerY.toFloat(), null)
    }
}
