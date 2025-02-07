package com.example.photochallenge.feature.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.photochallenge.activity.MainActivity

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        val channelId = "daily_challenge_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // creation du channel de notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Défis quotidiens",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal pour les défis quotidiens"
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
        val challengeIntent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = MainActivity.NOTIFICATION_DAILY_CHALLENGE
            data = Uri.parse("app://androidx.navigation/takePicture")
        }
        val challengePendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            challengeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val standingIntent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = MainActivity.NOTIFICATION_DAILY_STANDING
            data = Uri.parse("app://androidx.navigation/standing")
        }
        val standingPendingIntent = PendingIntent.getActivity(
            applicationContext,
            1,
            standingIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val challenges = listOf(
            "🏃‍♂️ Prêt pour un nouveau défi ? Montre-nous ce que tu vaux !",
            "💪 Hey champion ! Un nouveau défi t'attend. Tu relèves ?",
            "🎯 Objectif du jour : devenir encore plus fort ! Tu es partant ?",
            "🌟 Un nouveau jour, un nouveau défi ! On y va ?",
            "🚀 Mission du jour : repousser tes limites !",
            "🎮 Level up ! Un nouveau défi vient d'apparaître",
            "🏆 La victoire t'attend ! Prêt à gagner ?",
            "⚡ Nouveau défi disponible ! Tu as ce qu'il faut ?"
        )

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .setContentTitle("📝 Défi du Jour")
            .setContentText(challenges.random())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setColor(Color.GREEN)
            .addAction(
                android.R.drawable.ic_media_play,
                "Relever le défi",
                challengePendingIntent
            )
            .addAction(
                android.R.drawable.ic_dialog_info,
                "Mon classement",
                standingPendingIntent
            )
            .setContentIntent(challengePendingIntent)
            .setStyle(NotificationCompat.BigTextStyle())

        try {
            notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
            Log.d("NotificationWorker", "Notification envoyée")
        } catch (e: Exception) {
            Log.e("NotificationWorker", "Erreur lors de l'envoi de la notification", e)
        }
    }
}