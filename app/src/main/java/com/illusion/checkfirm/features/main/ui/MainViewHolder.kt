package com.illusion.checkfirm.features.main.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.databinding.RowMainSearchResultItemsBinding

class MainViewHolder(
    private val binding: RowMainSearchResultItemsBinding,
    private val isFirebaseEnabled: Boolean,
    private val onCardClicked: (isOfficialCard: Boolean, position: Int) -> Unit,
    private val onCardLongClicked: (firmware: String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.officialFirmwareCard.setOnClickListener {
            onCardClicked(true, absoluteAdapterPosition)
        }
        
        binding.officialFirmwareCard.setOnLongClickListener {
            onCardLongClicked(binding.officialFirmwareText.text.toString())
            return@setOnLongClickListener true
        }

        binding.testFirmwareCard.setOnClickListener {
            onCardClicked(false, absoluteAdapterPosition)
        }
        
        binding.testFirmwareCard.setOnLongClickListener {
            onCardLongClicked(binding.testFirmwareText.text.toString())
            return@setOnLongClickListener true
        }

        // COMMON
        val model = CheckFirm.searchModel[absoluteAdapterPosition]
        val csc = CheckFirm.searchCSC[absoluteAdapterPosition]
        binding.device.text =
            String.format(binding.device.context.getString(R.string.device_format_1), model, csc)

        // OFFICIAL
        if (CheckFirm.firmwareItems[absoluteAdapterPosition].officialFirmwareItem.latestFirmware.isBlank()) {
            binding.officialFirmwareText.let {
                it.text = it.context.getString(R.string.search_result_error)
            }
            binding.officialFirmwareText.setTextColor(binding.officialFirmwareText.context.getColor(com.bluesion.oneui.R.color.oneui_error))
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
                Tools.getFirmwareInfo(CheckFirm.firmwareItems[absoluteAdapterPosition].officialFirmwareItem.latestFirmware)
                    .substring(2)

            binding.smartSearchOfficialDeviceText.text =
                CheckFirm.firmwareItems[absoluteAdapterPosition].officialFirmwareItem.deviceName
            binding.smartSearchOfficialDateText.text =
                CheckFirm.firmwareItems[absoluteAdapterPosition].officialFirmwareItem.releaseDate
            binding.smartSearchOfficialAndroidVersionText.text =
                CheckFirm.firmwareItems[absoluteAdapterPosition].officialFirmwareItem.androidVersion
        }

        // TEST
        if (CheckFirm.firmwareItems[absoluteAdapterPosition].testFirmwareItem.latestFirmware.isBlank()) {
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
                Tools.getFirmwareInfo(CheckFirm.firmwareItems[absoluteAdapterPosition].testFirmwareItem.latestFirmware)
                    .substring(2)
            showDetail()
        }

        if (CheckFirm.firmwareItems[absoluteAdapterPosition].testFirmwareItem.decryptedFirmware.isNotBlank()) {
            binding.testFirmwareText.setTextColor(binding.testFirmwareText.context.getColor(com.bluesion.oneui.R.color.oneui_onSurface))
            binding.testFirmwareText.text =
                Tools.getFirmwareInfo(CheckFirm.firmwareItems[absoluteAdapterPosition].testFirmwareItem.decryptedFirmware)
                    .substring(2)
            showDetail()
        }

        if (CheckFirm.firmwareItems[absoluteAdapterPosition].testFirmwareItem.clue.isNotBlank()) {
            binding.testFirmwareText.setTextColor(binding.testFirmwareText.context.getColor(com.bluesion.oneui.R.color.oneui_onSurface))
            binding.testFirmwareText.text =
                Tools.getFirmwareInfo(CheckFirm.firmwareItems[absoluteAdapterPosition].testFirmwareItem.clue)
                    .substring(2)
            showDetail()
        }
    }

    private fun showDetail() {
        binding.testFirmwareCardDivider.visibility = View.VISIBLE
        binding.smartSearchTestDiscovererLayout.visibility = View.VISIBLE
        binding.smartSearchTestDateLayout.visibility = View.VISIBLE
        binding.smartSearchTestDynamicLayout.visibility = View.VISIBLE

        if (CheckFirm.firmwareItems[absoluteAdapterPosition].testFirmwareItem.androidVersion == binding.smartSearchTestDynamicText.context.getString(
                R.string.unknown
            )
        ) {
            binding.smartSearchTestDynamicIcon.setImageResource(R.drawable.ic_smart_search_firmware_type)
            binding.smartSearchTestDynamicText.text =
                CheckFirm.firmwareItems[absoluteAdapterPosition].testFirmwareItem.updateType
        } else {
            binding.smartSearchTestDynamicIcon.setImageResource(R.drawable.ic_smart_search_android_version)
            binding.smartSearchTestDynamicText.text =
                CheckFirm.firmwareItems[absoluteAdapterPosition].testFirmwareItem.androidVersion
        }

        if (isFirebaseEnabled) {
            binding.smartSearchTestDateText.text =
                CheckFirm.firmwareItems[absoluteAdapterPosition].testFirmwareItem.discoveryDate

            if (CheckFirm.firmwareItems[absoluteAdapterPosition].testFirmwareItem.watson.isBlank()) {
                binding.smartSearchTestDiscovererText.text =
                    CheckFirm.firmwareItems[absoluteAdapterPosition].testFirmwareItem.discoverer
            } else {
                binding.smartSearchTestDiscovererText.text =
                    CheckFirm.firmwareItems[absoluteAdapterPosition].testFirmwareItem.watson
            }
        } else {
            binding.smartSearchTestDateText.text = Tools.dateToString(Tools.getCurrentDateTime())
            binding.smartSearchTestDiscovererText.text =
                binding.smartSearchTestDiscovererText.context.getString(R.string.unknown)
        }
    }
}