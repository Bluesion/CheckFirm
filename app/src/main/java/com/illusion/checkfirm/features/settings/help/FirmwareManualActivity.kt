package com.illusion.checkfirm.features.settings.help

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmActivity
import com.illusion.checkfirm.databinding.ActivityHelpFirmwareManualBinding

class FirmwareManualActivity : CheckFirmActivity<ActivityHelpFirmwareManualBinding>() {

    override fun createBinding() = ActivityHelpFirmwareManualBinding.inflate(layoutInflater)

    override fun setContentInset() {
        setBottomInset(binding.buildView, 12)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(binding.toolbar, getString(R.string.help_manual))

        binding.tabLayout.newTab().setText(getString(R.string.help_firmware_manual_build)).let {
            binding.tabLayout.addTab(it)
        }

        binding.tabLayout.newTab().setText(getString(R.string.help_firmware_manual_csc)).let {
            binding.tabLayout.addTab(it)
        }

        binding.tabLayout.newTab().setText(getString(R.string.help_firmware_manual_baseband)).let {
            binding.tabLayout.addTab(it)
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        binding.commonDescription.text =
                            getString(R.string.help_firmware_manual_build_description)
                        binding.buildView.visibility = View.VISIBLE
                    }

                    1 -> {
                        binding.commonDescription.text =
                            getString(R.string.help_firmware_manual_csc_description)
                        binding.buildView.visibility = View.GONE
                    }

                    else -> {
                        binding.commonDescription.text =
                            getString(R.string.help_firmware_manual_baseband_description)
                        binding.buildView.visibility = View.GONE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        binding.deviceCard.setOnClickListener {
            binding.detailTitle.text = getString(R.string.help_firmware_manual_build_device)
            binding.detailText.text =
                getString(R.string.help_firmware_manual_build_device_description)
        }

        binding.regionCard.setOnClickListener {
            binding.detailTitle.text = getString(R.string.help_firmware_manual_build_region)
            binding.detailText.text =
                getString(R.string.help_firmware_manual_build_region_description)
        }

        binding.bootloaderCard.setOnClickListener {
            binding.detailTitle.text = getString(R.string.help_firmware_manual_build_bootloader)
            binding.detailText.text =
                getString(R.string.help_firmware_manual_build_bootloader_description)
        }

        binding.oneUiCard.setOnClickListener {
            binding.detailTitle.text = getString(R.string.help_firmware_manual_build_one_ui)
            binding.detailText.text =
                getString(R.string.help_firmware_manual_build_one_ui_description)
        }

        binding.yearCard.setOnClickListener {
            binding.detailTitle.text = getString(R.string.help_firmware_manual_build_year)
            binding.detailText.text =
                getString(R.string.help_firmware_manual_build_year_description)
        }

        binding.monthCard.setOnClickListener {
            binding.detailTitle.text = getString(R.string.help_firmware_manual_build_month)
            binding.detailText.text =
                getString(R.string.help_firmware_manual_build_month_description)
        }

        binding.revisionCard.setOnClickListener {
            binding.detailTitle.text = getString(R.string.help_firmware_manual_build_revision)
            binding.detailText.text =
                getString(R.string.help_firmware_manual_build_revision_description)
        }
    }
}