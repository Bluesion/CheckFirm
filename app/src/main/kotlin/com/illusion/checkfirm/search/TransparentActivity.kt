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
    private lateinit var date: String
    private lateinit var latest: String
    private lateinit var previous: String
    private lateinit var expected: String

    // ETC
    private var bookmark: Boolean = false
    private var model = ""
    private var csc = ""
    private var smart: Boolean = false

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

        officialFirmware = ArrayList()
        testFirmware = ArrayList()
        officialFirmware.clear()
        testFirmware.clear()
        latestOfficial = ""
        latestTest = ""
        date = ""
        latest = ""
        previous = ""
        expected = ""

        db = FirebaseFirestore.getInstance()
        bookmark = intent.getBooleanExtra("bookmarkSearch", false)
        model = intent.getStringExtra("model")!!
        csc = intent.getStringExtra("csc")!!

        val sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
        smart = sharedPrefs.getBoolean("smart", false)
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
                    officialFirmware.sort()
                    testFirmware.sort()
                    officialFirmware.reverse()
                    testFirmware.reverse()
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
                        intent.putExtra("firstDiscoveryDate", "")
                        intent.putExtra("expectedReleaseDate", "")
                        intent.putExtra("changelog", "")
                        intent.putExtra("downgrade", "")
                        intent.putExtra("detailCardView", false)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                        overridePendingTransition(0, 0)
                    } else if (latestOfficial.isNotBlank() && latestTest.isBlank()) {
                        intent.putExtra("firstDiscoveryDate", "")
                        intent.putExtra("expectedReleaseDate", "")
                        intent.putExtra("changelog", "")
                        intent.putExtra("downgrade", "")
                        intent.putExtra("detailCardView", false)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                        overridePendingTransition(0, 0)
                    } else if (latestOfficial.isBlank() && latestTest.isNotBlank()) {
                        search(1)
                    } else {
                        search(0)
                    }
                }
            }
        }.start()
    }

    // @@@@@@ -> 검색 결과 없음
    // ###### -> 암호화 펌웨어
    private fun smartSearch(check: Int) {
        val docRef = db.collection(model).document(csc)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val doc = task.result as DocumentSnapshot
                date = doc.get("date_latest").toString()
                latest = doc.get("firmware_latest").toString()
                previous = doc.get("firmware_previous").toString()
                expected = doc.get("expected_date_latest").toString()

                if (date == "null") {
                    add(check)
                } else {
                    val currentOfficial = if (check == 1) {
                        "@@@@@@@"
                    } else {
                        when {
                            latestOfficial.length < 6 -> {
                                "@@@@@@@"
                            }
                            latestOfficial.contains(".") -> {
                                val index = latestOfficial.indexOf(".")
                                latestOfficial.substring(index - 6, index)
                            }
                            latestOfficial.contains("/") -> {
                                val index = latestOfficial.indexOf("/")
                                latestOfficial.substring(index - 6, index)
                            }
                            else -> "######"
                        }
                    }

                    val currentTest = when {
                        latestTest.length < 6 -> {
                            "@@@@@@@"
                        }
                        latestTest.contains(".") -> {
                            val index = latestTest.indexOf(".")
                            latestTest.substring(index - 6, index)
                        }
                        latestTest.contains("/") -> {
                            val index = latestTest.indexOf("/")
                            latestTest.substring(index - 6, index)
                        }
                        else -> "######"
                    }

                    val firestorePrevious = if (previous == "null" || previous.isBlank()) {
                        "@@@@@@@"
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
                            else -> "######"
                        }
                    }

                    val transFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREAN)
                    val newDate = transFormat.parse(date)
                    val calendar = Calendar.getInstance()
                    calendar.time = newDate!!

                    intent.putExtra("firstDiscoveryDate", date)
                    intent.putExtra("expectedReleaseDate", expected)

                    if (currentTest[2] == '#' || currentTest[2] == '@') {
                        intent.putExtra("changelog", getString(R.string.smart_search_unknown))
                        intent.putExtra("downgrade", getString(R.string.smart_search_unknown))
                    } else {
                        val compare = currentOfficial[2].compareTo(currentTest[2])
                        when {
                            compare < 0 -> {
                                intent.putExtra("changelog", getString(R.string.smart_search_changelog_os))
                                if (currentTest[2] == firestorePrevious[2]) {
                                    calendar.add(Calendar.DAY_OF_YEAR, 35)
                                } else {
                                    calendar.add(Calendar.MONTH, 2)
                                }
                            }
                            compare > 0 -> {
                                intent.putExtra("changelog", getString(R.string.smart_search_changelog_rollback))
                            }
                            else -> {
                                intent.putExtra("changelog", getString(R.string.smart_search_changelog_bugfix))
                                calendar.add(Calendar.DAY_OF_YEAR, 21)
                            }
                        }

                        if (currentOfficial.substring(0, 2) == currentTest.substring(0, 2)) {
                            intent.putExtra("downgrade", getString(R.string.smart_search_downgrade_possible))
                        } else {
                            intent.putExtra("downgrade", getString(R.string.smart_search_downgrade_impossible))
                        }

                        if (latest != latestTest) {
                            val date = Tools.dateToString(Tools.getCurrentDateTime())
                            intent.putExtra("firstDiscoveryDate", date)

                            val day = calendar.get(Calendar.DAY_OF_WEEK)
                            if (day == Calendar.SUNDAY) {
                                calendar.add(Calendar.DAY_OF_YEAR, 1)
                            } else if (day == Calendar.SATURDAY) {
                                calendar.add(Calendar.DAY_OF_YEAR, 2)
                            }
                            val d = Date(calendar.timeInMillis)
                            intent.putExtra("expectedReleaseDate", transFormat.format(d))

                            update(transFormat.format(d))
                        }
                    }
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
                overridePendingTransition(0, 0)
            }
        }
    }

    private fun update(expected: String) {
        val date = Tools.dateToString(Tools.getCurrentDateTime())
        val docRef = db.collection(model).document(csc)
        docRef.update("date_latest", date)
        docRef.update("firmware_latest", latestTest)
        docRef.update("firmware_previous", latest)
        docRef.update("expected_date_latest", expected)

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
        items["firmware_latest"] = latestTest
        items["firmware_previous"] = latestTest

        intent.putExtra("firstDiscoveryDate", date)

        val currentOfficial = if (check == 1) {
            "@@@@@@"
        } else {
            when {
                latestOfficial.length < 6 -> {
                    "######"
                }
                latestOfficial.contains(".") -> {
                    val index = latestOfficial.indexOf(".")
                    latestOfficial.substring(index - 6, index)
                }
                latestOfficial.contains("/") -> {
                    val index = latestOfficial.indexOf("/")
                    latestOfficial.substring(index - 6, index)
                }
                else -> "######"
            }
        }

        val currentTest = when {
            latestTest.length < 6 -> {
                "######"
            }
            latestTest.contains(".") -> {
                val index = latestTest.indexOf(".")
                latestTest.substring(index - 6, index)
            }
            latestTest.contains("/") -> {
                val index = latestTest.indexOf("/")
                latestTest.substring(index - 6, index)
            }
            else -> "######"
        }

        val condition = currentTest == "######"

        if (currentOfficial == currentTest) {
            items["expected_date_latest"] = date
            if (condition) {
                intent.putExtra("expectedReleaseDate", getString(R.string.smart_search_unknown))
                intent.putExtra("changelog", getString(R.string.smart_search_unknown))
                intent.putExtra("downgrade", getString(R.string.smart_search_unknown))
            } else {
                intent.putExtra("expectedReleaseDate", date)
                intent.putExtra("changelog", getString(R.string.smart_search_changelog_bugfix))
                intent.putExtra("downgrade", getString(R.string.smart_search_downgrade_possible))
            }
        } else {
            val transFormat = SimpleDateFormat("yyyy/MM/dd", Locale.KOREAN)
            val newDate = transFormat.parse(date)
            val calendar = Calendar.getInstance()
            calendar.time = newDate!!

            if (condition) {
                intent.putExtra("changelog", getString(R.string.smart_search_unknown))
            } else {
                val compare = currentOfficial[2].compareTo(currentTest[2])
                when {
                    compare < 0 -> {
                        intent.putExtra("changelog", getString(R.string.smart_search_changelog_os))
                        calendar.add(Calendar.MONTH, 2)
                    }
                    compare > 0 -> {
                        intent.putExtra("changelog", getString(R.string.smart_search_changelog_rollback))
                    }
                    else -> {
                        intent.putExtra("changelog", getString(R.string.smart_search_changelog_bugfix))
                        calendar.add(Calendar.DAY_OF_YEAR, 21)
                    }
                }
            }

            if (currentOfficial.substring(0, 2) == currentTest.substring(0, 2)) {
                if (condition) {
                    intent.putExtra("downgrade", getString(R.string.smart_search_unknown))
                } else {
                    intent.putExtra("downgrade", getString(R.string.smart_search_downgrade_possible))
                }
            } else {
                intent.putExtra("downgrade", getString(R.string.smart_search_downgrade_impossible))
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
            intent.putExtra("expectedReleaseDate", h)
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

    private fun search(code: Int) {
        if (smart) {
            smartSearch(code)
        } else {
            setResult(Activity.RESULT_OK, intent)
            finish()
            overridePendingTransition(0, 0)
        }
    }
}