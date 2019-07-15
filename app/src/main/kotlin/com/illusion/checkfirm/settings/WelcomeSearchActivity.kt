package com.illusion.checkfirm.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.BookmarkDB
import com.illusion.checkfirm.database.BookmarkDBHelper
import java.util.ArrayList

class WelcomeSearchActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor
    private lateinit var switchText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_search)

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        mEditor = sharedPrefs.edit()
        val one = sharedPrefs.getBoolean("one", true)
        val welcome = sharedPrefs.getBoolean("welcome", false)

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

        switchText = findViewById<TextView>(R.id.switch_text)
        val welcomeSwitch = findViewById<SwitchMaterial>(R.id.welcome_switch)
        if (welcome) {
            welcomeSwitch.isChecked = true
            switchText.text = getString(R.string.switch_on)
        } else {
            welcomeSwitch.isChecked = false
            switchText.text = getString(R.string.switch_off)
        }
        welcomeSwitch.setOnCheckedChangeListener(this)

        val welcomeLayout = findViewById<ConstraintLayout>(R.id.welcome)
        welcomeLayout.setOnClickListener {
            welcomeSwitch.toggle()
        }

        val bookmarkList = ArrayList<BookmarkDB>()
        val bookmarkChipGroup = findViewById<ChipGroup>(R.id.chipGroup)
        val bookmarkHelper = BookmarkDBHelper(this)
        bookmarkList.addAll(bookmarkHelper.allBookmarkDB)
        if (bookmarkList.isEmpty()) {
            bookmarkChipGroup.visibility = View.GONE
        } else {
            bookmarkChipGroup.visibility = View.VISIBLE
        }

        val model = findViewById<TextInputEditText>(R.id.model)
        val csc = findViewById<TextInputEditText>(R.id.csc)

        for (i in bookmarkList.indices) {
            val bookmarkChip = Chip(this)
            bookmarkChip.text = bookmarkList[i].name
            bookmarkChip.isCheckable = false
            bookmarkChip.setOnClickListener {
                model.setText(bookmarkList[i].model)
                csc.setText(bookmarkList[i].csc)
            }
            bookmarkChipGroup.addView(bookmarkChip)
        }

        val sharedModel = sharedPrefs.getString("welcome_model", "") as String
        if (sharedModel.isBlank()) {
            model.setText(R.string.default_string)
        } else {
            model.setText(sharedModel)
        }
        model.setSelection(model.text!!.length)

        val sharedCSC = sharedPrefs.getString("welcome_csc", "") as String
        csc.setText(sharedCSC)

        val saveButton = findViewById<MaterialButton>(R.id.save)
        saveButton.setOnClickListener {
            val modelText = model.text!!.trim().toString().toUpperCase()
            val cscText = csc.text!!.trim().toString().toUpperCase()
            if (modelText.isNotBlank() && cscText.isNotBlank()) {
                mEditor.putString("welcome_model", modelText)
                mEditor.putString("welcome_csc", cscText)
                mEditor.apply()
                Toast.makeText(this, getString(R.string.welcome_search_saved), Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, getString(R.string.info_catcher_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCheckedChanged(p0: CompoundButton, isChecked: Boolean) {
        when {
            p0.id == R.id.welcome_switch -> {
                if (isChecked) {
                    mEditor.putBoolean("welcome", true)
                    switchText.text = getString(R.string.switch_on)
                } else {
                    mEditor.putBoolean("welcome", false)
                    switchText.text = getString(R.string.switch_off)
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