package com.illusion.checkfirm.features.main.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.data.model.local.FirmwareItem
import com.illusion.checkfirm.data.model.local.SearchResultItem
import com.illusion.checkfirm.data.model.local.UpdateType
import com.illusion.checkfirm.databinding.RowMainSearchResultItemsBinding

class MainViewHolder(
    private val binding: RowMainSearchResultItemsBinding,
    private val onCardClicked: (isOfficialCard: Boolean, searchResult: SearchResultItem) -> Unit,
    private val onCardLongClicked: (firmware: String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(searchResult: SearchResultItem, isFirebaseEnabled: Boolean) {
        binding.officialFirmwareCard.setOnClickListener {
            onCardClicked(true, searchResult)
        }

        binding.officialFirmwareCard.setOnLongClickListener {
            onCardLongClicked(binding.officialFirmwareText.text.toString())
            return@setOnLongClickListener true
        }

        binding.testFirmwareCard.setOnClickListener {
            onCardClicked(false, searchResult)
        }

        binding.testFirmwareCard.setOnLongClickListener {
            onCardLongClicked(binding.testFirmwareText.text.toString())
            return@setOnLongClickListener true
        }

        // COMMON
        binding.device.text =
            String.format(
                binding.device.context.getString(R.string.device_format_1),
                searchResult.device.model,
                searchResult.device.csc
            )

        // OFFICIAL
        if (searchResult.firmware.officialFirmwareItem.latestFirmware.isBlank()) {
            binding.officialFirmwareText.let {
                it.text = it.context.getString(R.string.search_result_error)
            }
            binding.officialFirmwareText.setTextColor(
                binding.officialFirmwareText.context.getColor(
                    com.bluesion.oneui.R.color.oneui_error
                )
            )
            binding.officialFirmwareCardDivider.visibility = View.GONE
            binding.smartSearchOfficialDeviceLayout.visibility = View.GONE
            binding.smartSearchOfficialDateLayout.visibility = View.GONE
            binding.smartSearchOfficialAndroidVersionLayout.visibility = View.GONE
        } else {
            binding.officialFirmwareCardDivider.visibility = View.VISIBLE
            binding.smartSearchOfficialDeviceLayout.visibility = View.VISIBLE
            binding.smartSearchOfficialDateLayout.visibility = View.VISIBLE
            binding.smartSearchOfficialAndroidVersionLayout.visibility = View.VISIBLE

            binding.officialFirmwareText.setTextColor(
                binding.officialFirmwareText.context.getColor(
                    com.bluesion.oneui.R.color.oneui_onSurface
                )
            )
            binding.officialFirmwareText.text =
                Tools.getShortBuildInfo(searchResult.firmware.officialFirmwareItem.latestFirmware)

            binding.smartSearchOfficialDeviceText.text =
                searchResult.firmware.officialFirmwareItem.deviceName
            binding.smartSearchOfficialDateText.text =
                searchResult.firmware.officialFirmwareItem.releaseDate
            binding.smartSearchOfficialAndroidVersionText.text =
                searchResult.firmware.officialFirmwareItem.androidVersion
        }

        // TEST
        if (searchResult.firmware.testFirmwareItem.latestFirmware.isBlank()) {
            binding.testFirmwareText.let {
                it.text = it.context.getString(R.string.search_result_error)
            }
            binding.testFirmwareText.setTextColor(binding.testFirmwareText.context.getColor(com.bluesion.oneui.R.color.oneui_error))
            binding.testFirmwareCardDivider.visibility = View.GONE
            binding.smartSearchTestDiscovererLayout.visibility = View.GONE
            binding.smartSearchTestDateLayout.visibility = View.GONE
            binding.smartSearchTestDynamicLayout.visibility = View.GONE
        } else {
            binding.testFirmwareText.setTextColor(binding.testFirmwareText.context.getColor(com.bluesion.oneui.R.color.oneui_onSurface))
            binding.testFirmwareText.text =
                Tools.getShortBuildInfo(searchResult.firmware.testFirmwareItem.latestFirmware)
            showDetail(searchResult.firmware, isFirebaseEnabled)
        }

        if (searchResult.firmware.testFirmwareItem.decryptedFirmware.isNotBlank()) {
            binding.testFirmwareText.setTextColor(binding.testFirmwareText.context.getColor(com.bluesion.oneui.R.color.oneui_onSurface))
            binding.testFirmwareText.text =
                Tools.getShortBuildInfo(searchResult.firmware.testFirmwareItem.decryptedFirmware)
            showDetail(searchResult.firmware, isFirebaseEnabled)
        }

        if (searchResult.firmware.testFirmwareItem.clue.isNotBlank()) {
            binding.testFirmwareText.setTextColor(binding.testFirmwareText.context.getColor(com.bluesion.oneui.R.color.oneui_onSurface))
            binding.testFirmwareText.text =
                Tools.getShortBuildInfo(searchResult.firmware.testFirmwareItem.clue)
            showDetail(searchResult.firmware, isFirebaseEnabled)
        }
    }

    private fun showDetail(firmwareItem: FirmwareItem, isFirebaseEnabled: Boolean) {
        binding.testFirmwareCardDivider.visibility = View.VISIBLE
        binding.smartSearchTestDiscovererLayout.visibility = View.VISIBLE
        binding.smartSearchTestDateLayout.visibility = View.VISIBLE
        binding.smartSearchTestDynamicLayout.visibility = View.VISIBLE

        if (firmwareItem.testFirmwareItem.androidVersion.isBlank()) {
            binding.smartSearchTestDynamicIcon.setImageResource(R.drawable.ic_smart_search_firmware_type)
            binding.smartSearchTestDynamicText.text =
                when (firmwareItem.testFirmwareItem.updateType) {
                    UpdateType.MAJOR -> binding.smartSearchTestDynamicText.context.getString(R.string.smart_search_type_major)
                    UpdateType.MINOR -> binding.smartSearchTestDynamicText.context.getString(R.string.smart_search_type_minor)
                    UpdateType.ROLLBACK -> binding.smartSearchTestDynamicText.context.getString(R.string.smart_search_type_rollback)
                    else -> binding.smartSearchTestDynamicText.context.getString(R.string.unknown)
                }
        } else {
            binding.smartSearchTestDynamicIcon.setImageResource(R.drawable.ic_smart_search_android_version)
            binding.smartSearchTestDynamicText.text =
                firmwareItem.testFirmwareItem.androidVersion
        }

        if (isFirebaseEnabled) {
            binding.smartSearchTestDateText.text =
                firmwareItem.testFirmwareItem.discoveryDate

            if (firmwareItem.testFirmwareItem.watson.isBlank()) {
                binding.smartSearchTestDiscovererText.text =
                    firmwareItem.testFirmwareItem.discoverer
            } else {
                binding.smartSearchTestDiscovererText.text =
                    firmwareItem.testFirmwareItem.watson
            }
        } else {
            binding.smartSearchTestDateText.text = Tools.dateToString(Tools.getCurrentDateTime())
            binding.smartSearchTestDiscovererText.text =
                binding.smartSearchTestDiscovererText.context.getString(R.string.unknown)
        }
    }
}