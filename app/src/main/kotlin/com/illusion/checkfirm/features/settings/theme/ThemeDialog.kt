package com.illusion.checkfirm.features.settings.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.common.ui.base.CheckFirmBottomSheetDialogFragment
import com.illusion.checkfirm.databinding.DialogThemeBinding
import com.illusion.checkfirm.features.settings.viewmodel.SettingsViewModel
import com.illusion.checkfirm.features.settings.viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: API 32부터 지원하는 Dynamic color 대응하기
class ThemeDialog : CheckFirmBottomSheetDialogFragment<DialogThemeBinding>() {

    private val settingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater) = DialogThemeBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT == 28 && Build.MANUFACTURER != "samsung") {
            binding!!.systemLayout.visibility = View.GONE
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.settingsState.collect {
                    when (it.theme) {
                        "light" -> {
                            binding!!.light.isChecked = true
                            binding!!.dark.isChecked = false
                            binding!!.system.isChecked = false

                            binding!!.lightText.typeface = Typeface.DEFAULT_BOLD
                            binding!!.lightText.setTextColor(
                                resources.getColor(
                                    com.bluesion.oneui.R.color.oneui_primary,
                                    context?.theme
                                )
                            )
                        }

                        "dark" -> {
                            binding!!.light.isChecked = false
                            binding!!.dark.isChecked = true
                            binding!!.system.isChecked = false

                            binding!!.darkText.typeface = Typeface.DEFAULT_BOLD
                            binding!!.darkText.setTextColor(
                                resources.getColor(
                                    com.bluesion.oneui.R.color.oneui_primary,
                                    context?.theme
                                )
                            )
                        }

                        "system" -> {
                            binding!!.light.isChecked = false
                            binding!!.dark.isChecked = false
                            binding!!.system.isChecked = true
                        }
                    }
                }
            }
        }

        binding!!.light.setOnClickListener {
            binding!!.light.isChecked = true
            binding!!.dark.isChecked = false
            binding!!.system.isChecked = false
            settingsViewModel.setTheme("light")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        binding!!.dark.setOnClickListener {
            binding!!.light.isChecked = false
            binding!!.dark.isChecked = true
            binding!!.system.isChecked = false
            settingsViewModel.setTheme("dark")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        binding!!.system.setOnClickListener {
            binding!!.light.isChecked = false
            binding!!.dark.isChecked = false
            binding!!.system.isChecked = true
            settingsViewModel.setTheme("system")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        binding!!.lightLayout.setOnClickListener {
            binding!!.light.isChecked = true
            binding!!.dark.isChecked = false
            binding!!.system.isChecked = false
            settingsViewModel.setTheme("light")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        binding!!.darkLayout.setOnClickListener {
            binding!!.light.isChecked = false
            binding!!.dark.isChecked = true
            binding!!.system.isChecked = false
            settingsViewModel.setTheme("dark")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        binding!!.systemLayout.setOnClickListener {
            binding!!.light.isChecked = false
            binding!!.dark.isChecked = false
            binding!!.system.isChecked = true
            settingsViewModel.setTheme("system")
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        binding!!.dynamicLayout.setOnClickListener {
            val activity = getActivity(requireContext())
            activity?.recreate()
        }

        binding!!.ok.setOnClickListener {
            dismiss()
        }
    }

    private fun getActivity(context: Context): Activity? {
        var ctx: Context = context
        while (ctx is ContextWrapper) {
            if (ctx is Activity) {
                return ctx
            }
            ctx = ctx.baseContext
        }
        return null
    }
}
