package com.example.day3

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi

class NotificationApplication : Application(){

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val notificationchannel = NotificationChannel(
             "Timer_reminder",
            "timer notification channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationchannel.description = "NOTIFICATION FOR TIMER"
        val notificationmanager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationmanager.createNotificationChannel(notificationchannel)
    }
}