package com.illusion.checkfirm.settings.help

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.ActivityHelpDeviceBinding
import com.illusion.checkfirm.dialogs.BookmarkDialog

class MyDeviceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHelpDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.title = getString(R.string.help_device_info)
        setSupportActionBar(toolbar)

        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val model = sharedPrefs.getString("new_saved_model", "")!!
        val csc = sharedPrefs.getString("new_saved_csc", "")!!
        binding.model.text = model
        binding.csc.text = csc

        binding.addBookmark.setOnClickListener {
            val bottomSheetFragment = BookmarkDialog.newInstance(false, 0, getString(R.string.my_device), model, csc)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
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
