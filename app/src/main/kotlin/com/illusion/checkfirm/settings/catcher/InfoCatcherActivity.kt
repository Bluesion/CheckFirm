package com.illusion.checkfirm.settings.catcher

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.google.firebase.messaging.FirebaseMessaging
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.bookmark.BookmarkViewModel
import com.illusion.checkfirm.database.catcher.InfoCatcherViewModel
import com.illusion.checkfirm.databinding.ActivityInfoCatcherBinding
import java.util.*

class InfoCatcherActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var binding: ActivityInfoCatcherBinding
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var icViewModel: InfoCatcherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoCatcherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        initToolbar()

        val catcher = sharedPrefs.getBoolean("catcher", false)
        val catcherSwitch = binding.catcherSwitch
        if (catcher) {
            catcherSwitch.isChecked = true
            binding.switchText.text = getString(R.string.switch_on)
        } else {
            catcherSwitch.isChecked = false
            binding.switchText.text = getString(R.string.switch_off)
        }
        catcherSwitch.setOnCheckedChangeListener(this)

        binding.catcherLayout.setOnClickListener {
            catcherSwitch.toggle()
        }

        val bookmarkChipGroup = binding.chipGroup

        val bmViewModel = ViewModelProvider(this, CheckFirm.viewModelFactory).get(BookmarkViewModel::class.java)
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

        val model = binding.model
        model.setText(getString(R.string.default_string))
        model.setSelection(model.text!!.length)

        binding.save.setOnClickListener {
            val modelText = model.text!!.trim().toString().toUpperCase(Locale.US)
            val cscText = binding.csc.text!!.trim().toString().toUpperCase(Locale.US)

            if (modelText.isBlank() || cscText.isBlank()) {
                Toast.makeText(this, getString(R.string.info_catcher_error), Toast.LENGTH_SHORT).show()
            } else {
                icViewModel.insert(modelText, cscText)
                FirebaseMessaging.getInstance().subscribeToTopic(modelText + cscText)
            }
        }

        val recyclerView = binding.recyclerView
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
                    binding.savedDevicesLayout.visibility = View.GONE
                } else {
                    binding.savedDevicesLayout.visibility = View.VISIBLE
                    binding.savedDevicesText.text = resources.getQuantityText(R.plurals.saved_devices, it.size)
                }
            }
        })
    }

    override fun onCheckedChanged(p0: CompoundButton, isChecked: Boolean) {
        val editor = sharedPrefs.edit()
        when (p0.id) {
            R.id.catcher_switch -> {
                if (isChecked) {
                    binding.switchCard.background = ContextCompat.getDrawable(this, R.color.switch_card_background_on)
                    editor.putBoolean("catcher", true)
                    binding.switchText.text = getString(R.string.switch_on)
                } else {
                    binding.switchCard.background = ContextCompat.getDrawable(this, R.color.switch_card_background_off)
                    editor.putBoolean("catcher", false)
                    binding.switchText.text = getString(R.string.switch_off)
                }
            }
        }
        editor.apply()
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

        val toolbarText = getString(R.string.info_catcher)
        val title = binding.includeToolbar.title
        title.text = toolbarText
        val expandedTitle = binding.includeToolbar.expandedTitle
        expandedTitle.text = toolbarText

        val appBar = binding.includeToolbar.appbar
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
}