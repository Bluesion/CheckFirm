package com.illusion.checkfirm.features.sherlock.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.illusion.checkfirm.R
import com.illusion.checkfirm.common.ui.base.CheckFirmFragment
import com.illusion.checkfirm.databinding.FragmentSherlockManualBinding
import com.illusion.checkfirm.features.sherlock.util.SherlockStatus
import com.illusion.checkfirm.features.sherlock.viewmodel.SherlockViewModel
import kotlinx.coroutines.launch

class SherlockManualFragment : CheckFirmFragment<FragmentSherlockManualBinding>() {

    private val sherlockViewModel by activityViewModels<SherlockViewModel>()

    override fun onCreateView(inflater: LayoutInflater) = FragmentSherlockManualBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sherlockViewModel.sherlockStatus.collect {
                    when (it) {
                        SherlockStatus.FAIL -> {
                            binding!!.buildCard.strokeColor = ContextCompat.getColor(requireContext(), com.bluesion.oneui.R.color.oneui_error)
                            binding!!.cscCard.strokeColor = ContextCompat.getColor(requireContext(), com.bluesion.oneui.R.color.oneui_error)
                            binding!!.basebandCard.strokeColor = ContextCompat.getColor(requireContext(), com.bluesion.oneui.R.color.oneui_error)
                        }
                        SherlockStatus.SUCCESS -> {
                            binding!!.buildCard.strokeColor = ContextCompat.getColor(requireContext(), com.bluesion.oneui.R.color.sesl_loading_progress_color1)
                            binding!!.cscCard.strokeColor = ContextCompat.getColor(requireContext(), com.bluesion.oneui.R.color.sesl_loading_progress_color1)
                            binding!!.basebandCard.strokeColor = ContextCompat.getColor(requireContext(), com.bluesion.oneui.R.color.sesl_loading_progress_color1)
                        }
                        else -> {
                            binding!!.buildCard.strokeColor = ContextCompat.getColor(requireContext(), com.bluesion.oneui.R.color.oneui_switch_thumb_off_stroke_color)
                            binding!!.cscCard.strokeColor = ContextCompat.getColor(requireContext(), com.bluesion.oneui.R.color.oneui_switch_thumb_off_stroke_color)
                            binding!!.basebandCard.strokeColor = ContextCompat.getColor(requireContext(), com.bluesion.oneui.R.color.oneui_switch_thumb_off_stroke_color)
                        }
                    }
                }
            }
        }

        binding!!.editTextBuildPrefix.setText(sherlockViewModel.buildPrefix.value)
        binding!!.editTextBuild.setText(sherlockViewModel.manualBuild.value)
        binding!!.editTextCscPrefix.setText(sherlockViewModel.cscPrefix.value)
        binding!!.editTextCsc.setText(sherlockViewModel.manualCsc.value)
        binding!!.editTextBasebandPrefix.setText(sherlockViewModel.basebandPrefix.value)
        binding!!.editTextBaseband.setText(sherlockViewModel.manualBaseband.value)

        binding!!.editTextBuildPrefix.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                sherlockViewModel.setManualBuildPrefix(s.toString())
            }
        })

        binding!!.editTextBuild.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                sherlockViewModel.setManualBuild(s.toString())
            }
        })

        binding!!.editTextCscPrefix.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                sherlockViewModel.setManualCscPrefix(s.toString())
            }
        })

        binding!!.editTextCsc.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                sherlockViewModel.setManualCsc(s.toString())
            }
        })

        binding!!.editTextBasebandPrefix.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                sherlockViewModel.setManualBasebandPrefix(s.toString())
            }
        })

        binding!!.editTextBaseband.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                sherlockViewModel.setManualBaseband(s.toString())
            }
        })
    }
}
