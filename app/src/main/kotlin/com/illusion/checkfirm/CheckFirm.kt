package com.illusion.checkfirm

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class CheckFirm : Application() {

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        when (sharedPrefs.getString("theme", "light")) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "system" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}