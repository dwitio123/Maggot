package tgs.app.maggot.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tgs.app.maggot.database.DatabaseProvider
import tgs.app.maggot.R
import tgs.app.maggot.activity.SmartFeederActivity
import tgs.app.maggot.database.NotificationEntity
import java.util.Date

class FirebaseNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MyFirebaseMessagingService", "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let {
            val message = it.body ?: "No message found"
            sendNotification(message)
            saveNotificationToDatabase(message)
        }
    }

    private fun sendNotification(message: String) {
        val intent = Intent(this, SmartFeederActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val channelId = "maggot_channel"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Peringatan")
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun saveNotificationToDatabase(message: String) {
        val database = DatabaseProvider.getDatabase(applicationContext)
        val notificationDao = database.notificationDao()
        val notification = NotificationEntity(message = message, receivedAt = Date())

        CoroutineScope(Dispatchers.IO).launch {
            notificationDao.insert(notification)
        }
    }
}
