package com.illusion.checkfirm.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.illusion.checkfirm.R

class ThemeDialog: BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.dialog_theme, container, false)

        val light = rootView.findViewById<RadioButton>(R.id.light)
        val dark = rootView.findViewById<RadioButton>(R.id.dark)
        val system = rootView.findViewById<RadioButton>(R.id.system)

        val lightLayout = rootView.findViewById<LinearLayout>(R.id.light_layout)
        val darkLayout = rootView.findViewById<LinearLayout>(R.id.dark_layout)
        val systemLayout = rootView.findViewById<LinearLayout>(R.id.system_layout)

        val sharedPrefs = activity!!.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val mEditor = sharedPrefs.edit()
        when (sharedPrefs.getString("theme", "light")) {
            "light" -> light.isChecked = true
            "dark" -> dark.isChecked = true
            "system" -> system.isChecked = true
        }

        lightLayout.setOnClickListener {
            mEditor.putString("theme", "light")
            mEditor.apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            dismiss()
        }
        darkLayout.setOnClickListener {
            mEditor.putString("theme", "dark")
            mEditor.apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            dismiss()
        }
        systemLayout.setOnClickListener {
            mEditor.putString("theme", "system")
            mEditor.apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            dismiss()
        }

        val okButton = rootView.findViewById<MaterialButton>(R.id.ok)
        okButton.setOnClickListener {
            dismiss()
        }

        return rootView
    }
}