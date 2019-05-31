package com.illusion.checkfirm.utils

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import java.text.SimpleDateFormat
import java.util.*

object Tools {
    fun isWifi(mContext: Context): Boolean {
        val manager = mContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (manager.isWifiEnabled) {
            val wifiInfo = manager.connectionInfo as WifiInfo
            if (wifiInfo.networkId == -1) {
                return false
            }
            return true
        } else {
            return false
        }
    }

    fun isOnline(mContext: Context): Boolean {
        val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    fun restart(activity: Activity, delay: Int) {
        var time = delay
        if (time == 0) {
            time = 1
        }

        val restartIntent = activity.packageManager.getLaunchIntentForPackage(activity.packageName)
        restartIntent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        val intent = PendingIntent.getActivity(activity, 0, restartIntent, PendingIntent.FLAG_ONE_SHOT)
        val manager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + time, intent)
        activity.setResult(Activity.RESULT_CANCELED)
        activity.finishAffinity()
    }

    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun dateToString(date: Date, format: String = "yyyy/MM/dd", locale: Locale = Locale.KOREAN): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(date)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}