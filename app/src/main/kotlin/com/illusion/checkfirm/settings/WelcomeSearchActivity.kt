package com.illusion.checkfirm.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
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
import com.illusion.checkfirm.R
import com.illusion.checkfirm.database.bookmark.BookmarkViewModel
import java.util.*

class WelcomeSearchActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var mEditor: SharedPreferences.Editor
    private lateinit var searchPrefsEditor: SharedPreferences.Editor
    private lateinit var switchText: MaterialTextView
    private lateinit var switchCard: LinearLayout
    private lateinit var adapter: WelcomeSearchAdapter
    private lateinit var modelList: ArrayList<String>
    private lateinit var cscList: ArrayList<String>
    private lateinit var viewModel: BookmarkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_search)

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
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

        val title = findViewById<MaterialTextView>(R.id.title)
        val expandedTitle = findViewById<MaterialTextView>(R.id.expanded_title)
        mAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, _ ->
            val percentage = (appBarLayout.y / appBarLayout.totalScrollRange)
            expandedTitle.alpha = 1 - (percentage * 2 * -1)
            title.alpha = percentage * -1
        })

        switchCard = findViewById(R.id.switch_card)
        switchText = findViewById(R.id.switch_text)
        val welcomeSwitch = findViewById<SwitchMaterial>(R.id.welcome_switch)
        if (welcome) {
            welcomeSwitch.isChecked = true
            switchText.text = getString(R.string.switch_on)
            switchCard.background = getDrawable(R.color.switch_card_background_on)
        } else {
            welcomeSwitch.isChecked = false
            switchText.text = getString(R.string.switch_off)
            switchCard.background = getDrawable(R.color.switch_card_background_off)
        }
        welcomeSwitch.setOnCheckedChangeListener(this)

        val welcomeLayout = findViewById<ConstraintLayout>(R.id.welcome)
        welcomeLayout.setOnClickListener {
            welcomeSwitch.toggle()
        }

        val savedDevicesLayout = findViewById<MaterialCardView>(R.id.saved_devices_layout)
        val savedDevicesText = findViewById<MaterialTextView>(R.id.saved_devices_text)
        val bookmarkChipGroup = findViewById<ChipGroup>(R.id.chipGroup)
        viewModel = ViewModelProviders.of(this).get(BookmarkViewModel::class.java)
        viewModel.allBookmarks.observe(this, androidx.lifecycle.Observer { bookmarks ->
            bookmarks?.let {
                if (it.isEmpty()) {
                    bookmarkChipGroup.visibility = View.GONE
                } else {
                    for (element in it) {
                        val bookmarkChip = Chip(this)
                        bookmarkChip.text = element.name
                        bookmarkChip.isCheckable = false
                        bookmarkChip.setOnClickListener { _ ->
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
                                }
                            }
                        }
                        bookmarkChipGroup.addView(bookmarkChip)
                    }
                    bookmarkChipGroup.visibility = View.VISIBLE
                }
            }
        })

        modelList = ArrayList()
        cscList = ArrayList()

        val searchPrefs = getSharedPreferences("search_device", Context.MODE_PRIVATE)
        searchPrefsEditor = searchPrefs.edit()
        val total = searchPrefs.getInt("welcome_search_total", 0)

        for (i in 0..total) {
            val model = searchPrefs.getString("welcome_search_model_$i", "")!!
            val csc = searchPrefs.getString("welcome_search_csc_$i", "")!!

            if (model.isNotEmpty() && csc.isNotEmpty()) {
                modelList.add(searchPrefs.getString("welcome_search_model_$i", "")!!)
                cscList.add(searchPrefs.getString("welcome_search_csc_$i", "")!!)
            }
        }

        if (modelList.isEmpty()) {
            savedDevicesLayout.visibility = View.GONE
        } else {
            savedDevicesLayout.visibility = View.VISIBLE
            savedDevicesText.text = resources.getQuantityText(R.plurals.saved_devices, modelList.size)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.welcome_list)
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

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

        val editTextModel = findViewById<TextInputEditText>(R.id.model)
        val editTextCsc = findViewById<TextInputEditText>(R.id.csc)
        val addButton = findViewById<MaterialButton>(R.id.add)
        addButton.setOnClickListener {
            if (modelList.size >= 4) {
                Toast.makeText(this, getString(R.string.multi_search_limit), Toast.LENGTH_SHORT).show()
            } else {
                savedDevicesLayout.visibility = View.VISIBLE
                val modelText = editTextModel.text!!.trim().toString().toUpperCase(Locale.US)
                val cscText = editTextCsc.text!!.trim().toString().toUpperCase(Locale.US)

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
                    }
                    savedDevicesText.text = resources.getQuantityText(R.plurals.saved_devices, modelList.size)
                } else {
                    Toast.makeText(this, getString(R.string.multi_search_limit), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCheckedChanged(p0: CompoundButton, isChecked: Boolean) {
        mEditor = sharedPrefs.edit()
        when (p0.id) {
            R.id.welcome_switch -> {
                if (isChecked) {
                    switchCard.background = getDrawable(R.color.switch_card_background_on)
                    mEditor.putBoolean("welcome", true)
                    switchText.text = getString(R.string.switch_on)
                } else {
                    switchCard.background = getDrawable(R.color.switch_card_background_off)
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
            for (i in 0 until modelList.size) {
                searchPrefsEditor.putString("welcome_search_model_$i", "")
                searchPrefsEditor.putString("welcome_search_csc_$i", "")
            }
        }
        searchPrefsEditor.apply()
    }
}