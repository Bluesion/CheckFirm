package com.illusion.checkfirm

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.messaging.FirebaseMessaging
import com.illusion.checkfirm.bookmark.BookmarkActivity
import com.illusion.checkfirm.database.BookmarkDB
import com.illusion.checkfirm.database.BookmarkDBHelper
import com.illusion.checkfirm.dialogs.SearchDialog
import com.illusion.checkfirm.help.HelpFirmware
import com.illusion.checkfirm.search.SearchActivity
import com.illusion.checkfirm.search.TransparentActivity
import com.illusion.checkfirm.settings.SettingsActivity
import com.illusion.checkfirm.utils.Tools
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    // URL
    private val baseURL = "https://fota-cloud-dn.ospserver.net/firmware/"
    private val officialURL = "/version.xml"
    private val testURL = "/version.test.xml"

    // Welcome Search
    private lateinit var welcomeCardView: MaterialCardView
    private lateinit var welcomeTitle: TextView
    private lateinit var welcomeText: TextView

    // Search Result
    private lateinit var mResult: LinearLayout
    private lateinit var latestOfficialFirmware: TextView
    private lateinit var latestTestFirmware: TextView
    private lateinit var latestOfficialFirmwareText: TextView
    private lateinit var latestTestFirmwareText: TextView

    // Smart Search
    private lateinit var firstDiscoveryDate: TextView
    private lateinit var expectedReleaseDate: TextView
    private lateinit var downgrade: TextView
    private lateinit var changelog: TextView
    private lateinit var detailCardView: MaterialCardView

    // Quick Search
    private var bookmarkList = ArrayList<BookmarkDB>()

    // ETC
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var modelOfficial: TextView
    private lateinit var modelTest: TextView
    private lateinit var infoOfficialFirmware: MaterialCardView
    private lateinit var infoTestFirmware: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val catcher = sharedPrefs.getBoolean("catcher", false)
        if (catcher) {
            val model = sharedPrefs.getString("catcher_model", "CheckFirm") as String
            val csc = sharedPrefs.getString("catcher_csc", "Catcher") as String
            if (model.isNotBlank() && csc.isNotBlank()) {
                FirebaseMessaging.getInstance().subscribeToTopic(model+csc)
            }
        }

        // UI
        mSwipeRefreshLayout = findViewById(R.id.mSwipeRefreshLayout)
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.green)
        mSwipeRefreshLayout.isEnabled = false

        // Smart Search
        detailCardView = findViewById(R.id.detail)
        firstDiscoveryDate = findViewById(R.id.smart_search_date)
        expectedReleaseDate = findViewById(R.id.smart_search_release)
        changelog = findViewById(R.id.smart_search_changelog)
        downgrade = findViewById(R.id.smart_search_downgrade)

        // Welcome Search
        welcomeCardView = findViewById(R.id.welcome)
        welcomeTitle = findViewById(R.id.welcome_title)
        welcomeText = findViewById(R.id.welcome_text)

        val welcome = sharedPrefs.getBoolean("welcome", false)
        if (welcome) {
            welcomeSearch()
        } else {
            welcomeTitle.text = getString(R.string.welcome_search)
            welcomeText.text = getString(R.string.welcome_disabled)
            welcomeCardView.visibility = View.VISIBLE
        }

        // Search Result
        mResult = findViewById(R.id.result)
        modelOfficial = findViewById(R.id.model_official)
        modelTest = findViewById(R.id.model_test)
        latestOfficialFirmware = findViewById(R.id.latestOfficialFirmware)
        latestTestFirmware = findViewById(R.id.latestTestFirmware)
        latestOfficialFirmwareText = findViewById(R.id.latestOfficialFirmwareText)
        latestTestFirmwareText = findViewById(R.id.latestTestFirmwareText)
        infoOfficialFirmware = findViewById(R.id.officialCardView)
        infoTestFirmware = findViewById(R.id.testCardView)

        initToolbar()
        initQuick()
        initHelpButton()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val save = sharedPrefs.getBoolean("saver", false)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val model = data!!.getStringExtra("model") as String
            val csc = data.getStringExtra("csc") as String

            if (save) {
                if (Tools.isWifi(this)) {
                    welcomeCardView.visibility = View.GONE
                    networkTask(model, csc)
                } else {
                    Toast.makeText(this, R.string.only_wifi, Toast.LENGTH_SHORT).show()
                }
            } else {
                if (Tools.isOnline(this)) {
                    welcomeCardView.visibility = View.GONE
                    networkTask(model, csc)
                } else {
                    Toast.makeText(this, R.string.check_network, Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            mResult.visibility = View.VISIBLE
            detailCardView.visibility = View.GONE

            val model = data!!.getStringExtra("model")!!
            val csc = data.getStringExtra("csc")!!
            val latestOfficial = data.getStringExtra("latestOfficial")!!
            val latestTest = data.getStringExtra("latestTest")!!
            val previousOfficial = data.getStringExtra("previousOfficial")!!
            val previousTest = data.getStringExtra("previousTest")!!

            modelOfficial.text = String.format(getString(R.string.device_format), model, csc)
            modelTest.text = String.format(getString(R.string.device_format), model, csc)

            if (latestOfficial.isBlank()) {
                latestOfficialFirmwareText.text = getString(R.string.search_error)
            } else {
                latestOfficialFirmwareText.text = latestOfficial
            }

            if (latestTest.isBlank()) {
                latestTestFirmwareText.text = getString(R.string.search_error)
            } else {
                latestTestFirmwareText.text = latestTest
            }

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            infoOfficialFirmware.setOnClickListener {
                val bottomSheetFragment = SearchDialog.newInstance(true, previousOfficial, model, csc)
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }
            infoOfficialFirmware.setOnLongClickListener {
                val clip = ClipData.newPlainText("checkfirmLatestOfficial", latestOfficial)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, R.string.clipboard, Toast.LENGTH_SHORT).show()
                true
            }
            infoTestFirmware.setOnClickListener {
                val bottomSheetFragment = SearchDialog.newInstance(false, previousTest, model, csc)
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }
            infoTestFirmware.setOnLongClickListener {
                val clip = ClipData.newPlainText("checkfirmLatestTest", latestTest)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, R.string.clipboard, Toast.LENGTH_SHORT).show()
                true
            }

            val smart = sharedPrefs.getBoolean("smart", false)
            if (smart) {
                detailCardView.visibility = View.VISIBLE
                val srFirstDiscoveryDate = data.getStringExtra("firstDiscoveryDate")
                val srExpectedReleaseDate = data.getStringExtra("expectedReleaseDate")
                val srChangelog = data.getStringExtra("changelog")
                val srDowngrade = data.getStringExtra("downgrade")
                val detail = data.getBooleanExtra("detailCardView", true)

                if (detail) {
                    detailCardView.visibility = View.VISIBLE
                } else {
                    detailCardView.visibility = View.GONE
                }

                firstDiscoveryDate.text = srFirstDiscoveryDate
                expectedReleaseDate.text = srExpectedReleaseDate
                changelog.text = srChangelog
                downgrade.text = srDowngrade
            }

            mSwipeRefreshLayout.isEnabled = false
            mSwipeRefreshLayout.isRefreshing = false
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
        initHelpButton()
        initQuick()
    }

    private fun initToolbar() {
        val one = sharedPrefs.getBoolean("one", true)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = ""
        toolbar.overflowIcon = getDrawable(R.drawable.ic_more)
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
    }

    private fun initQuick() {
        bookmarkList = ArrayList()
        bookmarkList.clear()
        val chipScroll = findViewById<HorizontalScrollView>(R.id.chipScroll)
        val quick = sharedPrefs.getBoolean("quick", false)
        if (quick) {
            val bookmarkHelper = BookmarkDBHelper(this)
            bookmarkList.addAll(bookmarkHelper.allBookmarkDB)

            if (bookmarkList.isEmpty()) {
                chipScroll.visibility = View.GONE
            } else {
                val bookmarkChipGroup = findViewById<ChipGroup>(R.id.chipGroup)
                bookmarkChipGroup.removeAllViews()
                chipScroll.visibility = View.VISIBLE
                for (i in bookmarkList.indices) {
                    val bookmarkChip = Chip(this)
                    bookmarkChip.text = bookmarkList[i].name
                    bookmarkChip.isCheckable = false
                    bookmarkChip.setOnClickListener {
                        welcomeCardView.visibility = View.GONE
                        networkTask(bookmarkList[i].model!!, bookmarkList[i].csc!!)
                    }
                    bookmarkChipGroup.addView(bookmarkChip)
                }
            }
        } else {
            chipScroll.visibility = View.GONE
        }
    }

    private fun initHelpButton() {
        val helpButton = findViewById<MaterialButton>(R.id.help)
        helpButton.setOnClickListener {
            val intent = Intent(this, HelpFirmware::class.java)
            startActivity(intent)
        }
        val help = sharedPrefs.getBoolean("help", true)
        if (help) {
            helpButton.visibility = View.VISIBLE
        } else {
            helpButton.visibility = View.GONE
        }
    }

    private fun welcomeSearch() {
        val save = sharedPrefs.getBoolean("saver", false)
        val model = sharedPrefs.getString("welcome_model", "SM-A720S")!!.trim()
        val csc = sharedPrefs.getString("welcome_csc", "SKC")!!.trim()

        if (model.isNotBlank() && csc.isNotBlank()) {
            if (save) {
                if (Tools.isWifi(this)) {
                    welcomeCardView.visibility = View.GONE
                    networkTask(model, csc)
                } else {
                    welcomeTitle.text = getString(R.string.wifi)
                    welcomeText.text = getString(R.string.welcome_wifi)
                    welcomeCardView.visibility = View.VISIBLE
                }
            } else {
                if (Tools.isOnline(this)) {
                    welcomeCardView.visibility = View.GONE
                    networkTask(model, csc)
                } else {
                    welcomeTitle.text = getString(R.string.online)
                    welcomeText.text = getString(R.string.welcome_online)
                    welcomeCardView.visibility = View.VISIBLE
                }
            }
        } else {
            welcomeTitle.text = getString(R.string.error)
            welcomeText.text = getString(R.string.search_error)
            welcomeCardView.visibility = View.VISIBLE
        }
    }

    private fun networkTask(model: String, csc: String) {
        if (model.isBlank() || csc.isBlank()) {
            Toast.makeText(this, R.string.info_catcher_error, Toast.LENGTH_SHORT).show()
        } else {
            mSwipeRefreshLayout.isEnabled = true
            mSwipeRefreshLayout.isRefreshing = true

            val officialURL = "$baseURL$csc/$model$officialURL"
            val testURL = "$baseURL$csc/$model$testURL"

            val intent = Intent(this, TransparentActivity::class.java)
            intent.putExtra("officialURL", officialURL)
            intent.putExtra("testURL", testURL)
            intent.putExtra("model", model)
            intent.putExtra("csc", csc)
            startActivityForResult(intent, 2)
            overridePendingTransition(0, 0)
        }
    }
}