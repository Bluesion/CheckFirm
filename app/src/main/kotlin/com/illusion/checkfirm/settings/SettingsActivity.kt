package com.illusion.checkfirm.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.illusion.checkfirm.R
import com.illusion.checkfirm.dialogs.WelcomeDialog
import com.illusion.checkfirm.utils.ThemeChanger
import com.illusion.checkfirm.utils.Tools

class SettingsActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeChanger.setAppTheme(this)
        setContentView(R.layout.activity_settings)

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val one = sharedPrefs.getBoolean("one", true)
        val dark= sharedPrefs.getBoolean("dark", false)
        val system = sharedPrefs.getBoolean("system", false)
        val welcome= sharedPrefs.getBoolean("welcome", false)
        val saver= sharedPrefs.getBoolean("saver", false)

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

        val oneSwitch = findViewById<SwitchMaterial>(R.id.expanded_bar)
        oneSwitch.isChecked = one
        oneSwitch.setOnCheckedChangeListener(this)

        val darkSwitch = findViewById<SwitchMaterial>(R.id.dark_mode)
        darkSwitch.isChecked = dark
        darkSwitch.setOnCheckedChangeListener(this)

        val systemSwitch = findViewById<SwitchMaterial>(R.id.system_theme)
        systemSwitch.isChecked = system
        systemSwitch.setOnCheckedChangeListener(this)

        val welcomeSwitch = findViewById<SwitchMaterial>(R.id.welcome_search)
        welcomeSwitch.isChecked = welcome
        welcomeSwitch.setOnCheckedChangeListener(this)

        val saverSwitch = findViewById<SwitchMaterial>(R.id.data_saver)
        saverSwitch.isChecked = saver
        saverSwitch.setOnCheckedChangeListener(this)

        val oneLayout = findViewById<RelativeLayout>(R.id.expanded)
        oneLayout.setOnClickListener {
            oneSwitch.toggle()
        }

        val darkLayout = findViewById<RelativeLayout>(R.id.dark)
        darkLayout.setOnClickListener {
            darkSwitch.toggle()
        }

        val systemLayout = findViewById<RelativeLayout>(R.id.system)
        systemLayout.setOnClickListener {
            systemSwitch.toggle()
        }

        val welcomeLayout = findViewById<RelativeLayout>(R.id.welcome)
        welcomeLayout.setOnClickListener {
            welcomeSwitch.toggle()
        }

        val saverLayout = findViewById<RelativeLayout>(R.id.saver)
        saverLayout.setOnClickListener {
            saverSwitch.toggle()
        }

        val about = findViewById<MaterialCardView>(R.id.about)
        about.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
        mEditor = sharedPrefs.edit()
        when {
            p0!!.id == R.id.expanded_bar -> {
                if (isChecked) {
                    mEditor.putBoolean("one", true)
                } else {
                    mEditor.putBoolean("one", false)
                }
                mEditor.apply()
                Tools.restart(this, 0)
            }
            p0.id == R.id.dark_mode -> {
                if (isChecked) {
                    mEditor.putBoolean("dark", true)
                } else {
                    mEditor.putBoolean("dark", false)
                }
                mEditor.apply()
                Tools.restart(this, 0)
            }
            p0.id == R.id.system_theme -> {
                if (isChecked) {
                    mEditor.putBoolean("system", true)
                } else {
                    mEditor.putBoolean("system", false)
                }
                mEditor.apply()
                Tools.restart(this, 0)
            }
            p0.id == R.id.welcome_search -> {
                if (isChecked) {
                    val bottomSheetFragment = WelcomeDialog()
                    bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                } else {
                    mEditor.putBoolean("welcome", false)
                    mEditor.apply()
                }
            }
            else -> {
                if (isChecked) {
                    mEditor.putBoolean("saver", true)
                } else {
                    mEditor.putBoolean("saver", false)
                }
                mEditor.apply()
            }
        }
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
}