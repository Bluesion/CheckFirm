package com.illusion.checkfirm

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.color.DynamicColors
import com.google.firebase.messaging.FirebaseMessaging
import com.illusion.checkfirm.data.model.FirmwareItem
import com.illusion.checkfirm.data.model.OfficialFirmwareItem
import com.illusion.checkfirm.data.model.TestFirmwareItem
import com.illusion.checkfirm.data.source.local.SettingsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CheckFirm : Application() {

    companion object {
        var searchModel = arrayOf("", "", "", "", "")
        var searchCSC = arrayOf("", "", "", "", "")
        var firmwareItems = arrayOf(
            FirmwareItem(
                OfficialFirmwareItem(),
                TestFirmwareItem()
            ),
            FirmwareItem(
                OfficialFirmwareItem(),
                TestFirmwareItem()
            ),
            FirmwareItem(
                OfficialFirmwareItem(),
                TestFirmwareItem()
            ),
            FirmwareItem(
                OfficialFirmwareItem(),
                TestFirmwareItem()
            ),
            FirmwareItem(
                OfficialFirmwareItem(),
                TestFirmwareItem()
            )
        )
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseMessaging.getInstance().subscribeToTopic("update")

        val settingsDataSource = SettingsDataSource(this)
        CoroutineScope(Dispatchers.Main).launch {
            val theme = settingsDataSource.getTheme.first()
            when (theme) {
                "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                "system" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }

            if (settingsDataSource.isDynamicColorEnabled.first() && DynamicColors.isDynamicColorAvailable()) {
                DynamicColors.applyToActivitiesIfAvailable(this@CheckFirm)
            }
        }
    }
}
