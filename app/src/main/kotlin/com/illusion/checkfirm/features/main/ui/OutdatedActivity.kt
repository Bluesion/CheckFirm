package com.illusion.checkfirm.features.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.databinding.ActivityOutdatedBinding

class OutdatedActivity : CheckFirmActivity<ActivityOutdatedBinding>() {

    override fun createBinding() = ActivityOutdatedBinding.inflate(layoutInflater)
    override fun setContentInset() {
        setHorizontalInset(binding.root, 24)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.update.setOnClickListener {
            val goPlayStore = Intent(Intent.ACTION_VIEW)
            goPlayStore.data = ("market://details?id=" + applicationContext.packageName).toUri()
            startActivity(goPlayStore)
        }

        binding.close.setOnClickListener {
            finish()
        }
    }
}