package com.obedhack.capturedeletedmessages

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.room.Room


class MyNotificationListenerService : NotificationListenerService() {

    private lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "notifications-db").build()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        if (packageName == "com.whatsapp") {
            val notification = sbn.notification
            val extras = notification.extras
            val title = extras.getString("android.title")
            val text = extras.getCharSequence("android.text").toString()

            Log.d("NotificationListener", "WhatsApp message from $title: $text")

            // Store the notification details in the database
            val message = NotificationEntity(0, packageName, title, text)

            saveNotification(message)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Handle notification removal if needed
    }

    private fun saveNotification(message: NotificationEntity) {
        Thread {
            db.notificationDao().insert(message)
        }.start()
    }
}

