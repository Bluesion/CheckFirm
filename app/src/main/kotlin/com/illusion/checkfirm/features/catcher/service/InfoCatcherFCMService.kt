package com.illusion.checkfirm.features.catcher.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.features.main.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class InfoCatcherFCMService : FirebaseMessagingService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data["model"] == "update" || remoteMessage.data["csc"] == "update") {
            updateNotification()
        } else {
            serviceScope.launch {
                sendNotification(
                    remoteMessage.data["model"].toString(),
                    remoteMessage.data["csc"].toString()
                )
            }
        }
    }

    private suspend fun sendNotification(model: String, csc: String) {
        if ((application as CheckFirm).repositoryProvider.getSettingsRepository()
                .isInfoCatcherEnabled().first()
        ) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("new_model", model)
                putExtra("new_csc", csc)
            }

            val channelID = getString(R.string.app_name)
            val notificationManager =
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
        val notificationManager =
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

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("CHECKFIRM_FCM_TOKEN", token)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}