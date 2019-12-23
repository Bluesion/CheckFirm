package com.illusion.checkfirm.fcm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.illusion.checkfirm.main.MainActivity
import com.illusion.checkfirm.R

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data["model"] == "update" || remoteMessage.data["csc"] == "update") {
            updateNotification()
        } else {
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
                val channel = NotificationChannel(channelID, getString(R.string.notification_channel_new_firmware), NotificationManager.IMPORTANCE_DEFAULT)
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

    private fun updateNotification() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + applicationContext.packageName))

        val channelID = getString(R.string.app_name)
        val notificationManager: NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelID, getString(R.string.notification_channel_new_update), NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder = NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.notification_new_update_title))
                .setContentText(getString(R.string.notification_new_update_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
        notificationManager.notify(0, notificationBuilder.build())
    }
}