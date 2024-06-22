package com.obedhack.capturedeletedmessages


import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotificationListener : NotificationListenerService() {

    private lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "notifications-db").build()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val extras = sbn.notification.extras
        val title = extras.getString("android.title") ?: ""
        val text = extras.getString("android.text")?.toString() ?: ""

        val notification = NotificationEntity(packageName = packageName, title = title, text = text)

        GlobalScope.launch {
            db.notificationDao().insert(notification)
            Log.d("NotificationListener", "Notification saved: $notification")
        }
    }
}
