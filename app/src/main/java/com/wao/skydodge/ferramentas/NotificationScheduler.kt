package com.wao.skydodge.ferramentas

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock

object NotificationScheduler {
    fun scheduleNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Agendar notificação a cada 20 segundos
        val interval = (60 * 60000L) *24// 20 segundos em milissegundos
        val triggerTime = SystemClock.elapsedRealtime() + interval

        // Configurar o AlarmManager para repetir a cada 20 segundos
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            interval,
            pendingIntent
        )
    }
}
