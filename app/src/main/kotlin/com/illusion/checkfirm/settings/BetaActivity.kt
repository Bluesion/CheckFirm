package com.illusion.checkfirm.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.switchmaterial.SwitchMaterial
import com.illusion.checkfirm.R
import com.illusion.checkfirm.utils.Tools

class BetaActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beta)

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val one = sharedPrefs.getBoolean("one", true)
        val beta = sharedPrefs.getBoolean("beta", false)

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

        val switchText = findViewById<TextView>(R.id.switch_text)
        val betaSwitch = findViewById<SwitchMaterial>(R.id.beta_switch)
        if (beta) {
            betaSwitch.isChecked = true
            switchText.text = getString(R.string.switch_on)
        } else {
            betaSwitch.isChecked = false
            switchText.text = getString(R.string.switch_off)
        }

        betaSwitch.setOnCheckedChangeListener(this)

        val betaLayout = findViewById<ConstraintLayout>(R.id.beta)
        betaLayout.setOnClickListener {
            betaSwitch.toggle()
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
        mEditor = sharedPrefs.edit()
        when {
            p0!!.id == R.id.beta_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("beta", true)
                } else {
                    mEditor.putBoolean("beta", false)
                }
                mEditor.apply()
                Tools.restart(this, 0)
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