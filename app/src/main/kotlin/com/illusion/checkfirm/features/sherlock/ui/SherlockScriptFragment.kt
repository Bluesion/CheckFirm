package com.illusion.checkfirm.features.sherlock.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.common.ui.base.CheckFirmFragment
import com.illusion.checkfirm.databinding.FragmentSherlockScriptBinding
import com.illusion.checkfirm.features.sherlock.util.SherlockStatus
import com.illusion.checkfirm.features.sherlock.viewmodel.SherlockViewModel
import com.illusion.checkfirm.features.sherlock.viewmodel.SherlockViewModelFactory
import kotlinx.coroutines.launch

class SherlockScriptFragment : CheckFirmFragment<FragmentSherlockScriptBinding>() {

    private val sherlockViewModel by activityViewModels<SherlockViewModel> {
        SherlockViewModelFactory(
            (requireActivity().application as CheckFirm).repositoryProvider.getSettingsRepository()
        )
    }

    override fun onCreateView(inflater: LayoutInflater) =
        FragmentSherlockScriptBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.scriptStart.setText(sherlockViewModel.scriptStart.value)
        binding!!.scriptEnd.setText(sherlockViewModel.scriptEnd.value)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sherlockViewModel.sherlockStatus.collect {
                    when (it) {
                        SherlockStatus.INITIAL -> {
                            binding!!.startButton.isEnabled = true
                        }

                        SherlockStatus.FAIL -> {
                            binding!!.startButton.isEnabled = true
                            binding!!.scriptStart.isEnabled = true
                            binding!!.scriptEnd.isEnabled = true
                            binding!!.controllerLayout.visibility = View.VISIBLE
                        }

                        SherlockStatus.RUNNING -> {
                            binding!!.startButton.isEnabled = false
                            binding!!.scriptStart.isEnabled = false
                            binding!!.scriptEnd.isEnabled = false
                            binding!!.controllerLayout.visibility = View.INVISIBLE
                        }

                        SherlockStatus.SUCCESS -> {
                            binding!!.startButton.isEnabled = true
                            binding!!.scriptStart.isEnabled = true
                            binding!!.scriptEnd.isEnabled = true
                            binding!!.controllerLayout.visibility = View.VISIBLE
                        }

                        SherlockStatus.NO_WARNING -> {
                            binding!!.startButton.isEnabled = true
                            binding!!.scriptStartLayout.error = null
                            binding!!.scriptEndLayout.error = null
                        }

                        SherlockStatus.WARNING_SCRIPT_START_INVALID -> {
                            binding!!.startButton.isEnabled = false
                            // 공백을 넣으면 에러 UI O, null 넣으면 에러 UI X
                            binding!!.scriptStartLayout.error = " "
                            binding!!.scriptEndLayout.error = " "
                        }

                        SherlockStatus.WARNING_SCRIPT_END_INVALID -> {
                            binding!!.startButton.isEnabled = false
                            binding!!.scriptStartLayout.error = " "
                            binding!!.scriptEndLayout.error = " "
                        }

                        else -> {
                            binding!!.startButton.isEnabled = false
                            binding!!.scriptStartLayout.error = " "
                            binding!!.scriptEndLayout.error = " "
                        }
                    }
                }
            }
        }

        binding!!.startButton.setOnClickListener {
            sherlockViewModel.runScript()
        }

        binding!!.scriptStart.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                sherlockViewModel.setScriptStart(s.toString())
            }
        })

        binding!!.scriptEnd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                sherlockViewModel.setScriptEnd(s.toString())
            }
        })
    }
}
