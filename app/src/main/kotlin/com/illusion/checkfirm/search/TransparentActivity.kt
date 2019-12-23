package com.illusion.checkfirm.search

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.illusion.checkfirm.R
import com.illusion.checkfirm.utils.Tools
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.*
import kotlin.collections.HashMap

class TransparentActivity : AppCompatActivity() {

    private val baseURL = "https://fota-cloud-dn.ospserver.net/firmware/"
    private val officialURL = "/version.xml"
    private val testURL = "/version.test.xml"

    private lateinit var searchDevice: SharedPreferences
    private lateinit var searchResult: SharedPreferences
    private lateinit var resultEditor: SharedPreferences.Editor

    // Firebase
    private lateinit var db: FirebaseFirestore

    // ETC
    private var model = ""
    private var csc = ""
    private var unknown = ""

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = FirebaseFirestore.getInstance()
        unknown = getString(R.string.smart_search_unknown)

        searchDevice = getSharedPreferences("search_device", Context.MODE_PRIVATE)
        searchResult = getSharedPreferences("search_result", Context.MODE_PRIVATE)
        resultEditor = searchResult.edit()

        val total = intent!!.getIntExtra("total", 1)
        for (i in 0 until total) {
            model = searchDevice.getString("search_model_$i", "")!!
            csc = searchDevice.getString("search_csc_$i", "")!!

            SearchThread(total, i, model, csc).start()
        }
    }

    inner class SearchThread(private val total: Int, private val current: Int, private val model: String, private val csc: String) : Thread() {

        private var latestOfficial = ""
        private var latestTest = ""
        private lateinit var officialFirmware: ArrayList<String>
        private lateinit var testFirmware: ArrayList<String>
        private var previousOfficial = ""
        private var previousTest = ""

        override fun run() {
            val urlOfficial = "$baseURL$csc/$model$officialURL"
            val urlTest = "$baseURL$csc/$model$testURL"
            officialFirmware = ArrayList()
            testFirmware = ArrayList()

            try {
                val official = Jsoup.parse(URL(urlOfficial).openStream(), "UTF-8", "", Parser.xmlParser())
                for (el in official.select("latest")) {
                    latestOfficial = el.text()
                }
                for (el in official.select("value")) {
                    val firmwares = el.text()
                    officialFirmware.add(firmwares)
                }

                val test = Jsoup.parse(URL(urlTest).openStream(), "UTF-8", "", Parser.xmlParser())
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
            resultEditor.putString("latest_official_$current", latestOfficial)
            resultEditor.putString("previous_official_$current", previousOfficial)
            resultEditor.putString("latest_test_$current", latestTest)
            resultEditor.putString("previous_test_$current", previousTest)
            intent.putExtra("total", total - 1)
            resultEditor.commit()

            if (latestOfficial.isBlank() && latestTest.isBlank()) {
                resultEditor.putString("first_discovery_date_$current", unknown)
                resultEditor.putString("changelog_$current", unknown)
                resultEditor.putString("downgrade_$current", unknown)
                resultEditor.apply()
                setResult(Activity.RESULT_OK, intent)
                finish()
                overridePendingTransition(0, 0)
            } else if (latestOfficial.isNotBlank() && latestTest.isBlank()) {
                resultEditor.putString("first_discovery_date_$current", unknown)
                resultEditor.putString("changelog_$current", unknown)
                resultEditor.putString("downgrade_$current", unknown)
                resultEditor.apply()
                setResult(Activity.RESULT_OK, intent)
                finish()
                overridePendingTransition(0, 0)
            } else if (latestOfficial.isBlank() && latestTest.isNotBlank()) {
                smartSearch(1, current)
            } else {
                smartSearch(0, current)
            }
        }
    }

    // @@@@@@ -> 검색 결과 없음
    // ###### -> 암호화 펌웨어
    private fun smartSearch(check: Int, current: Int) {
        val latestOfficial = searchResult.getString("latest_official_$current", "")!!
        val latestTest = searchResult.getString("latest_test_$current", "")!!
        val model = searchDevice.getString("search_model_$current", "")!!
        val csc = searchDevice.getString("search_csc_$current", "")!!

        val docRef = db.collection(model).document(csc)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val doc = task.result as DocumentSnapshot
                val firestoreDate = doc.get("date_latest").toString()
                val firestoreLatest = doc.get("firmware_latest").toString()

                if (firestoreDate == "null") {
                    add(check, current)
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

                    resultEditor.putString("first_discovery_date_$current", firestoreDate)

                    if (currentTest[2] == '#' || currentTest[2] == '@') {
                        resultEditor.putString("changelog_$current", unknown)
                        resultEditor.putString("downgrade_$current", unknown)
                    } else {
                        val compare = currentOfficial[2].compareTo(currentTest[2])
                        when {
                            compare < 0 -> {
                                resultEditor.putString("changelog_$current", getString(R.string.smart_search_changelog_os))
                            }
                            compare > 0 -> {
                                resultEditor.putString("changelog_$current", getString(R.string.smart_search_changelog_rollback))
                            }
                            else -> {
                                resultEditor.putString("changelog_$current", getString(R.string.smart_search_changelog_bugfix))
                            }
                        }

                        if (currentOfficial.substring(0, 2) == currentTest.substring(0, 2)) {
                            resultEditor.putString("downgrade_$current", getString(R.string.smart_search_downgrade_possible))
                        } else {
                            resultEditor.putString("downgrade_$current", getString(R.string.smart_search_downgrade_impossible))
                        }
                    }

                    if (firestoreLatest != latestTest) {
                        val newDate = Tools.dateToString(Tools.getCurrentDateTime())
                        resultEditor.putString("first_discovery_date_$current", newDate)
                        update(current)
                    }
                }
                resultEditor.commit()
                setResult(Activity.RESULT_OK, intent)
                finish()
                overridePendingTransition(0, 0)
            }
        }
    }

    private fun update(current: Int) {
        val model = searchDevice.getString("search_model_$current", "")!!
        val csc = searchDevice.getString("search_csc_$current", "")!!
        val latestTest = searchResult.getString("latest_test_$current", "")!!
        val date = Tools.dateToString(Tools.getCurrentDateTime())
        val docRef = db.collection(model).document(csc)
        docRef.update("date_latest", date)
        docRef.update("firmware_latest", latestTest)

        val items = HashMap<String, Any>()
        items["model"] = model
        items["csc"] = csc
        db.collection("A_NOTIFICATION").document("UPDATE").set(items)
    }

    private fun add(check: Int, current: Int) {
        val model = searchDevice.getString("search_model_$current", "")!!
        val csc = searchDevice.getString("search_csc_$current", "")!!
        val latestOfficial = searchResult.getString("latest_official_$current", "")!!
        val latestTest = searchResult.getString("latest_test_$current", "")!!
        val date = Tools.dateToString(Tools.getCurrentDateTime())
        val items = HashMap<String, Any>()
        items["date_latest"] = date
        items["firmware_latest"] = latestTest

        resultEditor.putString("first_discovery_date_$current", date)

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

        resultEditor.putString("changelog_$current", "")
        resultEditor.putString("downgrade_$current", "")

        if (currentOfficial == currentTest) {
            if (condition) {
                resultEditor.putString("changelog_$current", unknown)
                resultEditor.putString("downgrade_$current", unknown)
            } else {
                resultEditor.putString("changelog_$current", getString(R.string.smart_search_changelog_bugfix))
                resultEditor.putString("downgrade_$current", getString(R.string.smart_search_downgrade_possible))
            }
        } else {
            if (condition) {
                resultEditor.putString("changelog_$current", unknown)
            } else {
                val compare = currentOfficial[2].compareTo(currentTest[2])
                when {
                    compare < 0 -> {
                        resultEditor.putString("changelog_$current", getString(R.string.smart_search_changelog_os))
                    }
                    compare > 0 -> {
                        resultEditor.putString("changelog_$current", getString(R.string.smart_search_changelog_rollback))
                    }
                    else -> {
                        resultEditor.putString("changelog_$current", getString(R.string.smart_search_changelog_bugfix))
                    }
                }
            }

            if (currentOfficial.substring(0, 2) == currentTest.substring(0, 2)) {
                if (condition) {
                    resultEditor.putString("downgrade_$current", unknown)
                } else {
                    resultEditor.putString("downgrade_$current", getString(R.string.smart_search_downgrade_possible))
                }
            } else {
                resultEditor.putString("downgrade_$current", getString(R.string.smart_search_downgrade_impossible))
            }
        }

        db.collection(model).document(csc).set(items)
        resultEditor.apply()

        setResult(Activity.RESULT_OK, intent)
        finish()
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, getString(R.string.searching), Toast.LENGTH_SHORT).show()
    }
}