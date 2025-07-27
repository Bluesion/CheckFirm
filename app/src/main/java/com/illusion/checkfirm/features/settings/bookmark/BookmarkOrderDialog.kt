package com.illusion.checkfirm.features.settings.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmBottomSheetDialogFragment
import com.illusion.checkfirm.databinding.DialogBookmarkOrderBinding
import com.illusion.checkfirm.features.settings.viewmodel.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkOrderDialog : CheckFirmBottomSheetDialogFragment<DialogBookmarkOrderBinding>(),
    CompoundButton.OnCheckedChangeListener {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater) =
        DialogBookmarkOrderBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.settingsState.collect {
                    when (it.bookmarkOrder) {
                        "time" -> {
                            binding!!.time.isChecked = true
                            binding!!.device.isChecked = false
                            binding!!.name.isChecked = false
                        }

                        "device" -> {
                            binding!!.time.isChecked = false
                            binding!!.device.isChecked = true
                            binding!!.name.isChecked = false
                        }

                        "name" -> {
                            binding!!.time.isChecked = false
                            binding!!.device.isChecked = false
                            binding!!.name.isChecked = true
                        }

                        else -> {
                            binding!!.time.isChecked = true
                            binding!!.device.isChecked = false
                            binding!!.name.isChecked = false
                        }
                    }

                    binding!!.descSwitch.isChecked = !it.isBookmarkAscOrder
                }
            }
        }

        binding!!.descSwitch.setOnCheckedChangeListener(this)

        binding!!.time.setOnClickListener {
            binding!!.time.isChecked = true
            binding!!.device.isChecked = false
            binding!!.name.isChecked = false
            settingsViewModel.setBookmarkOrder("time")
        }

        binding!!.device.setOnClickListener {
            binding!!.time.isChecked = false
            binding!!.device.isChecked = true
            binding!!.name.isChecked = false
            settingsViewModel.setBookmarkOrder("device")
        }

        binding!!.name.setOnClickListener {
            binding!!.time.isChecked = false
            binding!!.device.isChecked = false
            binding!!.name.isChecked = true
            settingsViewModel.setBookmarkOrder("name")
        }

        binding!!.timeLayout.setOnClickListener {
            binding!!.time.performClick()
        }

        binding!!.deviceLayout.setOnClickListener {
            binding!!.device.performClick()
        }

        binding!!.nameLayout.setOnClickListener {
            binding!!.name.performClick()
        }

        binding!!.descLayout.setOnClickListener {
            binding!!.descSwitch.toggle()
        }

        binding!!.ok.setOnClickListener {
            dismiss()
        }
    }

    override fun onCheckedChanged(p0: CompoundButton, isChecked: Boolean) {
        when (p0.id) {
            R.id.desc_switch -> {
                settingsViewModel.setBookmarkAscOrder(!isChecked)
            }
        }
    }
}
