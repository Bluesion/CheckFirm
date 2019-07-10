package com.illusion.checkfirm.search

import android.app.Activity.RESULT_OK
import android.content.*
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.card.MaterialCardView
import com.illusion.checkfirm.help.HelpActivity
import com.illusion.checkfirm.R
import com.illusion.checkfirm.dialogs.SearchDialog
import com.illusion.checkfirm.settings.SettingsActivity
import com.illusion.checkfirm.utils.Tools

class Search : Fragment() {

    // URL
    private var baseURL = "https://fota-cloud-dn.ospserver.net/firmware/"
    private var officialURL = "/version.xml"
    private var testURL = "/version.test.xml"

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

    // ETC
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var modelOfficial: TextView
    private lateinit var modelTest: TextView
    private lateinit var infoOfficialFirmware: MaterialCardView
    private lateinit var infoTestFirmware: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        sharedPrefs = activity!!.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        // UI
        mSwipeRefreshLayout = rootView.findViewById(R.id.mSwipeRefreshLayout)
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.green)
        mSwipeRefreshLayout.isEnabled = false

        // Smart Search
        detailCardView = rootView.findViewById(R.id.detail)
        firstDiscoveryDate = rootView.findViewById(R.id.smart_search_date)
        expectedReleaseDate = rootView.findViewById(R.id.smart_search_release)
        changelog = rootView.findViewById(R.id.smart_search_changelog)
        downgrade = rootView.findViewById(R.id.smart_search_downgrade)

        // Welcome Search
        welcomeCardView = rootView.findViewById(R.id.welcome)
        welcomeTitle = rootView.findViewById(R.id.welcome_title)
        welcomeText = rootView.findViewById(R.id.welcome_text)

        val welcome = sharedPrefs.getBoolean("welcome", false)
        if (welcome) {
            welcomeSearch()
        } else {
            welcomeTitle.text = getString(R.string.welcome_search)
            welcomeText.text = getString(R.string.welcome_disabled)
            welcomeCardView.visibility = View.VISIBLE
        }

        // Search Result
        mResult = rootView.findViewById(R.id.result)
        modelOfficial = rootView.findViewById(R.id.model_official)
        modelTest = rootView.findViewById(R.id.model_test)
        latestOfficialFirmware = rootView.findViewById(R.id.latestOfficialFirmware)
        latestTestFirmware = rootView.findViewById(R.id.latestTestFirmware)
        latestOfficialFirmwareText = rootView.findViewById(R.id.latestOfficialFirmwareText)
        latestTestFirmwareText = rootView.findViewById(R.id.latestTestFirmwareText)
        infoOfficialFirmware = rootView.findViewById(R.id.officialCardView)
        infoTestFirmware = rootView.findViewById(R.id.testCardView)

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                val intent = Intent(activity!!, SearchActivity::class.java)
                startActivityForResult(intent, 1)
                return true
            }
            R.id.help -> {
                val intent = Intent(activity!!, HelpActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.settings -> {
                val intent = Intent(activity!!, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val save = sharedPrefs.getBoolean("saver", false)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val model = data!!.getStringExtra("model") as String
            val csc = data.getStringExtra("csc") as String

            if (save) {
                if (Tools.isWifi(activity!!)) {
                    welcomeCardView.visibility = View.GONE
                    networkTask(model, csc)
                } else {
                    Toast.makeText(activity!!, R.string.only_wifi, Toast.LENGTH_SHORT).show()
                }
            } else {
                if (Tools.isOnline(activity!!)) {
                    welcomeCardView.visibility = View.GONE
                    networkTask(model, csc)
                } else {
                    Toast.makeText(activity!!, R.string.check_network, Toast.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            mResult.visibility = View.VISIBLE
            detailCardView.visibility = View.GONE

            val model = data!!.getStringExtra("model") as String
            val csc = data.getStringExtra("csc") as String
            val latestOfficial = data.getStringExtra("latestOfficial") as String
            val latestTest = data.getStringExtra("latestTest") as String
            val previousOfficial = data.getStringExtra("previousOfficial") as String
            val previousTest = data.getStringExtra("previousTest") as String

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

            val clipboard = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            infoOfficialFirmware.setOnClickListener {
                val bottomSheetFragment = SearchDialog.newInstance(true, previousOfficial, model, csc)
                bottomSheetFragment.show(activity!!.supportFragmentManager, bottomSheetFragment.tag)
            }
            infoOfficialFirmware.setOnLongClickListener {
                val clip = ClipData.newPlainText("checkfirmLatestOfficial", latestOfficial)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(activity!!, R.string.clipboard, Toast.LENGTH_SHORT).show()
                true
            }
            infoTestFirmware.setOnClickListener {
                val bottomSheetFragment = SearchDialog.newInstance(false, previousTest, model, csc)
                bottomSheetFragment.show(activity!!.supportFragmentManager, bottomSheetFragment.tag)
            }
            infoTestFirmware.setOnLongClickListener {
                val clip = ClipData.newPlainText("checkfirmLatestTest", latestTest)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(activity!!, R.string.clipboard, Toast.LENGTH_SHORT).show()
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
        }
    }

    private fun welcomeSearch() {
        val save = sharedPrefs.getBoolean("saver", false)

        val model = sharedPrefs.getString("welcome_model", "SM-A720S")!!.trim()
        val csc = sharedPrefs.getString("welcome_csc", "SKC")!!.trim()

        if (model.isNotBlank() && csc.isNotBlank()) {
            if (save) {
                if (Tools.isWifi(activity!!)) {
                    welcomeCardView.visibility = View.GONE
                    networkTask(model, csc)
                } else {
                    welcomeTitle.text = getString(R.string.wifi)
                    welcomeText.text = getString(R.string.welcome_wifi)
                    welcomeCardView.visibility = View.VISIBLE
                }
            } else {
                if (Tools.isOnline(activity!!)) {
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
            Toast.makeText(activity!!, R.string.info_catcher_error, Toast.LENGTH_SHORT).show()
        } else {
            mSwipeRefreshLayout.isEnabled = true
            mSwipeRefreshLayout.isRefreshing = true

            val officialURL = "$baseURL$csc/$model$officialURL"
            val testURL = "$baseURL$csc/$model$testURL"

            val intent = Intent(activity!!, TransparentActivity::class.java)
            intent.putExtra("officialURL", officialURL)
            intent.putExtra("testURL", testURL)
            intent.putExtra("model", model)
            intent.putExtra("csc", csc)
            startActivityForResult(intent, 2)
            activity!!.overridePendingTransition(0, 0)
        }
    }
}