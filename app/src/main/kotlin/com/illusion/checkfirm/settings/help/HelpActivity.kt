package com.illusion.checkfirm.settings.help

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()

        binding.helpManual.setOnClickListener {
            val intent = Intent(this, FirmwareManualActivity::class.java)
            startActivity(intent)
        }

        binding.helpDevice.setOnClickListener {
            val intent = Intent(this, MyDeviceActivity::class.java)
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

    private fun initToolbar() {
        setSupportActionBar(binding.includeToolbar.toolbar)

        val toolbarText = getString(R.string.help)
        val title = binding.includeToolbar.title
        title.text = toolbarText
        val expandedTitle = binding.includeToolbar.expandedTitle
        expandedTitle.text = toolbarText

        val mAppBar = binding.includeToolbar.appBar
        mAppBar.layoutParams.height = (resources.displayMetrics.heightPixels * 0.3976).toInt()
        mAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, _ ->
            val percentage = (appBarLayout.y / appBarLayout.totalScrollRange)
            expandedTitle.alpha = 1 - (percentage * 2 * -1)
            title.alpha = percentage * -1
        })

        val one = getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("one", true)
        if (one) {
            mAppBar.setExpanded(true)
        } else {
            mAppBar.setExpanded(false)
        }
    }
}
