package tgs.app.maggot.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotificationDao {
    @Insert
    suspend fun insert(notification: NotificationEntity)

    @Query("SELECT * FROM notifications ORDER BY receivedAt DESC")
    suspend fun getAllNotifications(): List<NotificationEntity>
}
