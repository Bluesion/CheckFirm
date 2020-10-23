package com.illusion.checkfirm.settings.welcome

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
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.bookmark.BookmarkViewModel
import com.illusion.checkfirm.databinding.ActivityWelcomeSearchBinding
import java.util.*

class WelcomeSearchActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var binding: ActivityWelcomeSearchBinding
    private lateinit var settingPrefs: SharedPreferences
    private lateinit var searchPrefsEditor: SharedPreferences.Editor
    private lateinit var adapter: WelcomeSearchAdapter
    private var modelList = ArrayList<String>()
    private var cscList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val welcome = settingPrefs.getBoolean("welcome", false)

        initToolbar()

        val welcomeSwitch = binding.welcomeSwitch
        if (welcome) {
            welcomeSwitch.isChecked = true
            binding.switchText.text = getString(R.string.switch_on)
            binding.switchCard.background = ContextCompat.getDrawable(this, R.color.switch_card_background_on)
        } else {
            welcomeSwitch.isChecked = false
            binding.switchText.text = getString(R.string.switch_off)
            binding.switchCard.background = ContextCompat.getDrawable(this, R.color.switch_card_background_off)
        }
        welcomeSwitch.setOnCheckedChangeListener(this)

        binding.welcome.setOnClickListener {
            welcomeSwitch.toggle()
        }

        val savedDevicesLayout = binding.savedDevicesLayout
        val savedDevicesText = binding.savedDevicesText

        val searchPrefs = getSharedPreferences("search", Context.MODE_PRIVATE)
        searchPrefsEditor = searchPrefs.edit()
        val total = searchPrefs.getInt("welcome_search_total", 0)

        for (i in 0..total) {
            val model = searchPrefs.getString("welcome_search_model_$i", "")!!
            val csc = searchPrefs.getString("welcome_search_csc_$i", "")!!

            if (model.isNotEmpty() && csc.isNotEmpty()) {
                modelList.add(model)
                cscList.add(csc)
            }
        }

        if (modelList.isEmpty()) {
            savedDevicesLayout.visibility = View.GONE
        } else {
            savedDevicesLayout.visibility = View.VISIBLE
            savedDevicesText.text = resources.getQuantityText(R.plurals.saved_devices, modelList.size)
        }

        val recyclerView = binding.welcomeList
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = WelcomeSearchAdapter(this, modelList, cscList, object : WelcomeSearchAdapter.MyAdapterListener {
            override fun onDeleteClicked(position: Int) {
                modelList.removeAt(position)
                cscList.removeAt(position)
                savedDevicesText.text = resources.getQuantityText(R.plurals.saved_devices, modelList.size)
                searchPrefsEditor.putString("welcome_search_model_$position", "")
                searchPrefsEditor.putString("welcome_search_csc_$position", "")
                searchPrefsEditor.apply()
                if (modelList.isEmpty()) {
                    savedDevicesLayout.visibility = View.GONE
                }
                adapter.notifyDataSetChanged()
            }
        })
        recyclerView.adapter = adapter

        val bookmarkChipGroup = binding.chipGroup

        val bookmarkOrderBy = settingPrefs.getString("bookmark_order_by", "time")!!
        val isDescending = settingPrefs.getBoolean("bookmark_order_by_desc", false)

        val viewModel = ViewModelProvider(this, CheckFirm.viewModelFactory).get(BookmarkViewModel::class.java)
        viewModel.getBookmarks(bookmarkOrderBy, isDescending).observe(this, androidx.lifecycle.Observer { bookmarks ->
            bookmarks?.let {
                if (it.isEmpty()) {
                    bookmarkChipGroup.visibility = View.GONE
                } else {
                    for (element in it) {
                        val bookmarkChip = Chip(this)
                        bookmarkChip.text = element.name
                        bookmarkChip.isCheckable = false
                        bookmarkChip.setOnClickListener {
                            if (modelList.size >= 4) {
                                Toast.makeText(this, getString(R.string.multi_search_limit), Toast.LENGTH_SHORT).show()
                            } else {
                                var isDuplicated = false
                                val tempModel = element.model
                                val tempCsc = element.csc

                                if (modelList.isNotEmpty()) {
                                    for (j in modelList.indices) {
                                        if (tempModel == modelList[j] && tempCsc == cscList[j]) {
                                            isDuplicated = true
                                        }
                                    }
                                }

                                if (!isDuplicated) {
                                    modelList.add(tempModel)
                                    cscList.add(tempCsc)
                                    adapter.notifyDataSetChanged()
                                    savedDevicesLayout.visibility = View.VISIBLE
                                    bookmarkChipGroup.visibility = View.VISIBLE
                                }
                                savedDevicesText.text = resources.getQuantityText(R.plurals.saved_devices, modelList.size)
                            }
                        }
                        bookmarkChipGroup.addView(bookmarkChip)
                    }
                    bookmarkChipGroup.visibility = View.VISIBLE
                }
            }
        })

        binding.add.setOnClickListener {
            if (modelList.size >= 4) {
                Toast.makeText(this, getString(R.string.multi_search_limit), Toast.LENGTH_SHORT).show()
            } else {
                val modelText = binding.model.text!!.trim().toString().toUpperCase(Locale.US)
                val cscText = binding.csc.text!!.trim().toString().toUpperCase(Locale.US)

                if (modelText.isNotEmpty() && cscText.isNotEmpty()) {
                    var isDuplicated = false

                    if (modelList.isNotEmpty()) {
                        for (j in modelList.indices) {
                            if (modelText == modelList[j] && cscText == cscList[j]) {
                                isDuplicated = true
                            }
                        }
                    }

                    if (!isDuplicated) {
                        modelList.add(modelText)
                        cscList.add(cscText)
                        adapter.notifyDataSetChanged()
                        savedDevicesLayout.visibility = View.VISIBLE
                        bookmarkChipGroup.visibility = View.VISIBLE
                    }
                    savedDevicesText.text = resources.getQuantityText(R.plurals.saved_devices, modelList.size)
                } else {
                    Toast.makeText(this, getString(R.string.main_search_error_text), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCheckedChanged(p0: CompoundButton, isChecked: Boolean) {
        val mEditor = settingPrefs.edit()
        when (p0.id) {
            R.id.welcome_switch -> {
                if (isChecked) {
                    binding.switchCard.background = ContextCompat.getDrawable(this, R.color.switch_card_background_on)
                    mEditor.putBoolean("welcome", true)
                    binding.switchText.text = getString(R.string.switch_on)
                } else {
                    binding.switchCard.background = ContextCompat.getDrawable(this, R.color.switch_card_background_off)
                    mEditor.putBoolean("welcome", false)
                    binding.switchText.text = getString(R.string.switch_off)
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

    override fun onDestroy() {
        super.onDestroy()

        if (modelList.size != 0) {
            searchPrefsEditor.putInt("welcome_search_total", modelList.size - 1)
            for (i in 0 until modelList.size) {
                searchPrefsEditor.putString("welcome_search_model_$i", modelList[i])
                searchPrefsEditor.putString("welcome_search_csc_$i", cscList[i])
            }
        } else {
            searchPrefsEditor.putInt("welcome_search_total", 0)
            for (i in 0..4) {
                searchPrefsEditor.putString("welcome_search_model_$i", "")
                searchPrefsEditor.putString("welcome_search_csc_$i", "")
            }
        }
        searchPrefsEditor.apply()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.includeToolbar.toolbar)

        val toolbarText = getString(R.string.welcome_search)
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
}
