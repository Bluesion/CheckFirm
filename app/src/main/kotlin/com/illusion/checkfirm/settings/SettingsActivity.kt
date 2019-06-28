package com.illusion.checkfirm.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.messaging.FirebaseMessaging
import com.illusion.checkfirm.R
import com.illusion.checkfirm.dialogs.ContactDialog

class SettingsActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor
    private lateinit var oneSwitch: SwitchMaterial
    private lateinit var darkSwitch: SwitchMaterial
    private lateinit var systemSwitch: SwitchMaterial
    private lateinit var welcomeSwitch: SwitchMaterial
    private lateinit var saverSwitch: SwitchMaterial
    private lateinit var smartSwitch: SwitchMaterial
    private lateinit var catcherSwitch: SwitchMaterial
    private var one: Boolean = true
    private var dark: Boolean = false
    private var system: Boolean = false
    private var welcome: Boolean = false
    private var saver: Boolean = false
    private var smart: Boolean = false
    private var catcher: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        val mAppBar = findViewById<AppBarLayout>(R.id.appbar)
        val height = (resources.displayMetrics.heightPixels * 0.3976)
        val lp = mAppBar.layoutParams
        lp.height = height.toInt()
        if (one) {
            mAppBar.setExpanded(true)
        } else {
            mAppBar.setExpanded(false)
        }

        val title = findViewById<TextView>(R.id.title)
        val expandedTitle = findViewById<TextView>(R.id.expanded_title)
        mAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, _ ->
            val percentage = (appBarLayout.y / appBarLayout.totalScrollRange)
            expandedTitle.alpha = 1 - (percentage * 2 * -1)
            title.alpha = percentage * -1
        })

        oneSwitch = findViewById(R.id.expanded_switch)
        darkSwitch = findViewById(R.id.dark_switch)
        systemSwitch = findViewById(R.id.system_switch)
        welcomeSwitch = findViewById(R.id.welcome_switch)
        saverSwitch = findViewById(R.id.saver_switch)
        smartSwitch = findViewById(R.id.smart_switch)
        catcherSwitch = findViewById(R.id.catcher_switch)
        initSwitch()
        oneSwitch.setOnCheckedChangeListener(this)
        darkSwitch.setOnCheckedChangeListener(this)
        systemSwitch.setOnCheckedChangeListener(this)
        welcomeSwitch.setOnCheckedChangeListener(this)
        saverSwitch.setOnCheckedChangeListener(this)
        smartSwitch.setOnCheckedChangeListener(this)
        catcherSwitch.setOnCheckedChangeListener(this)

        val oneLayout = findViewById<ConstraintLayout>(R.id.expanded_layout)
        oneLayout.setOnClickListener {
            oneSwitch.toggle()
        }

        val darkLayout = findViewById<ConstraintLayout>(R.id.dark_layout)
        darkLayout.setOnClickListener {
            darkSwitch.toggle()
        }

        val systemLayout = findViewById<ConstraintLayout>(R.id.system_layout)
        systemLayout.setOnClickListener {
            systemSwitch.toggle()
        }

        val welcomeLayout = findViewById<ConstraintLayout>(R.id.welcome_layout)
        welcomeLayout.setOnClickListener {
            val intent = Intent(this, WelcomeSearchActivity::class.java)
            startActivity(intent)
        }

        val saverLayout = findViewById<ConstraintLayout>(R.id.saver_layout)
        saverLayout.setOnClickListener {
            saverSwitch.toggle()
        }

        val smartLayout = findViewById<ConstraintLayout>(R.id.smart_layout)
        smartLayout.setOnClickListener {
            smartSwitch.toggle()
        }

        val catcherLayout = findViewById<ConstraintLayout>(R.id.catcher_layout)
        catcherLayout.setOnClickListener {
            val intent = Intent(this, InfoCatcherActivity::class.java)
            startActivity(intent)
        }

        val aboutLayout = findViewById<ConstraintLayout>(R.id.about_layout)
        aboutLayout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        val contactLayout = findViewById<ConstraintLayout>(R.id.contact_layout)
        contactLayout.setOnClickListener {
            val bottomSheetFragment = ContactDialog()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }

    override fun onCheckedChanged(p0: CompoundButton, isChecked: Boolean) {
        mEditor = sharedPrefs.edit()
        when {
            p0.id == R.id.expanded_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("one", true)
                } else {
                    mEditor.putBoolean("one", false)
                }
                mEditor.apply()
            }
            p0.id == R.id.dark_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("dark", true)
                    if (system) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
                    }
                } else {
                    mEditor.putBoolean("dark", false)
                    if (system) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
                    }
                }
                mEditor.apply()
            }
            p0.id == R.id.system_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("system", true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                } else {
                    mEditor.putBoolean("system", false)
                    if (dark) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
                    }
                }
                mEditor.apply()
            }
            p0.id == R.id.welcome_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("welcome", true)
                } else {
                    mEditor.putBoolean("welcome", false)
                }
                mEditor.apply()
            }
            p0.id == R.id.saver_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("saver", true)
                } else {
                    mEditor.putBoolean("saver", false)
                }
                mEditor.apply()
            }
            p0.id == R.id.smart_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("smart", true)
                } else {
                    mEditor.putBoolean("smart", false)
                }
                mEditor.apply()
            }
            else -> {
                val model = sharedPrefs.getString("catcher_model", "CheckFirm") as String
                val csc = sharedPrefs.getString("catcher_csc", "Catcher") as String
                if (isChecked) {
                    mEditor.putBoolean("catcher", true)
                    if (model.isNotBlank() && csc.isNotBlank()) {
                        FirebaseMessaging.getInstance().subscribeToTopic(model+csc)
                    }
                } else {
                    mEditor.putBoolean("catcher", false)
                    if (model.isNotBlank() && csc.isNotBlank()) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(model+csc)
                    }
                }
                mEditor.apply()
            }
        }
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

    private fun initSwitch() {
        one = sharedPrefs.getBoolean("one", true)
        dark = sharedPrefs.getBoolean("dark", false)
        system = sharedPrefs.getBoolean("system", false)
        welcome = sharedPrefs.getBoolean("welcome", false)
        saver = sharedPrefs.getBoolean("saver", false)
        smart = sharedPrefs.getBoolean("smart", false)
        catcher = sharedPrefs.getBoolean("catcher", false)

        oneSwitch.isChecked = one
        darkSwitch.isChecked = dark
        systemSwitch.isChecked = system
        welcomeSwitch.isChecked = welcome
        saverSwitch.isChecked = saver
        smartSwitch.isChecked = smart
        catcherSwitch.isChecked = catcher
    }
}