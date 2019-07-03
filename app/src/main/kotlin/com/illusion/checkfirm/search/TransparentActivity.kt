package com.illusion.checkfirm.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.illusion.checkfirm.R
import com.illusion.checkfirm.utils.Tools
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class TransparentActivity : AppCompatActivity() {

    internal lateinit var latestOfficial: String
    internal lateinit var latestTest: String
    internal lateinit var officialFirmware: ArrayList<String>
    internal lateinit var testFirmware: ArrayList<String>
    private var previousOfficial = ""
    private var previousTest = ""
    private var catcher: Boolean = false

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
    private var model = ""
    private var csc = ""

    private class MyHandler(activity: TransparentActivity) : Handler() {
        private val mActivity: WeakReference<TransparentActivity> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            val activity = mActivity.get()
            activity?.handleMessage()
        }
    }

    private fun handleMessage() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clear()

        db = FirebaseFirestore.getInstance()
        model = intent.getStringExtra("model") as String
        csc = intent.getStringExtra("csc") as String

        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val smart = sharedPrefs.getBoolean("smart", false)
        catcher = sharedPrefs.getBoolean("catcher", false)

        object : Thread() {
            val mHandler = MyHandler(this@TransparentActivity)
            override fun run() {
                try {
                    val official = Jsoup.parse(URL(intent.getStringExtra("officialURL")).openStream(), "UTF-8", "", Parser.xmlParser())
                    for (el in official.select("latest")) {
                        latestOfficial = el.text()
                    }
                    for (el in official.select("value")) {
                        val firmwares = el.text()
                        officialFirmware.add(firmwares)
                    }

                    val test = Jsoup.parse(URL(intent.getStringExtra("testURL")).openStream(), "UTF-8", "", Parser.xmlParser())
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
                    previousOfficial = officialFirmware.toString()
                            .replace(", ", "\n")
                            .replace("[", "")
                            .replace("]", "")
                    previousTest = testFirmware.toString()
                            .replace(", ", "\n")
                            .replace("[", "")
                            .replace("]", "")
                    intent.putExtra("previousOfficial", previousOfficial)
                    intent.putExtra("previousTest", previousTest)
                    intent.putExtra("latestOfficial", latestOfficial)
                    intent.putExtra("latestTest", latestTest)
                    if (latestOfficial.isBlank() && latestTest.isBlank()) {
                        if (smart) {
                            smartSearch(0)
                        } else {
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                            overridePendingTransition(0, 0)
                        }
                    } else if (latestOfficial.isNotBlank() && latestTest.isBlank()) {
                        if (smart) {
                            smartSearch(1)
                        } else {
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                            overridePendingTransition(0, 0)
                        }
                    } else if (latestOfficial.isBlank() && latestTest.isNotBlank()) {
                        if (smart) {
                            smartSearch(2)
                        } else {
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                            overridePendingTransition(0, 0)
                        }
                    } else {
                        if (smart) {
                            smartSearch(3)
                        } else {
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                            overridePendingTransition(0, 0)
                        }
                    }
                }
            }
        }.start()
    }

    private fun smartSearch(check: Int) {
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
                    if (check == 2 || check == 3) {
                        add(check)
                    }
                } else {
                    intent.putExtra("firstDiscoveryDate", dateLatest)
                    intent.putExtra("expectedReleaseDate", expectedDateLatest)

                    when (changeLogLatest) {
                        "bug_fix" -> intent.putExtra("changelog", getString(R.string.smart_search_changelog_bugfix))
                        "os_update" -> intent.putExtra("changelog", getString(R.string.smart_search_changelog_os))
                        else -> intent.putExtra("changelog", getString(R.string.error))
                    }

                    when (downGradeLatest) {
                        "possible" -> intent.putExtra("downgrade", getString(R.string.smart_search_downgrade_possible))
                        "impossible" -> intent.putExtra("downgrade", getString(R.string.smart_search_downgrade_impossible))
                        else -> intent.putExtra("downgrade", getString(R.string.error))
                    }

                    val currentOfficial = if (check == 0 || check == 1) {
                        "&&&&&&"
                    } else {
                        when {
                            latestOfficial.contains(".") -> {
                                val index = latestOfficial.indexOf(".")
                                latestOfficial.substring(index - 6, index)
                            }
                            latestOfficial.contains("/") -> {
                                val index = latestOfficial.indexOf("/")
                                latestOfficial.substring(index - 6, index)
                            }
                            else -> latestOfficial
                        }
                    }

                    val currentTest = if (check == 0 || check == 2) {
                        "&&&&&&"
                    } else {
                        when {
                            latestTest.contains(".") -> {
                                val index = latestTest.indexOf(".")
                                latestTest.substring(index - 6, index)
                            }
                            latestTest.contains("/") -> {
                                val index = latestTest.indexOf("/")
                                latestTest.substring(index - 6, index)
                            }
                            else -> latestTest
                        }
                    }

                    var firestoreLatest = ""
                    firestoreLatest = if (latest == "null" || latest.isBlank()) {
                        "&&&&&&"
                    } else {
                        when {
                            latest.contains(".") -> {
                                val index = latest.indexOf(".")
                                latest.substring(index - 6, index)
                            }
                            latest.contains("/") -> {
                                val index = latest.indexOf("/")
                                latest.substring(index - 6, index)
                            }
                            else -> latest
                        }
                    }

                    var firestorePrevious = ""
                    firestorePrevious = if (previous == "null" || previous.isBlank()) {
                        "&&&&&&"
                    } else {
                        when {
                            previous.contains(".") -> {
                                val index = previous.indexOf(".")
                                previous.substring(index - 6, index)
                            }
                            previous.contains("/") -> {
                                val index = previous.indexOf("/")
                                previous.substring(index - 6, index)
                            }
                            else -> previous
                        }
                    }

                    if (latest != latestTest) {
                        var tempChangelog = ""
                        var tempDowngrade = ""
                        val transFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREAN)
                        val newDate = transFormat.parse(dateLatest)
                        val calendar = Calendar.getInstance()
                        calendar.time = newDate!!

                        val date = Tools.dateToString(Tools.getCurrentDateTime())
                        intent.putExtra("firstDiscoveryDate", date)

                        if (currentOfficial[2] == currentTest[2]) {
                            if (currentOfficial == "&&&&&&") {
                                intent.putExtra("detailCardView", false)
                            }
                            tempChangelog = "bug_fix"
                            intent.putExtra("changelog", getString(R.string.smart_search_changelog_bugfix))
                            calendar.add(Calendar.DAY_OF_YEAR, 21)
                        } else {
                            tempChangelog = "os_update"
                            intent.putExtra("changelog", getString(R.string.smart_search_changelog_os))
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
                        intent.putExtra("expectedReleaseDate", transFormat.format(d))

                        if (currentTest.substring(0, 1) == firestoreLatest.substring(0, 1)) {
                            tempDowngrade = "possible"
                            intent.putExtra("downgrade", getString(R.string.smart_search_downgrade_possible))
                        } else {
                            tempDowngrade = "impossible"
                            intent.putExtra("downgrade", getString(R.string.smart_search_downgrade_impossible))
                        }

                        update(transFormat.format(d), tempChangelog, tempDowngrade)
                    }
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
                overridePendingTransition(0, 0)
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

        if (catcher) {
            val items = HashMap<String, Any>()
            items["model"] = model
            items["csc"] = csc
            db.collection("A_NOTIFICATION").document("UPDATE").set(items)
        }
    }

    private fun add(check: Int) {
        val date = Tools.dateToString(Tools.getCurrentDateTime())
        val items = HashMap<String, Any>()
        items["date_latest"] = date
        items["date_previous"] = date

        intent.putExtra("firstDiscoveryDate", date)

        val currentOfficial = if (check == 0 || check == 1) {
            "&&&&&&"
        } else {
            when {
                latestOfficial.contains(".") -> {
                    val index = latestOfficial.indexOf(".")
                    latestOfficial.substring(index - 6, index)
                }
                latestOfficial.contains("/") -> {
                    val index = latestOfficial.indexOf("/")
                    latestOfficial.substring(index - 6, index)
                }
                else -> latestOfficial
            }
        }

        val currentTest = if (check == 0 || check == 2) {
            "&&&&&&"
        } else {
            when {
                latestTest.contains(".") -> {
                    val index = latestTest.indexOf(".")
                    latestTest.substring(index - 6, index)
                }
                latestTest.contains("/") -> {
                    val index = latestTest.indexOf("/")
                    latestTest.substring(index - 6, index)
                }
                else -> latestTest
            }
        }

        if (currentTest != "&&&&&&") {
            items["firmware_latest"] = latestTest
            items["firmware_previous"] = latestTest
        }

        if (currentOfficial == currentTest) {
            if (currentOfficial == "&&&&&&") {
                intent.putExtra("detailCardView", false)
            }
            items["expected_date_latest"] = date
            items["expected_date_previous"] = date
            items["changelog_latest"] = "bug_fix"
            items["changelog_previous"] = "bug_fix"
            items["downgrade_latest"] = "possible"
            items["downgrade_previous"] = "possible"
            intent.putExtra("expectedReleaseDate", date)
            intent.putExtra("changelog", getString(R.string.smart_search_changelog_bugfix))
            intent.putExtra("downgrade", getString(R.string.smart_search_downgrade_possible))
        } else {
            val transFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREAN)
            val newDate = transFormat.parse(date)
            val calendar = Calendar.getInstance()
            calendar.time = newDate!!

            if (currentOfficial[2] == currentTest[2]) {
                items["changelog_latest"] = "bug_fix"
                items["changelog_previous"] = "bug_fix"
                intent.putExtra("changelog", getString(R.string.smart_search_changelog_bugfix))
                calendar.add(Calendar.DAY_OF_YEAR, 21)
            } else {
                items["changelog_latest"] = "os_update"
                items["changelog_previous"] = "os_update"
                intent.putExtra("changelog", getString(R.string.smart_search_changelog_os))
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
            intent.putExtra("expectedReleaseDate", h)

            if (currentOfficial.substring(0, 2) == currentTest.substring(0, 2)) {
                items["downgrade_latest"] = "possible"
                items["downgrade_previous"] = "possible"
                intent.putExtra("downgrade", getString(R.string.smart_search_downgrade_possible))
            } else {
                items["downgrade_latest"] = "impossible"
                items["downgrade_previous"] = "impossible"
                intent.putExtra("downgrade", getString(R.string.smart_search_downgrade_impossible))
            }
        }

        db.collection(model).document(csc).set(items)

        setResult(Activity.RESULT_OK, intent)
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, getString(R.string.searching), Toast.LENGTH_SHORT).show()
    }

    private fun clear() {
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
    }
}