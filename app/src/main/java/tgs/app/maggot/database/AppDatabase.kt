package tgs.app.maggot.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tgs.app.maggot.utils.Converters

@Database(entities = [NotificationEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}
