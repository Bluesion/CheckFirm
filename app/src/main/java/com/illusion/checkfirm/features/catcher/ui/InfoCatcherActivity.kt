package com.illusion.checkfirm.features.catcher.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bluesion.oneui.switchcard.OneUISwitchCardListener
import com.google.firebase.messaging.FirebaseMessaging
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.features.catcher.viewmodel.InfoCatcherViewModel
import com.illusion.checkfirm.databinding.ActivityInfoCatcherBinding
import com.illusion.checkfirm.common.ui.recyclerview.CheckFirmDivider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class InfoCatcherActivity : CheckFirmActivity<ActivityInfoCatcherBinding>() {

    private val icViewModel: InfoCatcherViewModel by viewModels()
    private var showToolbarMenuIcon = false

    override fun createBinding() = ActivityInfoCatcherBinding.inflate(layoutInflater)

    override fun setContentInset() {
        setBottomInset(binding.main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(binding.includeToolbar.appBar, getString(R.string.info_catcher))

        binding.catcherSwitchCard.setSwitchCardListener(object : OneUISwitchCardListener {
            override fun onCheckedChange(isChecked: Boolean) {
                settingsViewModel.enableInfoCatcher(isChecked)
            }
        })

        val adapter =
            InfoCatcherAdapter(
                deviceList = emptyList(),
                onItemClicked = {
                    icViewModel.delete(it)
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(it)
                }
            )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            CheckFirmDivider(
                ContextCompat.getDrawable(
                    this, R.drawable.checkfirm_divider
                )!!
            )
        )

        binding.emptyButton.setOnClickListener {
            addDevice()
        }

        lifecycleScope.launch(Dispatchers.Main) {
            if (settingsViewModel.settingsState.first().isInfoCatcherEnabled) {
                val channelID = getString(R.string.app_name)
                val notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val channel = NotificationChannel(
                    channelID,
                    getString(R.string.notification_channel_new_firmware),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)

                binding.catcherSwitchCard.turnOn()
            } else {
                binding.catcherSwitchCard.turnOff()
            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                icViewModel.allDevices.collect {
                    adapter.setDevices(it)
                    if (it.isEmpty()) {
                        showToolbarMenuIcon = false
                        binding.savedDevicesLayout.visibility = View.GONE
                        binding.emptyButton.visibility = View.VISIBLE
                    } else {
                        showToolbarMenuIcon = true
                        binding.savedDevicesLayout.visibility = View.VISIBLE
                        binding.emptyButton.visibility = View.GONE
                    }
                    invalidateOptionsMenu()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (!showToolbarMenuIcon) {
            return false
        }
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                addDevice()
                return true
            }

            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addDevice() {
        InfoCatcherDialog()
            .show(supportFragmentManager, null)
    }
}
