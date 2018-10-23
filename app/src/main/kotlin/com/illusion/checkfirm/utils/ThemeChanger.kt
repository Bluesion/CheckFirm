package com.illusion.checkfirm.utils

import android.app.Activity
import com.illusion.checkfirm.R

object ThemeChanger {
    fun setAppTheme(a: Activity) {
        val theme = a.getSharedPreferences("settings", Activity.MODE_PRIVATE)
        val dark = theme.getBoolean("dark", true)
        if (dark) {
            a.setTheme(R.style.AppTheme)
        } else {
            a.setTheme(R.style.DarkTheme)
        }
    }
}