package com.obedhack.capturedeletedmessages

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotificationDao {
    @Insert
    fun insert(notification: NotificationEntity)

    @Query("SELECT * FROM notifications")
    fun getAllNotifications(): List<NotificationEntity>
}
