package com.illusion.checkfirm.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.bookmark.BookmarkActivity
import com.illusion.checkfirm.database.bookmark.BookmarkViewModel
import com.illusion.checkfirm.databinding.ActivityMainBinding
import com.illusion.checkfirm.dialogs.SearchDialog
import com.illusion.checkfirm.search.SearchActivity
import com.illusion.checkfirm.search.TransparentActivity
import com.illusion.checkfirm.settings.SettingsActivity
import com.illusion.checkfirm.settings.help.MyDeviceActivity
import com.illusion.checkfirm.settings.welcome.WelcomeSearchActivity
import com.illusion.checkfirm.utils.Tools

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var settingPrefs: SharedPreferences
    private lateinit var searchPrefs: SharedPreferences

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK) {
            when (data!!.getIntExtra("search_code", 1)) {
                1 -> {
                    val model = data.getStringExtra("model") as String
                    val csc = data.getStringExtra("csc") as String
                    val total = data.getIntExtra("total", 0)

                    networkTask(model, csc, total)
                }
                2 -> {
                    binding.searchResult.visibility = View.VISIBLE

                    val tab = binding.tabLayout
                    var firebase = settingPrefs.getBoolean("firebase", false)

                    val total = data.getIntExtra("total", 0)
                    val recyclerView = binding.searchResult
                    recyclerView.layoutManager = LinearLayoutManager(this)

                    if (total == 0) {
                        tab.visibility = View.GONE

                        val mAdapter = SingleAdapter(this, firebase, object : SingleAdapter.MyAdapterListener {
                            override fun onOfficialCardClicked(v: View, position: Int) {
                                val bottomSheetDialog = SearchDialog(true, position)
                                bottomSheetDialog.show(supportFragmentManager, bottomSheetDialog.tag)
                            }

                            override fun onTestCardClicked(v: View, position: Int) {
                                val bottomSheetDialog = SearchDialog(false, position)
                                bottomSheetDialog.show(supportFragmentManager, bottomSheetDialog.tag)
                            }
                        })
                        recyclerView.adapter = mAdapter
                    } else {
                        tab.visibility = View.VISIBLE

                        val tab1 = binding.buttonOfficial
                        val tab2 = binding.buttonTest
                        tab1.setTextAppearance(R.style.SearchButton_Selected)
                        tab2.setTextAppearance(R.style.SearchButton_Unselected)

                        var isOfficial = true
                        firebase = false

                        var mAdapter = MultiAdapter(this, isOfficial, firebase, total, object : MultiAdapter.MyAdapterListener {
                            override fun onLayoutClicked(v: View, position: Int) {
                                val bottomSheetDialog = SearchDialog(isOfficial, position)
                                bottomSheetDialog.show(supportFragmentManager, bottomSheetDialog.tag)
                            }
                        })
                        recyclerView.adapter = mAdapter

                        tab1.setOnClickListener {
                            tab1.setTextAppearance(R.style.SearchButton_Selected)
                            tab2.setTextAppearance(R.style.SearchButton_Unselected)
                            isOfficial = true
                            firebase = false
                            mAdapter = MultiAdapter(this, isOfficial, firebase, total, object : MultiAdapter.MyAdapterListener {
                                override fun onLayoutClicked(v: View, position: Int) {
                                    val bottomSheetDialog = SearchDialog(isOfficial, position)
                                    bottomSheetDialog.show(supportFragmentManager, bottomSheetDialog.tag)
                                }
                            })
                            recyclerView.adapter = mAdapter
                        }

                        tab2.setOnClickListener {
                            tab1.setTextAppearance(R.style.SearchButton_Unselected)
                            tab2.setTextAppearance(R.style.SearchButton_Selected)
                            isOfficial = false
                            firebase = settingPrefs.getBoolean("firebase", false)
                            mAdapter = MultiAdapter(this, isOfficial, firebase, total, object : MultiAdapter.MyAdapterListener {
                                override fun onLayoutClicked(v: View, position: Int) {
                                    val bottomSheetDialog = SearchDialog(isOfficial, position)
                                    bottomSheetDialog.show(supportFragmentManager, bottomSheetDialog.tag)
                                }
                            })
                            recyclerView.adapter = mAdapter
                        }
                    }

                    mSwipeRefreshLayout.isEnabled = false
                    mSwipeRefreshLayout.isRefreshing = false
                }
                else -> {
                    mSwipeRefreshLayout.isEnabled = false
                    mSwipeRefreshLayout.isRefreshing = false
                    binding.includeView.mainView.visibility = View.VISIBLE
                    binding.includeView.noWelcome.visibility = View.GONE
                    binding.includeView.networkError.visibility = View.VISIBLE
                    binding.includeView.searchError.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        searchPrefs = getSharedPreferences("search", Context.MODE_PRIVATE)

        // UI
        mSwipeRefreshLayout = binding.swipeRefreshLayout
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.green)
        mSwipeRefreshLayout.isEnabled = false

        initToolbar()
        initQuick()

        val welcome = settingPrefs.getBoolean("welcome", false)
        if (welcome) {
            welcomeSearch()
        } else {
            binding.includeView.mainView.visibility = View.VISIBLE
            binding.includeView.noWelcome.visibility = View.VISIBLE
            binding.includeView.networkError.visibility = View.GONE
            binding.includeView.searchError.visibility = View.GONE
        }

        binding.includeView.welcomeSearchSetting.setOnClickListener {
            val intent = Intent(this, WelcomeSearchActivity::class.java)
            startActivity(intent)
        }

        binding.includeView.wifiSetting.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

        binding.includeView.dataSetting.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 28) {
                startActivity(Intent(Settings.ACTION_DATA_USAGE_SETTINGS))
            } else {
                startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
            }
        }

        binding.includeView.myDevice.setOnClickListener {
            val intent = Intent(this, MyDeviceActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                startForResult.launch(
                    Intent(this, SearchActivity::class.java).apply {
                        putExtra("search_code", 1)
                    }
                )
                return true
            }
            R.id.bookmark -> {
                startForResult.launch(
                    Intent(this, BookmarkActivity::class.java).apply {
                        putExtra("search_code", 1)
                    }
                )
                return true
            }
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRestart() {
        super.onRestart()
        initQuick()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.includeToolbar.toolbar)

        val toolbarText = getString(R.string.app_name)
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

    private fun initQuick() {
        val chipScroll = binding.chipScroll
        val quick = settingPrefs.getBoolean("quick", false)
        if (quick) {
            val bookmarkOrderBy = settingPrefs.getString("bookmark_order_by", "time")!!
            val isDescending = settingPrefs.getBoolean("bookmark_order_by_desc", false)

            val viewModel = ViewModelProvider(this, CheckFirm.viewModelFactory).get(BookmarkViewModel::class.java)
            viewModel.getBookmarks(bookmarkOrderBy, isDescending).observe(this, { bookmarks ->
                bookmarks?.let {
                    if (it.isEmpty()) {
                        chipScroll.visibility = View.GONE
                    } else {
                        val bookmarkChipGroup = binding.chipGroup
                        bookmarkChipGroup.removeAllViews()
                        for (element in it) {
                            val bookmarkChip = Chip(this)
                            bookmarkChip.text = element.name
                            bookmarkChip.isCheckable = false
                            bookmarkChip.setOnClickListener {
                                networkTask(element.model, element.csc, 0)
                            }
                            bookmarkChipGroup.addView(bookmarkChip)
                        }
                        chipScroll.visibility = View.VISIBLE
                    }
                }
            })
        } else {
            chipScroll.visibility = View.GONE
        }
    }

    private fun welcomeSearch() {
        val total = searchPrefs.getInt("welcome_search_total", 0)
        var model = ""
        var csc = ""

        for (i in 0..total) {
            val tempModel = searchPrefs.getString("welcome_search_model_$i", "")
            val tempCsc = searchPrefs.getString("welcome_search_csc_$i", "")

            model += if (tempModel!!.isBlank()) {
                settingPrefs.getString("new_saved_model", "SM-A720S") + "%"
            } else {
                "$tempModel%"
            }

            csc += if (tempCsc!!.isBlank()) {
                settingPrefs.getString("new_saved_csc", "SKC") + "%"
            } else {
                "$tempCsc%"
            }
        }
        networkTask(model, csc, total + 1)
    }

    private fun networkTask(tempModel: String, tempCsc: String, total: Int) {
        if (tempModel.isBlank() || tempCsc.isBlank()) {
            binding.includeView.mainView.visibility = View.VISIBLE
            binding.includeView.noWelcome.visibility = View.GONE
            binding.includeView.networkError.visibility = View.GONE
            binding.includeView.searchError.visibility = View.VISIBLE
        } else {
            if (Tools.isOnline(this)) {
                binding.includeView.mainView.visibility = View.GONE
                mSwipeRefreshLayout.isEnabled = true
                mSwipeRefreshLayout.isRefreshing = true

                val editor = searchPrefs.edit()
                val intent = Intent(this, TransparentActivity::class.java)
                when {
                    total == 0 -> {
                        editor.putString("search_model_0", tempModel)
                        editor.putString("search_csc_0", tempCsc)
                        intent.putExtra("total", 1)
                    }
                    total == 1 -> {
                        val model = tempModel.substring(0, tempModel.length - 1)
                        val csc = tempCsc.substring(0, tempCsc.length - 1)
                        editor.putString("search_model_0", model)
                        editor.putString("search_csc_0", csc)
                        intent.putExtra("total", 1)
                    }
                    total > 1 -> {
                        val modelList = tempModel.split("%")
                        val cscList = tempCsc.split("%")

                        for (i in modelList.indices) {
                            editor.putString("search_model_$i", modelList[i])
                            editor.putString("search_csc_$i", cscList[i])
                        }
                        intent.putExtra("total", total)
                    }
                }
                editor.apply()
                intent.putExtra("search_code", 2)
                startForResult.launch(intent)
                overridePendingTransition(0, 0)
            } else {
                binding.includeView.mainView.visibility = View.VISIBLE
                binding.includeView.noWelcome.visibility = View.GONE
                binding.includeView.networkError.visibility = View.VISIBLE
                binding.includeView.searchError.visibility = View.GONE
            }
        }
    }
}
