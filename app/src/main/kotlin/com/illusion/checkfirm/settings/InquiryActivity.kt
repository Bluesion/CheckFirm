package com.illusion.checkfirm.settings

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.illusion.checkfirm.BuildConfig
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.ActivityInquiryBinding
import java.text.SimpleDateFormat
import java.util.*

class InquiryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInquiryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        toolbar.title = getString(R.string.inquiry)
        setSupportActionBar(toolbar)

        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val baseText = "\n\n\n*****\n" + "App version: " + BuildConfig.VERSION_NAME + "\nAndroid version: " + Build.VERSION.RELEASE +
                "\nDevice: " + sharedPrefs.getString("new_saved_model", "")!! + " (" + sharedPrefs.getString("new_saved_csc", "")!! + ")\n*****"

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        val date = SimpleDateFormat("yyyyMMdd", Locale.KOREAN).format(calendar.time)

        var title = ""
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            title = when (checkedId) {
                0 -> "[CheckFirm] NEW #$date"
                1 -> "[CheckFirm] BUG #$date"
                else -> "[CheckFirm] QUESTION #$date"
            }
        }

        binding.send.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("dnjscjf098@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, title)
            intent.putExtra(Intent.EXTRA_TEXT, binding.message.text.toString() + baseText)
            startActivity(intent)
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