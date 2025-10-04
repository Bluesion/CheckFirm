package com.illusion.checkfirm.features.welcome.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bluesion.oneui.switchcard.OneUISwitchCardListener
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.common.ui.recyclerview.CheckFirmDivider
import com.illusion.checkfirm.data.repository.WelcomeSearchRepository
import com.illusion.checkfirm.data.source.local.DatabaseProvider
import com.illusion.checkfirm.databinding.ActivityWelcomeSearchBinding
import com.illusion.checkfirm.features.settings.viewmodel.SettingsViewModel
import com.illusion.checkfirm.features.settings.viewmodel.SettingsViewModelFactory
import com.illusion.checkfirm.features.welcome.viewmodel.WelcomeSearchViewModel
import com.illusion.checkfirm.features.welcome.viewmodel.WelcomeSearchViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WelcomeSearchActivity : CheckFirmActivity<ActivityWelcomeSearchBinding>() {

    private lateinit var adapter: WelcomeSearchAdapter

    private val settingsViewModel by viewModels<SettingsViewModel> {
        SettingsViewModelFactory(
            (application as CheckFirm).repositoryProvider.getSettingsRepository()
        )
    }

    private val wsViewModel by viewModels<WelcomeSearchViewModel> {
        WelcomeSearchViewModelFactory(
            WelcomeSearchRepository(DatabaseProvider.getWelcomeSearchDao())
        )
    }

    private var showToolbarMenuIcon = false

    override fun createBinding() = ActivityWelcomeSearchBinding.inflate(layoutInflater)

    override fun setContentInset() {
        setBottomInset(binding.main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(binding.includeToolbar.appBar, getString(R.string.welcome_search))
        adapter =
            WelcomeSearchAdapter(
                deviceList = emptyList(),
                onItemClicked = {
                    wsViewModel.delete(it)
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

        lifecycleScope.launch(Dispatchers.Main) {
            binding.welcomeSwitchCard.setStatus(settingsViewModel.settingsState.first().isWelcomeSearchEnabled)

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                wsViewModel.allDevices.collect {
                    adapter.updateList(it)
                    if (it.isEmpty()) {
                        showToolbarMenuIcon = false
                        binding.savedDevicesLayout.visibility = View.GONE
                        binding.emptyButton.visibility = View.VISIBLE
                        binding.emptyText.visibility = View.VISIBLE
                    } else {
                        showToolbarMenuIcon = true
                        binding.savedDevicesLayout.visibility = View.VISIBLE
                        binding.emptyButton.visibility = View.GONE
                        binding.emptyText.visibility = View.GONE
                    }
                    invalidateOptionsMenu()
                }
            }
        }

        binding.welcomeSwitchCard.setSwitchCardListener(object : OneUISwitchCardListener {
            override fun onCheckedChange(isChecked: Boolean) {
                settingsViewModel.enableWelcomeSearch(isChecked)
            }
        })

        binding.emptyButton.setOnClickListener {
            addDevice()
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
        if (adapter.itemCount >= 5) {
            Toast.makeText(this, getString(R.string.multi_search_limit), Toast.LENGTH_SHORT).show()
        } else {
            WelcomeSearchDialog().show(supportFragmentManager, null)
        }
    }
}
