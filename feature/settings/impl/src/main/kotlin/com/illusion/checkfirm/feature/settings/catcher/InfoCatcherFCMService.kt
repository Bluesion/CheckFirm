package com.illusion.checkfirm.feature.settings.catcher

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.illusion.checkfirm.core.preference.api.PreferenceRepository
import com.illusion.checkfirm.feature.settings.R
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.illusion.checkfirm.core.designsystem.R as DesignSystemR

@AndroidEntryPoint
class InfoCatcherFCMService : FirebaseMessagingService() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

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
        if (preferenceRepository.getSettings().first().isInfoCatcherEnabled) {
            val intent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("new_model", model)
                putExtra("new_csc", csc)
            } ?: Intent()

            val channelID = getString(DesignSystemR.string.app_name)
            val notificationManager =
                this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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
                .setSmallIcon(applicationInfo.icon)
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

        val channelID = getString(DesignSystemR.string.app_name)
        val notificationManager =
            this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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
            .setSmallIcon(applicationInfo.icon)
            .setContentTitle(getString(R.string.notification_new_update_title))
            .setContentText(getString(R.string.notification_new_update_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(token: String) = Unit

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
