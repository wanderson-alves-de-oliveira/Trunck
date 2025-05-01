package com.wao.skydodge.ferramentas


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.wao.skydodge.MainActivity
import com.wao.skydodge.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        if (AppUtils.isAppInForeground(context)) {
            return
        }

        val channelId = "notification_channel"
        val notificationId = 1
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val resultIntent = Intent(context, MainActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(context).apply {
            addNextIntentWithParentStack(resultIntent)
        }
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val customView = RemoteViews(context.packageName, R.layout.custom_notification)

        // Criar o canal de notificação (para Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificações Periódicas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Definir som padrão
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // Criar a notificação personalizada
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ima)
            .setCustomContentView(customView) // Aplica o layout customizado
            .setStyle(NotificationCompat.DecoratedCustomViewStyle()) // Cor da notificação
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri) // Som de notificação
            .setVibrate(longArrayOf(500, 1000, 500))
            .setContentIntent(resultPendingIntent) // Abre MainActivity ao clicar
            .setAutoCancel(true) // Padrão de vibração
            .build()

        notificationManager.notify(notificationId, notification)
    }
}
