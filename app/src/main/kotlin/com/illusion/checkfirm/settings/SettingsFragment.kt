package com.illusion.checkfirm.settings

import android.content.Context
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.illusion.checkfirm.BuildConfig
import com.illusion.checkfirm.R
import com.illusion.checkfirm.utils.Tools

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)

        val sharedPrefs = activity!!.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val dark = findPreference("dark")
        dark.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, _ ->
            val on = (preference as SwitchPreference).isChecked
            if (on) {
                editor.putBoolean("dark", true)
            } else {
                editor.putBoolean("dark", false)
            }
            editor.apply()
            Tools.restart(activity!!, 0)
            true
        }

        val saver = findPreference("saver")
        saver.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, _ ->
            val on = (preference as SwitchPreference).isChecked
            if (on) {
                editor.putBoolean("saver", true)
            } else {
                editor.putBoolean("saver", false)
            }
            editor.apply()
            Tools.restart(activity!!, 0)
            true
        }

        val version = findPreference("version")
        version.summary = BuildConfig.VERSION_NAME
    }
}