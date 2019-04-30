package com.illusion.checkfirm

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class CheckFirm : Application() {

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val dark = sharedPrefs.getBoolean("dark", false)

        if (dark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}