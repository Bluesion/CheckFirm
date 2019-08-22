package com.illusion.checkfirm.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.illusion.checkfirm.MainActivity
import com.illusion.checkfirm.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data != null) {
            sendNotification(remoteMessage.data["model"].toString(), remoteMessage.data["csc"].toString())
        }
    }

    private fun sendNotification(model: String, csc: String) {
        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val catcher = sharedPrefs.getBoolean("catcher", false)
        if (catcher) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("new_model", model)
                putExtra("new_csc", csc)
            }

            val channelID = getString(R.string.app_name)
            val notificationManager: NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelID, getString(R.string.notification_channel_name), NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }

            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            val notificationBuilder = NotificationCompat.Builder(this, channelID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(String.format(getString(R.string.info_catcher_notification_title), model, csc))
                    .setContentText(getString(R.string.info_catcher_notification_text))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
            notificationManager.notify(0, notificationBuilder.build())
        }
    }
}