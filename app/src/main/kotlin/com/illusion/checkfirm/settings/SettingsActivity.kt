package com.illusion.checkfirm.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.switchmaterial.SwitchMaterial
import com.illusion.checkfirm.R
import com.illusion.checkfirm.databinding.ActivitySettingsBinding
import com.illusion.checkfirm.dialogs.ProfileDialog
import com.illusion.checkfirm.dialogs.ThemeDialog
import com.illusion.checkfirm.settings.about.AboutActivity
import com.illusion.checkfirm.settings.catcher.InfoCatcherActivity
import com.illusion.checkfirm.settings.help.HelpActivity
import com.illusion.checkfirm.settings.welcome.WelcomeSearchActivity

class SettingsActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsPrefs: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor
    private lateinit var welcomeSwitch: SwitchMaterial
    private lateinit var orderSwitch: SwitchMaterial
    private lateinit var quickSwitch: SwitchMaterial
    private lateinit var catcherSwitch: SwitchMaterial
    private lateinit var firebaseSwitch: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingsPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        welcomeSwitch = binding.welcomeSwitch
        orderSwitch = binding.orderSwitch
        quickSwitch = binding.quickSwitch
        catcherSwitch = binding.catcherSwitch
        firebaseSwitch = binding.firebaseSwitch
        initSwitch()
        initToolbar()

        binding.userName.text = settingsPrefs.getString("profile_user_name", "Unknown")

        binding.profileLayout.setOnClickListener {
            val bottomSheetDialog = ProfileDialog()
            bottomSheetDialog.setOnDialogCloseListener(object : ProfileDialog.OnDialogCloseListener {
                override fun onDialogClose() {
                    binding.userName.text = settingsPrefs.getString("profile_user_name", "Unknown")
                }
            })
            bottomSheetDialog.show(supportFragmentManager, bottomSheetDialog.tag)
        }

        binding.themeLayout.setOnClickListener {
            val bottomSheetFragment = ThemeDialog()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.welcomeLayout.setOnClickListener {
            val intent = Intent(this, WelcomeSearchActivity::class.java)
            startActivity(intent)
        }

        binding.orderLayout.setOnClickListener {
            orderSwitch.toggle()
        }

        binding.quickLayout.setOnClickListener {
            quickSwitch.toggle()
        }

        binding.catcherLayout.setOnClickListener {
            val intent = Intent(this, InfoCatcherActivity::class.java)
            startActivity(intent)
        }

        binding.firebaseLayout.setOnClickListener {
            firebaseSwitch.toggle()
        }

        binding.helpLayout.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }

        binding.aboutLayout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        binding.inquiryLayout.setOnClickListener {
            val intent = Intent(this, InquiryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCheckedChanged(p0: CompoundButton, isChecked: Boolean) {
        mEditor = settingsPrefs.edit()
        when (p0.id) {
            R.id.welcome_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("welcome", true)
                } else {
                    mEditor.putBoolean("welcome", false)
                }
            }
            R.id.order_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("alphabetical", true)
                } else {
                    mEditor.putBoolean("alphabetical", false)
                }
            }
            R.id.quick_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("quick", true)
                } else {
                    mEditor.putBoolean("quick", false)
                }
            }
            R.id.catcher_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("catcher", true)
                } else {
                    mEditor.putBoolean("catcher", false)
                }
            }
            else -> {
                if (isChecked) {
                    mEditor.putBoolean("firebase", true)
                } else {
                    mEditor.putBoolean("firebase", false)
                }
            }
        }
        mEditor.apply()
    }

    override fun onRestart() {
        super.onRestart()
        initSwitch()
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.includeToolbar.toolbar)

        val toolbarText = getString(R.string.settings)
        val title = binding.includeToolbar.title
        title.text = toolbarText
        val expandedTitle = binding.includeToolbar.expandedTitle
        expandedTitle.text = toolbarText

        val appBar = binding.includeToolbar.appBar
        appBar.layoutParams.height = (resources.displayMetrics.heightPixels * 0.3976).toInt()
        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, _ ->
            val percentage = (appBarLayout.y / appBarLayout.totalScrollRange)
            expandedTitle.alpha = 1 - (percentage * 2 * -1)
            title.alpha = percentage * -1
        })
    }

    private fun initSwitch() {
        val welcome = settingsPrefs.getBoolean("welcome", false)
        val order = settingsPrefs.getBoolean("alphabetical", true)
        val quick = settingsPrefs.getBoolean("quick", false)
        val catcher = settingsPrefs.getBoolean("catcher", false)
        val firebase = settingsPrefs.getBoolean("firebase", false)

        welcomeSwitch.isChecked = welcome
        orderSwitch.isChecked = order
        quickSwitch.isChecked = quick
        catcherSwitch.isChecked = catcher
        firebaseSwitch.isChecked = firebase

        welcomeSwitch.setOnCheckedChangeListener(this)
        orderSwitch.setOnCheckedChangeListener(this)
        quickSwitch.setOnCheckedChangeListener(this)
        catcherSwitch.setOnCheckedChangeListener(this)
        firebaseSwitch.setOnCheckedChangeListener(this)
    }
}
