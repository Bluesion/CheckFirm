package com.illusion.checkfirm.search

import android.app.Activity.RESULT_OK
import android.content.*
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.illusion.checkfirm.help.HelpActivity
import com.illusion.checkfirm.R
import com.illusion.checkfirm.dialogs.SearchDialog
import com.illusion.checkfirm.settings.SettingsActivity
import com.illusion.checkfirm.utils.Tools
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

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
    internal lateinit var latestOfficial: String
    internal lateinit var latestTest: String
    internal lateinit var officialFirmware: ArrayList<String>
    internal lateinit var testFirmware: ArrayList<String>
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

    // Clipboard
    private var clipboard: ClipboardManager? = null
    private var clip: ClipData? = null

    // Firebase
    private lateinit var db: FirebaseFirestore
    private lateinit var dateLatest: String
    private lateinit var datePrevious: String
    private lateinit var latest: String
    private lateinit var previous: String
    private lateinit var expectedDateLatest: String
    private lateinit var expectedDatePrevious: String
    private lateinit var changeLogLatest: String
    private lateinit var changeLogPrevious: String
    private lateinit var downGradeLatest: String
    private lateinit var downGradePrevious: String

    // ETC
    private var save: Boolean = false
    private val mHandler = MyHandler()
    private lateinit var sharedPrefs: SharedPreferences
    private var previousOfficial = ""
    private var previousTest = ""
    private var model = ""
    private var csc = ""
    internal lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var modelOfficial: TextView
    private lateinit var modelTest: TextView

    private fun init(rootView: View) {
        downgrade = rootView.findViewById(R.id.smart_search_downgrade)
        detailCardView = rootView.findViewById(R.id.detail)
        expectedReleaseDate = rootView.findViewById(R.id.smart_search_release)
        changelog = rootView.findViewById(R.id.smart_search_changelog)
        firstDiscoveryDate = rootView.findViewById(R.id.smart_search_date)

        welcomeCardView = rootView.findViewById(R.id.welcome)
        sharedPrefs = activity!!.getSharedPreferences("settings", Context.MODE_PRIVATE)
        welcomeTitle = rootView.findViewById(R.id.welcome_title)
        welcomeText = rootView.findViewById(R.id.welcome_text)

        mResult = rootView.findViewById(R.id.result)

        mSwipeRefreshLayout = rootView.findViewById(R.id.mSwipeRefreshLayout)
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.green)
        mSwipeRefreshLayout.isEnabled = false

        modelOfficial = rootView.findViewById(R.id.model_official)
        modelTest = rootView.findViewById(R.id.model_test)
        latestOfficialFirmware = rootView.findViewById(R.id.latestOfficialFirmware)
        latestTestFirmware = rootView.findViewById(R.id.latestTestFirmware)
        latestOfficialFirmwareText = rootView.findViewById(R.id.latestOfficialFirmwareText)
        latestTestFirmwareText = rootView.findViewById(R.id.latestTestFirmwareText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        db = FirebaseFirestore.getInstance()

        init(rootView)
        welcomeSearch()
        save = sharedPrefs.getBoolean("saver", false)

        clipboard = activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val infoOfficialFirmware = rootView.findViewById<MaterialCardView>(R.id.officialCardView)
        infoOfficialFirmware.setOnClickListener {
            val bottomSheetFragment = SearchDialog.newInstance(true, previousOfficial, model, csc)
            bottomSheetFragment.show(activity!!.supportFragmentManager, bottomSheetFragment.tag)
        }
        infoOfficialFirmware.setOnLongClickListener {
            clip = ClipData.newPlainText("checkfirmLatestOfficial", latestOfficial)
            clipboard?.primaryClip = clip!!
            Toast.makeText(activity!!, R.string.clipboard, Toast.LENGTH_SHORT).show()
            true
        }

        val infoTestFirmware = rootView.findViewById<MaterialCardView>(R.id.testCardView)
        infoTestFirmware.setOnClickListener {
            val bottomSheetFragment = SearchDialog.newInstance(false, previousTest, model, csc)
            bottomSheetFragment.show(activity!!.supportFragmentManager, bottomSheetFragment.tag)
        }
        infoTestFirmware.setOnLongClickListener {
            clip = ClipData.newPlainText("checkfirmLatestTest", latestTest)
            clipboard?.primaryClip = clip!!
            Toast.makeText(activity!!, R.string.clipboard, Toast.LENGTH_SHORT).show()
            true
        }

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

        if (requestCode == 1 && resultCode == RESULT_OK) {
            model = data!!.getStringExtra("model")
            csc = data.getStringExtra("csc")
            val official = "$baseURL$csc/$model$officialURL"
            val test = "$baseURL$csc/$model$testURL"

            if (save) {
                if (Tools.isWifi(activity!!)) {
                    networkTask(official, test)
                } else {
                    Toast.makeText(activity!!, R.string.only_wifi, Toast.LENGTH_SHORT).show()
                }
            } else {
                if (Tools.isOnline(activity!!)) {
                    networkTask(official, test)
                } else {
                    Toast.makeText(activity!!, R.string.check_network, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun welcomeSearch() {
        val welcome = sharedPrefs.getBoolean("welcome", false)

        model = sharedPrefs.getString("welcome_model", "SM-A720S") as String
        csc = sharedPrefs.getString("welcome_csc", "SKC") as String
        if (welcome) {
            val official = "$baseURL$csc/$model$officialURL"
            val test = "$baseURL$csc/$model$testURL"
            if (save) {
                if (Tools.isWifi(activity!!)) {
                    networkTask(official, test)
                } else {
                    welcomeTitle.text = getString(R.string.wifi)
                    welcomeText.text = getString(R.string.welcome_wifi)
                    welcomeCardView.visibility = View.VISIBLE
                }
            } else {
                if (Tools.isOnline(activity!!)) {
                    networkTask(official, test)
                } else {
                    welcomeTitle.text = getString(R.string.online)
                    welcomeText.text = getString(R.string.welcome_online)
                    welcomeCardView.visibility = View.VISIBLE
                }
            }
        } else {
            welcomeTitle.text = getString(R.string.welcome_search)
            welcomeText.text = getString(R.string.welcome_disabled)
            welcomeCardView.visibility = View.VISIBLE
        }
    }

    private fun smartSearch() {
        val docRef = db.collection(model).document(csc)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val doc = task.result as DocumentSnapshot
                dateLatest = doc.get("date_latest").toString()
                datePrevious = doc.get("date_previous").toString()
                latest = doc.get("firmware_latest").toString()
                previous = doc.get("firmware_previous").toString()
                expectedDateLatest = doc.get("expected_date_latest").toString()
                expectedDatePrevious = doc.get("expected_date_previous").toString()
                changeLogLatest = doc.get("changelog_latest").toString()
                changeLogPrevious = doc.get("changelog_previous").toString()
                downGradeLatest = doc.get("downgrade_latest").toString()
                downGradePrevious = doc.get("downgrade_previous").toString()

                if (dateLatest == "null") {
                    add()
                } else {
                    val currentOfficial = if (latest.contains(".")) {
                        val index = latest.indexOf(".")
                        latest.substring(index - 6, index)
                    } else {
                        latest.substring(latest.length - 6, latest.length)
                    }

                    val currentTest = if (latestTest.contains(".")) {
                        val index = latestTest.indexOf(".")
                        latestTest.substring(index - 6, index)
                    } else {
                        latestTest.substring(latestTest.length - 6, latestTest.length)
                    }

                    val firestoreLatest = if (latest.contains(".")) {
                        val index = latest.indexOf(".")
                        latest.substring(index - 6, index)
                    } else {
                        latest.substring(latest.length - 6, latest.length)
                    }

                    val firestorePrevious = if (previous.contains(".")) {
                        val index = previous.indexOf(".")
                        previous.substring(index - 6, index)
                    } else {
                        previous.substring(previous.length - 6, previous.length)
                    }

                    firstDiscoveryDate.text = dateLatest
                    expectedReleaseDate.text = expectedDateLatest

                    when (changeLogLatest) {
                        "bug_fix" -> changelog.text = getString(R.string.smart_search_changelog_bugfix)
                        "os_update" -> changelog.text = getString(R.string.smart_search_changelog_os)
                        else -> changelog.text = getString(R.string.error)
                    }

                    when (downGradeLatest) {
                        "possible" -> downgrade.text = getString(R.string.smart_search_downgrade_possible)
                        "impossible" -> downgrade.text = getString(R.string.smart_search_downgrade_impossible)
                        else -> downgrade.text = getString(R.string.error)
                    }

                    if (latest != latestTest) {
                        var tempChangelog = ""
                        var tempDowngrade = ""
                        val transFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREAN)
                        val newDate = transFormat.parse(dateLatest)
                        val calendar = Calendar.getInstance()
                        calendar.time = newDate

                        val date = Tools.dateToString(Tools.getCurrentDateTime())
                        firstDiscoveryDate.text = date

                        if (currentOfficial[2] == currentTest[2]) {
                            tempChangelog = "bug_fix"
                            changelog.text = getString(R.string.smart_search_changelog_bugfix)
                            calendar.add(Calendar.DAY_OF_YEAR, 21)
                        } else {
                            tempChangelog = "os_update"
                            changelog.text = getString(R.string.smart_search_changelog_os)
                            if (currentTest[2] == firestorePrevious[2]) {
                                calendar.add(Calendar.DAY_OF_YEAR, 35)
                            } else {
                                calendar.add(Calendar.MONTH, 2)
                            }
                        }

                        val day = calendar.get(Calendar.DAY_OF_WEEK)
                        if (day == Calendar.SUNDAY) {
                            calendar.add(Calendar.DAY_OF_YEAR, 1)
                        } else if (day == Calendar.SATURDAY) {
                            calendar.add(Calendar.DAY_OF_YEAR, 2)
                        }
                        val d = Date(calendar.timeInMillis)
                        expectedReleaseDate.text = transFormat.format(d)

                        if (currentTest.substring(0, 1) == firestoreLatest.substring(0, 1)) {
                            tempDowngrade = "possible"
                            downgrade.text = getString(R.string.smart_search_downgrade_possible)
                        } else {
                            tempDowngrade = "impossible"
                            downgrade.text = getString(R.string.smart_search_downgrade_impossible)
                        }

                        update(transFormat.format(d), tempChangelog, tempDowngrade)
                    }
                }

                mSwipeRefreshLayout.isEnabled = false
                mSwipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun update(expectedDate: String, changeLog: String, downgrade: String) {
        val date = Tools.dateToString(Tools.getCurrentDateTime())
        val docRef = db.collection(model).document(csc)
        docRef.update("date_latest", date)
        docRef.update("date_previous", dateLatest)
        docRef.update("firmware_latest", latestTest)
        docRef.update("firmware_previous", latest)
        docRef.update("expected_date_latest", expectedDate)
        docRef.update("expected_date_previous", expectedDateLatest)
        docRef.update("changelog_latest", changeLog)
        docRef.update("changelog_previous", changeLogLatest)
        docRef.update("downgrade_latest", downgrade)
        docRef.update("downgrade_previous", downGradeLatest)
    }

    private fun add() {
        val date = Tools.dateToString(Tools.getCurrentDateTime())
        val items = HashMap<String, Any>()
        items["date_latest"] = date
        items["date_previous"] = date
        items["firmware_latest"] = latestTest
        items["firmware_previous"] = latestTest

        firstDiscoveryDate.text = date

        val currentOfficial = if (latestOfficial.contains(".")) {
            val index = latestOfficial.indexOf(".")
            latestOfficial.substring(index - 6, index)
        } else {
            latestOfficial.substring(latestOfficial.length - 6, latestOfficial.length)
        }

        val currentTest = if (latestTest.contains(".")) {
            val index = latestTest.indexOf(".")
            latestTest.substring(index - 6, index)
        } else {
            latestTest.substring(latestTest.length - 6, latestTest.length)
        }

        if (currentOfficial == currentTest) {
            items["expected_date_latest"] = date
            items["expected_date_previous"] = date
            expectedReleaseDate.text = date
        } else {
            val transFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREAN)
            val newDate = transFormat.parse(date)
            val calendar = Calendar.getInstance()
            calendar.time = newDate

            if (currentOfficial[2] == currentTest[2]) {
                items["changelog_latest"] = "bug_fix"
                items["changelog_previous"] = "bug_fix"
                changelog.text = getString(R.string.smart_search_changelog_bugfix)
                calendar.add(Calendar.DAY_OF_YEAR, 21)
            } else {
                items["changelog_latest"] = "os_update"
                items["changelog_previous"] = "os_update"
                changelog.text = getString(R.string.smart_search_changelog_os)
                calendar.add(Calendar.MONTH, 2)
            }

            val day = calendar.get(Calendar.DAY_OF_WEEK)
            if (day == Calendar.SUNDAY) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            } else if (day == Calendar.SATURDAY) {
                calendar.add(Calendar.DAY_OF_YEAR, 2)
            }
            val d = Date(calendar.timeInMillis)
            val h = transFormat.format(d)
            items["expected_date_latest"] = h
            items["expected_date_previous"] = h
            expectedReleaseDate.text = h

            if (currentOfficial.substring(0, 1) == currentTest.substring(0, 1)) {
                items["downgrade_latest"] = "possible"
                items["downgrade_previous"] = "possible"
                downgrade.text = getString(R.string.smart_search_downgrade_possible)
            } else {
                items["downgrade_latest"] = "impossible"
                items["downgrade_previous"] = "impossible"
                downgrade.text = getString(R.string.smart_search_downgrade_impossible)
            }
        }

        db.collection(model).document(csc).set(items)
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
    }

    private fun testResult() {
        if (latestOfficial.isNotBlank() && latestOfficial.isNotEmpty() && latestTest.isNotBlank() && latestTest.isNotEmpty()) {
            latestOfficialFirmwareText.text = latestOfficial
            latestTestFirmwareText.text = latestTest
        } else {
            latestOfficialFirmwareText.text = getString(R.string.search_error)
            latestTestFirmwareText.text = getString(R.string.search_error)
            detailCardView.visibility = View.GONE
        }
        welcomeCardView.visibility = View.GONE
        previousOfficial = officialFirmware.toString()
                .replace(", ", "\n")
                .replace("[", "")
                .replace("]", "")
        previousTest = testFirmware.toString()
                .replace(", ", "\n")
                .replace("[", "")
                .replace("]", "")
        modelOfficial.text = String.format(getString(R.string.device_format), model, csc)
        modelTest.text = String.format(getString(R.string.device_format), model, csc)
        mResult.visibility = View.VISIBLE
    }

    private fun networkTask(officialURL: String, testURL: String) {
        val beta = sharedPrefs.getBoolean("beta", false)

        officialFirmware = ArrayList()
        officialFirmware.clear()

        testFirmware = ArrayList()
        testFirmware.clear()

        latestOfficial = ""
        latestTest = ""

        dateLatest = ""
        datePrevious = ""
        latest = ""
        previous = ""
        expectedDateLatest = ""
        expectedDatePrevious = ""
        changeLogLatest = ""
        changeLogPrevious = ""
        downGradeLatest = ""
        downGradePrevious = ""

        object : Thread() {
            override fun run() {
                mHandler.post {
                    mSwipeRefreshLayout.isEnabled = true
                    mSwipeRefreshLayout.isRefreshing = true
                }
                try {
                    val official = Jsoup.parse(URL(officialURL).openStream(), "UTF-8", "", Parser.xmlParser())
                    for (el in official.select("latest")) {
                        latestOfficial = el.text()
                    }
                    for (el in official.select("value")) {
                        val firmwares = el.text()
                        officialFirmware.add(firmwares)
                    }

                    val test = Jsoup.parse(URL(testURL).openStream(), "UTF-8", "", Parser.xmlParser())
                    for (el in test.select("latest")) {
                        latestTest = el.text()
                    }
                    for (el in test.select("value")) {
                        val firmwares = el.text()
                        testFirmware.add(firmwares)
                    }

                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                mHandler.post {
                    smartSearch()
                    if (beta) {
                        detailCardView.visibility = View.VISIBLE
                    } else {
                        detailCardView.visibility = View.GONE
                        mSwipeRefreshLayout.isEnabled = false
                        mSwipeRefreshLayout.isRefreshing = false
                    }
                    testResult()
                }
            }
        }.start()
    }

    companion object {
        private class MyHandler : Handler() {
            override fun handleMessage(msg: Message) {}
        }
    }
}
