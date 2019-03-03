package com.illusion.checkfirm.utils

import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import com.illusion.checkfirm.R

object ThemeChanger {
    fun setAppTheme(a: Activity) {
        val theme = a.getSharedPreferences("settings", Activity.MODE_PRIVATE)
        val dark = theme.getBoolean("dark", false)
        val system = theme.getBoolean("system", false)
        val uiModeManager = a.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        if (system) {
            if (uiModeManager.nightMode == 2) {
                a.setTheme(R.style.DarkTheme)
            } else if (uiModeManager.nightMode == 1) {
                a.setTheme(R.style.AppTheme)
            }
        } else {
            if (dark) {
                a.setTheme(R.style.DarkTheme)
            } else  {
                a.setTheme(R.style.AppTheme)
            }
        }
    }
}