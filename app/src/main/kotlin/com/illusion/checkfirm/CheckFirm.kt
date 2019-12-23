package com.illusion.checkfirm

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.messaging.FirebaseMessaging
import java.io.BufferedReader
import java.io.InputStreamReader

class CheckFirm : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().subscribeToTopic("update")

        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val savedModel = sharedPrefs.getString("saved_model", "")!!
        if (savedModel.isEmpty()) {
            val mEditor = sharedPrefs.edit()
            val tempModel = Build.MODEL
            val tempCsc = getCsc()
            val realCSC = if (tempCsc.isEmpty()) {
                getString(R.string.unknown)
            } else {
                tempCsc.substring(tempCsc.length - 8, tempCsc.length - 5)
            }
            mEditor.putString("saved_model", tempModel)
            mEditor.putString("saved_csc", realCSC)
            mEditor.apply()
        }

        if (Build.VERSION.SDK_INT >= 29) {
            when (sharedPrefs.getString("theme", "system")) {
                "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                "system" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        } else {
            when (sharedPrefs.getString("theme", "light")) {
                "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                "system" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    private fun getCsc(): String {
        val process = Runtime.getRuntime().exec(
                arrayOf("/system/bin/getprop", "ril.official_cscver")
        )
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        return reader.readLine()
    }
}