package com.illusion.checkfirm.features.settings.help

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.features.bookmark.ui.BookmarkDialog
import com.illusion.checkfirm.databinding.ActivityHelpDeviceBinding
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.features.bookmark.viewmodel.BookmarkViewModel

class MyDeviceActivity : CheckFirmActivity<ActivityHelpDeviceBinding>() {

    private val bookmarkViewModel by viewModels<BookmarkViewModel>()

    override fun createBinding() = ActivityHelpDeviceBinding.inflate(layoutInflater)

    override fun setContentInset() {
        setBottomInset(binding.tipCard, 12)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(binding.toolbar, getString(R.string.help_device_info))

        val userName =
            Settings.Global.getString(contentResolver, Settings.Global.DEVICE_NAME)

        binding.userName.text = userName

        val model = Build.MODEL
        val csc = Tools.getCSC(this)

        binding.model.text = model
        binding.csc.text = csc

        val modelName = Settings.Global.getString(contentResolver, "default_device_name")
        if (modelName.isNullOrBlank()) {
            binding.deviceName.text = getString(R.string.unknown)
        } else {
            binding.deviceName.text = modelName
        }

        binding.editChip.setOnClickListener {
            startActivity(Intent(Settings.ACTION_DEVICE_INFO_SETTINGS))
        }

        binding.addBookmark.setOnClickListener {
            BookmarkDialog(onDialogClose = {
                bookmarkViewModel.addOrEditBookmark(it)
                Toast.makeText(
                    this@MyDeviceActivity,
                    getString(R.string.help_device_info_bookmark),
                    Toast.LENGTH_SHORT
                ).show()
            }).show(supportFragmentManager, null)
        }
    }
}
