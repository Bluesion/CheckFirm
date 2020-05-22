package com.illusion.checkfirm.main

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textview.MaterialTextView
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.bookmark.BookmarkActivity
import com.illusion.checkfirm.database.bookmark.BookmarkViewModel
import com.illusion.checkfirm.dialogs.SearchDialog
import com.illusion.checkfirm.primitive.MainItem
import com.illusion.checkfirm.primitive.PreviousItem
import com.illusion.checkfirm.primitive.SmartSearchItem
import com.illusion.checkfirm.search.TransparentActivity
import com.illusion.checkfirm.search.SearchActivity
import com.illusion.checkfirm.settings.SettingsActivity
import com.illusion.checkfirm.settings.help.HelpActivity
import com.illusion.checkfirm.settings.help.ManualActivity
import com.illusion.checkfirm.settings.help.MyDeviceActivity
import com.illusion.checkfirm.utils.Tools
import java.util.*

class MainActivity : AppCompatActivity() {

    // Welcome Search
    private lateinit var welcomeCardView: MaterialCardView
    private lateinit var welcomeTitle: MaterialTextView
    private lateinit var welcomeText: MaterialTextView

    // Search Layouts
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mResult: LinearLayout

    // Quick Search
    private lateinit var viewModel: BookmarkViewModel

    // ETC
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var searchDevicePrefs: SharedPreferences
    private lateinit var searchResultPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        searchDevicePrefs = getSharedPreferences("search_device", Context.MODE_PRIVATE)
        searchResultPrefs = getSharedPreferences("search_result", Context.MODE_PRIVATE)

        // UI
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.green)
        mSwipeRefreshLayout.isEnabled = false

        // Welcome Search
        welcomeCardView = findViewById(R.id.welcome)
        welcomeTitle = findViewById(R.id.welcome_title)
        welcomeText = findViewById(R.id.welcome_text)

        // Search Result
        mResult = findViewById(R.id.result)

        initToolbar()
        initQuick()

        if (Intent.ACTION_VIEW == intent.action) {
            val url = intent.data!!

            when (url.pathSegments.size) {
                0 -> {
                    Toast.makeText(this, getString(R.string.link_share_error), Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    when (url.pathSegments[0].toString()) {
                        "help" -> {
                            val intent = Intent(this, HelpActivity::class.java)
                            startActivity(intent)
                        }
                        "manual" -> {
                            val intent = Intent(this, ManualActivity::class.java)
                            startActivity(intent)
                        }
                        "mydevice" -> {
                            val intent = Intent(this, MyDeviceActivity::class.java)
                            startActivity(intent)
                        }
                        else -> {
                            Toast.makeText(this, getString(R.string.link_share_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else -> {
                    val model = if (url.pathSegments[0].isBlank()) {
                        ""
                    } else {
                        url.pathSegments[0].toString().toUpperCase(Locale.ENGLISH)
                    }
                    val csc = if (url.pathSegments[1].isBlank()) {
                        ""
                    } else {
                        url.pathSegments[1].toString().toUpperCase(Locale.ENGLISH)
                    }
                    networkTask(model, csc, 0)
                }
            }
        } else {
            val welcome = sharedPrefs.getBoolean("welcome", false)
            if (welcome) {
                welcomeSearch()
            } else {
                welcomeTitle.text = getString(R.string.welcome_search)
                welcomeText.text = getString(R.string.welcome_disabled)
                welcomeCardView.visibility = View.VISIBLE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (Tools.isOnline(this)) {
                val model = data!!.getStringExtra("model") as String
                val csc = data.getStringExtra("csc") as String
                val total = data.getIntExtra("total", 0)
                
                welcomeCardView.visibility = View.GONE
                networkTask(model, csc, total)
            } else {
                Toast.makeText(this, R.string.check_network, Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            mResult.visibility = View.VISIBLE

            val tab = findViewById<LinearLayout>(R.id.tab_layout)
            var smart = sharedPrefs.getBoolean("smart", true)
            if (sharedPrefs.getBoolean("china", false)) {
                smart = false
            }

            var model: String
            var csc: String
            var latestOfficial: String
            var latestTest: String
            var previousOfficial: String
            var previousTest: String
            var date: String
            var downgrade: String
            var type: String

            val total = data!!.getIntExtra("total", 0)
            val mRecyclerView = findViewById<RecyclerView>(R.id.search_result)
            val mLayoutManager = LinearLayoutManager(this)
            mRecyclerView.layoutManager = mLayoutManager

            if (total == 0) {
                tab.visibility = View.GONE
                model = searchDevicePrefs.getString("search_model_0", "")!!
                csc = searchDevicePrefs.getString("search_csc_0", "")!!
                previousOfficial = searchResultPrefs.getString("previous_official_0", "")!!
                previousTest = searchResultPrefs.getString("previous_test_0", "")!!
                date = searchResultPrefs.getString("first_discovery_date_0", "")!!
                downgrade = searchResultPrefs.getString("downgrade_0", "")!!
                type = searchResultPrefs.getString("type_0", "")!!

                latestOfficial = if (searchResultPrefs.getString("latest_official_0", "")!!.isBlank()) {
                    getString(R.string.search_error)
                } else {
                    searchResultPrefs.getString("latest_official_0", "")!!
                }

                latestTest = if (searchResultPrefs.getString("latest_test_0", "")!!.isBlank()) {
                    getString(R.string.search_error)
                } else {
                    searchResultPrefs.getString("latest_test_0", "")!!
                }

                val mAdapter = SingleAdapter(this, model, csc, latestOfficial, latestTest,
                        smart, date, downgrade, type, object : SingleAdapter.MyAdapterListener {
                    override fun onOfficialCardClicked(v: View, position: Int) {
                        val bottomSheetFragment = SearchDialog.newInstance(true, model, csc, latestOfficial, previousOfficial,
                                latestTest, previousTest)
                        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                    }

                    override fun onTestCardClicked(v: View, position: Int) {
                        val bottomSheetFragment = SearchDialog.newInstance(false, model, csc, latestOfficial, previousOfficial,
                                latestTest, previousTest)
                        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                    }
                })
                mRecyclerView.adapter = mAdapter
            } else {
                tab.visibility = View.VISIBLE

                val tab1 = findViewById<MaterialButton>(R.id.button_official)
                val tab2 = findViewById<MaterialButton>(R.id.button_test)
                tab1.setTextAppearance(R.style.SearchButton_Selected)
                tab2.setTextAppearance(R.style.SearchButton_Unselected)

                var isOfficial = true
                smart = false

                val resultList = ArrayList<MainItem>()
                val previousList = ArrayList<PreviousItem>()
                val smartList = ArrayList<SmartSearchItem>()
                resultList.clear()
                previousList.clear()
                smartList.clear()
                for (i in 0..total) {
                    model = searchDevicePrefs.getString("search_model_$i", "")!!
                    csc = searchDevicePrefs.getString("search_csc_$i", "")!!
                    latestOfficial = searchResultPrefs.getString("latest_official_$i", "")!!
                    latestTest = searchResultPrefs.getString("latest_test_$i", "")!!
                    previousOfficial = searchResultPrefs.getString("previous_official_$i", "")!!
                    previousTest = searchResultPrefs.getString("previous_test_$i", "")!!

                    val item = MainItem(model, csc, latestOfficial, latestTest)
                    resultList.add(item)

                    val previous = PreviousItem(previousOfficial, previousTest)
                    previousList.add(previous)

                    date = searchResultPrefs.getString("first_discovery_date_$i", "")!!
                    downgrade = searchResultPrefs.getString("downgrade_$i", "")!!
                    type = searchResultPrefs.getString("type_$i", "")!!

                    val element = SmartSearchItem(date, downgrade, type)
                    smartList.add(element)
                }

                var mAdapter = MultiAdapter(this, isOfficial, smart, resultList, smartList, object : MultiAdapter.MyAdapterListener {
                    override fun onLayoutClicked(v: View, position: Int) {
                        val bottomSheetFragment = SearchDialog.newInstance(isOfficial, resultList[position].model, resultList[position].csc,
                                resultList[position].officialLatest, previousList[position].officialPrevious,
                                resultList[position].testLatest, previousList[position].testPrevious)
                        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                    }
                })
                mRecyclerView.adapter = mAdapter

                tab1.setOnClickListener {
                    tab1.setTextAppearance(R.style.SearchButton_Selected)
                    tab2.setTextAppearance(R.style.SearchButton_Unselected)
                    isOfficial = true
                    smart = false
                    mAdapter = MultiAdapter(this, isOfficial, smart, resultList, smartList, object : MultiAdapter.MyAdapterListener {
                        override fun onLayoutClicked(v: View, position: Int) {
                            val bottomSheetFragment = SearchDialog.newInstance(isOfficial, resultList[position].model, resultList[position].csc,
                                    resultList[position].officialLatest, previousList[position].officialPrevious,
                                    resultList[position].testLatest, previousList[position].testPrevious)
                            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                        }
                    })
                    mRecyclerView.adapter = mAdapter
                }

                tab2.setOnClickListener {
                    tab1.setTextAppearance(R.style.SearchButton_Unselected)
                    tab2.setTextAppearance(R.style.SearchButton_Selected)
                    isOfficial = false
                    smart = !sharedPrefs.getBoolean("china", false)
                    mAdapter = MultiAdapter(this, isOfficial, smart, resultList, smartList, object : MultiAdapter.MyAdapterListener {
                        override fun onLayoutClicked(v: View, position: Int) {
                            val bottomSheetFragment = SearchDialog.newInstance(isOfficial, resultList[position].model, resultList[position].csc,
                                    resultList[position].officialLatest, previousList[position].officialPrevious,
                                    resultList[position].testLatest, previousList[position].testPrevious)
                            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                        }
                    })
                    mRecyclerView.adapter = mAdapter
                }
            }

            mSwipeRefreshLayout.isEnabled = false
            mSwipeRefreshLayout.isRefreshing = false
        } else if (requestCode == 2 && resultCode == Activity.RESULT_CANCELED) {
            mSwipeRefreshLayout.isEnabled = false
            mSwipeRefreshLayout.isRefreshing = false
            Toast.makeText(this, getString(R.string.search_timeout), Toast.LENGTH_SHORT).show()
        } else {
            mSwipeRefreshLayout.isEnabled = false
            mSwipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                val intent = Intent(this, SearchActivity::class.java)
                startActivityForResult(intent, 1)
                return true
            }
            R.id.bookmark -> {
                val intent = Intent(this, BookmarkActivity::class.java)
                startActivityForResult(intent, 1)
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
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toolbarText = getString(R.string.app_name)
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

    private fun initQuick() {
        val chipScroll = findViewById<HorizontalScrollView>(R.id.chip_scroll)
        val quick = sharedPrefs.getBoolean("quick", false)
        if (quick) {
            viewModel = ViewModelProvider(this, CheckFirm.viewModelFactory).get(BookmarkViewModel::class.java)
            viewModel.allBookmarks.observe(this, androidx.lifecycle.Observer { bookmarks ->
                bookmarks?.let {
                    if (it.isEmpty()) {
                        chipScroll.visibility = View.GONE
                    } else {
                        val bookmarkChipGroup = findViewById<ChipGroup>(R.id.chip_group)
                        bookmarkChipGroup.removeAllViews()
                        for (element in it) {
                            val bookmarkChip = Chip(this)
                            bookmarkChip.text = element.name
                            bookmarkChip.isCheckable = false
                            bookmarkChip.setOnClickListener {
                                welcomeCardView.visibility = View.GONE
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
        if (Tools.isOnline(this)) {
            val total = searchDevicePrefs.getInt("welcome_search_total", 0)
            var model = ""
            var csc = ""

            for (i in 0..total) {
                val tempModel = searchDevicePrefs.getString("welcome_search_model_$i", "")
                val tempCsc = searchDevicePrefs.getString("welcome_search_csc_$i", "")

                model += if (tempModel!!.isBlank()) {
                    sharedPrefs.getString("new_saved_model", "SM-A720S") + "%"
                } else {
                    "$tempModel%"
                }

                csc += if (tempCsc!!.isBlank()) {
                    sharedPrefs.getString("new_saved_csc", "SKC") + "%"
                } else {
                    "$tempCsc%"
                }
            }
            welcomeCardView.visibility = View.GONE
            networkTask(model, csc, total + 1)
        } else {
            welcomeTitle.text = getString(R.string.check_network)
            welcomeText.text = getString(R.string.welcome_online)
            welcomeCardView.visibility = View.VISIBLE
        }
    }

    private fun networkTask(tempModel: String, tempCsc: String, total: Int) {
        if (tempModel.isBlank() || tempCsc.isBlank()) {
            Toast.makeText(this, R.string.info_catcher_error, Toast.LENGTH_SHORT).show()
        } else {
            mSwipeRefreshLayout.isEnabled = true
            mSwipeRefreshLayout.isRefreshing = true

            val editor = searchDevicePrefs.edit()
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
            startActivityForResult(intent, 2)
            overridePendingTransition(0, 0)
        }
    }
}