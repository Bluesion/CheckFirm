package com.illusion.checkfirm.settings.catcher

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.messaging.FirebaseMessaging
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.bookmark.BookmarkViewModel
import com.illusion.checkfirm.database.catcher.InfoCatcherViewModel
import java.util.*

class InfoCatcherActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor
    private lateinit var switchText: MaterialTextView
    private lateinit var switchCard: LinearLayout
    private lateinit var icViewModel: InfoCatcherViewModel
    private lateinit var bmViewModel: BookmarkViewModel

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_catcher)

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        mEditor = sharedPrefs.edit()

        initToolbar()

        val catcher = sharedPrefs.getBoolean("catcher", false)
        switchCard = findViewById(R.id.switch_card)
        switchText = findViewById(R.id.switch_text)
        val catcherSwitch = findViewById<SwitchMaterial>(R.id.catcher_switch)
        if (catcher) {
            catcherSwitch.isChecked = true
            switchText.text = getString(R.string.switch_on)
        } else {
            catcherSwitch.isChecked = false
            switchText.text = getString(R.string.switch_off)
        }
        catcherSwitch.setOnCheckedChangeListener(this)

        val catcherLayout = findViewById<ConstraintLayout>(R.id.catcher_layout)
        catcherLayout.setOnClickListener {
            catcherSwitch.toggle()
        }

        val savedDevicesLayout = findViewById<MaterialCardView>(R.id.saved_devices_layout)
        val savedDevicesText = findViewById<MaterialTextView>(R.id.saved_devices_text)
        val bookmarkChipGroup = findViewById<ChipGroup>(R.id.chipGroup)
        val model = findViewById<TextInputEditText>(R.id.model)
        val csc = findViewById<TextInputEditText>(R.id.csc)

        bmViewModel = ViewModelProvider(this, CheckFirm.viewModelFactory).get(BookmarkViewModel::class.java)
        bmViewModel.allBookmarks.observe(this, androidx.lifecycle.Observer { bookmarks ->
            bookmarks?.let {
                if (it.isEmpty()) {
                    bookmarkChipGroup.visibility = View.GONE
                } else {
                    for (element in it) {
                        val bookmarkChip = Chip(this)
                        bookmarkChip.text = element.name
                        bookmarkChip.isCheckable = false
                        bookmarkChip.setOnClickListener {
                            icViewModel.insert(element.model, element.csc)
                            FirebaseMessaging.getInstance().subscribeToTopic(element.model + element.csc)
                        }
                        bookmarkChipGroup.addView(bookmarkChip)
                    }
                    bookmarkChipGroup.visibility = View.VISIBLE
                }

            }
        })

        model.setText(getString(R.string.default_string))
        model.setSelection(model.text!!.length)

        val saveButton = findViewById<MaterialButton>(R.id.save)
        saveButton.setOnClickListener {
            val modelText = model.text!!.trim().toString().toUpperCase(Locale.US)
            val cscText = csc.text!!.trim().toString().toUpperCase(Locale.US)

            if (modelText.isBlank() || cscText.isBlank()) {
                Toast.makeText(this, getString(R.string.info_catcher_error), Toast.LENGTH_SHORT).show()
            } else {
                icViewModel.insert(modelText, cscText)
                FirebaseMessaging.getInstance().subscribeToTopic(modelText + cscText)
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = InfoCatcherAdapter(this, ArrayList(), object : InfoCatcherAdapter.MyAdapterListener {
            override fun onDeleteClicked(device: String) {
                icViewModel.delete(device)
                FirebaseMessaging.getInstance().unsubscribeFromTopic(device)
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        icViewModel = ViewModelProvider(this, CheckFirm.viewModelFactory).get(InfoCatcherViewModel::class.java)
        icViewModel.allDevices.observe(this, androidx.lifecycle.Observer { devices ->
            devices?.let {
                adapter.setDevices(it)
                if (it.isEmpty()) {
                    savedDevicesLayout.visibility = View.GONE
                } else {
                    savedDevicesLayout.visibility = View.VISIBLE
                    savedDevicesText.text = resources.getQuantityText(R.plurals.saved_devices, it.size)
                }
            }
        })
    }

    override fun onCheckedChanged(p0: CompoundButton, isChecked: Boolean) {
        when (p0.id) {
            R.id.catcher_switch -> {
                if (isChecked) {
                    switchCard.background = ContextCompat.getDrawable(this, R.color.switch_card_background_on)
                    mEditor.putBoolean("catcher", true)
                    switchText.text = getString(R.string.switch_on)
                } else {
                    switchCard.background = ContextCompat.getDrawable(this, R.color.switch_card_background_off)
                    mEditor.putBoolean("catcher", false)
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

    private fun initToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val toolbarText = getString(R.string.info_catcher)
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
}