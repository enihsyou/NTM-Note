package com.enihsyou.ntmnote

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initDatabase()
        createNotificationChannel()
    }

    private fun initDatabase() {
        Log.v(TAG, "初始化数据库")
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "note_channel"
            val description = "note_channel_description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    companion object {
        private const val TAG = "NTM Note Application"

        const val CHANNEL_ID = "notes"
    }
}
