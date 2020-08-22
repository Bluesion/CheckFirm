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
import com.illusion.checkfirm.dialogs.ThemeDialog
import com.illusion.checkfirm.settings.about.AboutActivity
import com.illusion.checkfirm.settings.catcher.InfoCatcherActivity
import com.illusion.checkfirm.settings.help.HelpActivity
import com.illusion.checkfirm.settings.welcome.WelcomeSearchActivity

class SettingsActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor
    private lateinit var oneSwitch: SwitchMaterial
    private lateinit var welcomeSwitch: SwitchMaterial
    private lateinit var smartSwitch: SwitchMaterial
    private lateinit var quickSwitch: SwitchMaterial
    private lateinit var catcherSwitch: SwitchMaterial
    private lateinit var firebaseSwitch: SwitchMaterial
    private var one: Boolean = true
    private var welcome: Boolean = false
    private var smart: Boolean = false
    private var quick: Boolean = false
    private var catcher: Boolean = false
    private var firebase: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        oneSwitch = binding.expandedSwitch
        welcomeSwitch = binding.welcomeSwitch
        smartSwitch = binding.smartSwitch
        quickSwitch = binding.quickSwitch
        catcherSwitch = binding.catcherSwitch
        firebaseSwitch = binding.firebaseSwitch
        initSwitch()
        initToolbar()

        oneSwitch.setOnCheckedChangeListener(this)
        welcomeSwitch.setOnCheckedChangeListener(this)
        smartSwitch.setOnCheckedChangeListener(this)
        quickSwitch.setOnCheckedChangeListener(this)
        catcherSwitch.setOnCheckedChangeListener(this)
        firebaseSwitch.setOnCheckedChangeListener(this)

        binding.expandedLayout.setOnClickListener {
            oneSwitch.toggle()
        }

        binding.themeLayout.setOnClickListener {
            val bottomSheetFragment = ThemeDialog()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.welcomeLayout.setOnClickListener {
            val intent = Intent(this, WelcomeSearchActivity::class.java)
            startActivity(intent)
        }

        binding.smartLayout.setOnClickListener {
            smartSwitch.toggle()
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
        mEditor = sharedPrefs.edit()
        when (p0.id) {
            R.id.expanded_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("one", true)
                } else {
                    mEditor.putBoolean("one", false)
                }
            }
            R.id.welcome_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("welcome", true)
                } else {
                    mEditor.putBoolean("welcome", false)
                }
            }
            R.id.smart_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("smart", true)
                } else {
                    mEditor.putBoolean("smart", false)
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

        val one = sharedPrefs.getBoolean("one", true)
        if (one) {
            appBar.setExpanded(true)
        } else {
            appBar.setExpanded(false)
        }
    }

    private fun initSwitch() {
        one = sharedPrefs.getBoolean("one", true)
        welcome = sharedPrefs.getBoolean("welcome", false)
        smart = sharedPrefs.getBoolean("smart", true)
        quick = sharedPrefs.getBoolean("quick", false)
        catcher = sharedPrefs.getBoolean("catcher", false)
        firebase = sharedPrefs.getBoolean("firebase", false)

        oneSwitch.isChecked = one
        welcomeSwitch.isChecked = welcome
        smartSwitch.isChecked = smart
        quickSwitch.isChecked = quick
        catcherSwitch.isChecked = catcher
        firebaseSwitch.isChecked = firebase
    }
}
