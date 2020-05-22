package com.illusion.checkfirm.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.R
import com.illusion.checkfirm.dialogs.ThemeDialog
import com.illusion.checkfirm.settings.about.AboutActivity
import com.illusion.checkfirm.settings.catcher.InfoCatcherActivity
import com.illusion.checkfirm.settings.help.HelpActivity
import com.illusion.checkfirm.settings.welcome.WelcomeSearchActivity

class SettingsActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor
    private lateinit var oneSwitch: SwitchMaterial
    private lateinit var welcomeSwitch: SwitchMaterial
    private lateinit var smartSwitch: SwitchMaterial
    private lateinit var quickSwitch: SwitchMaterial
    private lateinit var catcherSwitch: SwitchMaterial
    private lateinit var chinaSwitch: SwitchMaterial
    private var one: Boolean = true
    private var welcome: Boolean = false
    private var smart: Boolean = false
    private var quick: Boolean = false
    private var catcher: Boolean = false
    private var china: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        oneSwitch = findViewById(R.id.expanded_switch)
        welcomeSwitch = findViewById(R.id.welcome_switch)
        smartSwitch = findViewById(R.id.smart_switch)
        quickSwitch = findViewById(R.id.quick_switch)
        catcherSwitch = findViewById(R.id.catcher_switch)
        chinaSwitch = findViewById(R.id.china_switch)
        initSwitch()
        initToolbar()

        oneSwitch.setOnCheckedChangeListener(this)
        welcomeSwitch.setOnCheckedChangeListener(this)
        smartSwitch.setOnCheckedChangeListener(this)
        quickSwitch.setOnCheckedChangeListener(this)
        catcherSwitch.setOnCheckedChangeListener(this)
        chinaSwitch.setOnCheckedChangeListener(this)

        val oneLayout = findViewById<ConstraintLayout>(R.id.expanded_layout)
        oneLayout.setOnClickListener {
            oneSwitch.toggle()
        }

        val themeLayout = findViewById<LinearLayout>(R.id.theme_layout)
        themeLayout.setOnClickListener {
            val bottomSheetFragment = ThemeDialog()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        val welcomeLayout = findViewById<ConstraintLayout>(R.id.welcome_layout)
        welcomeLayout.setOnClickListener {
            val intent = Intent(this, WelcomeSearchActivity::class.java)
            startActivity(intent)
        }

        val smartLayout = findViewById<ConstraintLayout>(R.id.smart_layout)
        smartLayout.setOnClickListener {
            smartSwitch.toggle()
        }

        val quickLayout = findViewById<ConstraintLayout>(R.id.quick_layout)
        quickLayout.setOnClickListener {
            quickSwitch.toggle()
        }

        val catcherLayout = findViewById<ConstraintLayout>(R.id.catcher_layout)
        catcherLayout.setOnClickListener {
            val intent = Intent(this, InfoCatcherActivity::class.java)
            startActivity(intent)
        }

        val chinaLayout = findViewById<ConstraintLayout>(R.id.china_layout)
        chinaLayout.setOnClickListener {
            chinaSwitch.toggle()
        }

        val helpLayout = findViewById<MaterialTextView>(R.id.help_layout)
        helpLayout.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }

        val aboutLayout = findViewById<MaterialTextView>(R.id.about_layout)
        aboutLayout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        val inquiryLayout = findViewById<MaterialTextView>(R.id.inquiry_layout)
        inquiryLayout.setOnClickListener {
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
                    mEditor.putBoolean("china", true)
                } else {
                    mEditor.putBoolean("china", false)
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
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val toolbarText = getString(R.string.settings)
        val title = findViewById<MaterialTextView>(R.id.title)
        title.text = toolbarText
        val expandedTitle = findViewById<MaterialTextView>(R.id.expanded_title)
        expandedTitle.text = toolbarText

        val mAppBar = findViewById<AppBarLayout>(R.id.appbar)
        mAppBar.layoutParams.height = (resources.displayMetrics.heightPixels * 0.3976).toInt()
        mAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, _ ->
            val percentage = (appBarLayout.y / appBarLayout.totalScrollRange)
            expandedTitle.alpha = 1 - (percentage * 2 * -1)
            title.alpha = percentage * -1
        })

        val one = sharedPrefs.getBoolean("one", true)
        if (one) {
            mAppBar.setExpanded(true)
        } else {
            mAppBar.setExpanded(false)
        }
    }

    private fun initSwitch() {
        one = sharedPrefs.getBoolean("one", true)
        welcome = sharedPrefs.getBoolean("welcome", false)
        smart = sharedPrefs.getBoolean("smart", true)
        quick = sharedPrefs.getBoolean("quick", false)
        catcher = sharedPrefs.getBoolean("catcher", false)
        china = sharedPrefs.getBoolean("china", false)

        oneSwitch.isChecked = one
        welcomeSwitch.isChecked = welcome
        smartSwitch.isChecked = smart
        quickSwitch.isChecked = quick
        catcherSwitch.isChecked = catcher
        chinaSwitch.isChecked = china
    }
}