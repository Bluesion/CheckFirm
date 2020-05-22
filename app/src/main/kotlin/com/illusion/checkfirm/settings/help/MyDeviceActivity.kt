package com.illusion.checkfirm.settings.help

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.R

class MyDeviceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_device)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = getString(R.string.help_device_info)
        setSupportActionBar(toolbar)

        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val myModel = findViewById<MaterialTextView>(R.id.model)
        val myCsc = findViewById<MaterialTextView>(R.id.csc)
        myModel.text = sharedPrefs.getString("new_saved_model", "")!!
        myCsc.text = sharedPrefs.getString("new_saved_csc", "")!!
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