package com.illusion.checkfirm.dialogs

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.illusion.checkfirm.databinding.DialogThemeBinding

class ThemeDialog : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogThemeBinding.inflate(inflater)

        val light = binding.light
        val dark = binding.dark
        val system = binding.system

        val lightLayout = binding.lightLayout
        val darkLayout = binding.darkLayout
        val systemLayout = binding.systemLayout

        if (Build.VERSION.SDK_INT == 28) {
            if (Build.MANUFACTURER != "samsung") {
                systemLayout.visibility = View.GONE
            }
        } else if (Build.VERSION.SDK_INT < 28) {
            systemLayout.visibility = View.GONE
        }

        val sharedPrefs = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        val mEditor = sharedPrefs.edit()
        when (sharedPrefs.getString("theme", "light")) {
            "light" -> {
                light.isChecked = true
                dark.isChecked = false
                system.isChecked = false
            }
            "dark" -> {
                light.isChecked = false
                dark.isChecked = true
                system.isChecked = false
            }
            "system" -> {
                light.isChecked = false
                dark.isChecked = false
                system.isChecked = true
            }
        }

        light.setOnClickListener {
            light.isChecked = true
            dark.isChecked = false
            system.isChecked = false
            mEditor.putString("theme", "light")
            mEditor.apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            dismiss()
        }
        dark.setOnClickListener {
            light.isChecked = false
            dark.isChecked = true
            system.isChecked = false
            mEditor.putString("theme", "dark")
            mEditor.apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            dismiss()
        }
        system.setOnClickListener {
            light.isChecked = false
            dark.isChecked = false
            system.isChecked = true
            mEditor.putString("theme", "system")
            mEditor.apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            dismiss()
        }

        lightLayout.setOnClickListener {
            light.isChecked = true
            dark.isChecked = false
            system.isChecked = false
            mEditor.putString("theme", "light")
            mEditor.apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            dismiss()
        }
        darkLayout.setOnClickListener {
            light.isChecked = false
            dark.isChecked = true
            system.isChecked = false
            mEditor.putString("theme", "dark")
            mEditor.apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            dismiss()
        }
        systemLayout.setOnClickListener {
            light.isChecked = false
            dark.isChecked = false
            system.isChecked = true
            mEditor.putString("theme", "system")
            mEditor.apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            dismiss()
        }

        binding.ok.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}