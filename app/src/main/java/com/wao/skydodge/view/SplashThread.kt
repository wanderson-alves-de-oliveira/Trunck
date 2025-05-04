package com.wao.skydodge.view

import android.view.SurfaceHolder

class SplashThread(private val surfaceHolder: SurfaceHolder, private val splashView: SplashScreenView) : Thread() {
    var running = false

    override fun run() {
        while (running) {
            val canvas = surfaceHolder.lockCanvas()
            if (canvas != null) {
                synchronized(surfaceHolder) {
                    splashView.update()
                    splashView.draw(canvas)
                }
                surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }
    }
}
