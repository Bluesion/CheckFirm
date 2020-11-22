package com.illusion.checkfirm

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.illusion.checkfirm.search.SearchResultItem
import java.io.BufferedReader
import java.io.InputStreamReader

class CheckFirm : Application() {

    companion object {
        lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory
        var searchModel = arrayOf("", "", "", "")
        var searchCSC = arrayOf("", "", "", "")
        var searchResult = arrayOf(
            SearchResultItem("", "", sortedMapOf(), "", "", sortedMapOf(), "", "", "", "", "", ""),
            SearchResultItem("", "", sortedMapOf(), "", "", sortedMapOf(), "", "", "", "", "", ""),
            SearchResultItem("", "", sortedMapOf(), "", "", sortedMapOf(), "", "", "", "", "", ""),
            SearchResultItem("", "", sortedMapOf(), "", "", sortedMapOf(), "", "", "", "", "", "")
        )
    }

    override fun onCreate() {
        super.onCreate()
        viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)

        FirebaseMessaging.getInstance().subscribeToTopic("update")

        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val savedModel = sharedPrefs.getString("new_saved_model", "")!!
        if (savedModel.isEmpty()) {
            val mEditor = sharedPrefs.edit()
            val tempModel = Build.MODEL
            val tempCsc = getCsc()
            val realCSC = if (tempCsc.isEmpty()) {
                getString(R.string.unknown)
            } else {
                tempCsc
            }
            mEditor.putString("new_saved_model", tempModel)
            mEditor.putString("new_saved_csc", realCSC)
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
                arrayOf("/system/bin/getprop", "ro.csc.sales_code")
        )
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        return reader.readLine()
    }
}
