package com.illusion.checkfirm.features.settings.help

import android.content.Intent
import android.os.Bundle
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.databinding.ActivityHelpBinding

class HelpActivity : CheckFirmActivity<ActivityHelpBinding>() {

    override fun createBinding() = ActivityHelpBinding.inflate(layoutInflater)

    override fun setContentInset() {
        setBottomInset(binding.main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(binding.includeToolbar.appBar, getString(R.string.help))

        binding.helpManual.setOnClickListener {
            startActivity(Intent(this, FirmwareManualActivity::class.java))
        }

        binding.helpDevice.setOnClickListener {
            startActivity(Intent(this, MyDeviceActivity::class.java))
        }
    }
}
