package com.illusion.checkfirm.features.catcher.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.illusion.checkfirm.R
import com.illusion.checkfirm.features.main.ui.MainActivity
import com.illusion.checkfirm.data.source.local.SettingsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class InfoCatcherFCMService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data["model"] == "update" || remoteMessage.data["csc"] == "update") {
            updateNotification()
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                sendNotification(
                    remoteMessage.data["model"].toString(),
                    remoteMessage.data["csc"].toString()
                )
            }
        }
    }

    private suspend fun sendNotification(model: String, csc: String) {
        if (SettingsDataSource(this).isInfoCatcherEnabled.first()) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("new_model", model)
                putExtra("new_csc", csc)
            }

            val channelID = getString(R.string.app_name)
            val notificationManager: NotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                channelID,
                getString(R.string.notification_channel_new_firmware),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)

            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
            val notificationBuilder = NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(
                    String.format(
                        getString(R.string.notification_info_catcher_title),
                        model,
                        csc
                    )
                )
                .setContentText(getString(R.string.notification_info_catcher_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
            notificationManager.notify(0, notificationBuilder.build())
        }
    }

    private fun updateNotification() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            ("market://details?id=" + applicationContext.packageName).toUri()
        )

        val channelID = getString(R.string.app_name)
        val notificationManager: NotificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            channelID,
            getString(R.string.notification_channel_new_update),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
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