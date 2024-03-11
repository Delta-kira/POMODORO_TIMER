package com.example.day3

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class TimerNotification(
    private val context: Context
) {
    fun showNotification() {
        val notification = NotificationCompat.Builder(context, "Timer_reminder")
            .setContentTitle("Timer Finished")
            .setContentText("Your 25-minute timer has finished!")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(123, notification)
    }

}