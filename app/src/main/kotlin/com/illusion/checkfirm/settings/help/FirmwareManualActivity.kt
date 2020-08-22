package com.illusion.checkfirm.settings.help

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.ActivityHelpFirmwareManualBinding

class FirmwareManualActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHelpFirmwareManualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.title = getString(R.string.help_manual)
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
