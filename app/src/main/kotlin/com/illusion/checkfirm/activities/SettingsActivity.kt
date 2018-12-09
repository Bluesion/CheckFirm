package com.illusion.checkfirm.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.illusion.checkfirm.R
import com.illusion.checkfirm.utils.ThemeChanger

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeChanger.setAppTheme(this)
        setContentView(R.layout.activity_settings)

        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val one = sharedPrefs.getBoolean("one", true)
        val mAppBar = findViewById<AppBarLayout>(R.id.appbar)
        if (!one) {
            mAppBar.setExpanded(true)
        } else {
            mAppBar.setExpanded(false)
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}