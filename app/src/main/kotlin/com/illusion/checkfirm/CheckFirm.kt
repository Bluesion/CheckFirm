package com.illusion.checkfirm

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp
import com.illusion.checkfirm.core.designsystem.R as DesignSystemR

@HiltAndroidApp
class CheckFirm : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    /**
     * Mirror the legacy InfoCatcherActivity.onCreate channel-creation so the very
     * first FCM message routed through the service has a channel to land on.
     */
    private fun createNotificationChannel() {
        val name = getString(DesignSystemR.string.app_name)
        val channel = NotificationChannel(
            name,
            name,
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}
