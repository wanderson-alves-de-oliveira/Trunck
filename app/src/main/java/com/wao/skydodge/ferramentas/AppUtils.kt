package com.wao.skydodge.ferramentas



import android.app.ActivityManager
import android.content.Context

object AppUtils {
    fun isAppInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses ?: return false

        for (processInfo in runningAppProcesses) {
            if (processInfo.processName == context.packageName) {
                return processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return false
    }
}
