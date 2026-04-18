package com.illusion.checkfirm.feature.settings.about

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.net.toUri

fun Context.openAppSystemSettings() {
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = "package:${packageName}".toUri()
    })
}

fun Context.openPlayStore() {
    startActivity(Intent(Intent.ACTION_VIEW).apply {
        data = "https://play.google.com/store/apps/details?id=${packageName}".toUri()
    })
}

val Context.versionName: String
    get() = try {
        packageManager.getPackageInfo(packageName, 0).versionName ?: "Unknown"
    } catch (e: PackageManager.NameNotFoundException) {
        "Unknown"
    }
