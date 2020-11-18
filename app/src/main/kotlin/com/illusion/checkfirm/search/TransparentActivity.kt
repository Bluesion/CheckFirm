package com.illusion.checkfirm.search

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.illusion.checkfirm.CheckFirm
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

    private lateinit var settingPrefs: SharedPreferences

    // Firebase
    private lateinit var db: FirebaseFirestore

    // ETC
    private var model = "null"
    private var csc = "null"
    private var error = "null"

    // ArrayList that helps closing this activity safely
    private lateinit var list: IntArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = FirebaseFirestore.getInstance()
        error = getString(R.string.search_error)

        settingPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE)

        val total = intent!!.getIntExtra("total", 1)
        list = IntArray(total) { 0 }
        for (i in 0 until total) {
            model = CheckFirm.searchModel[i]
            csc = CheckFirm.searchCSC[i]

            SearchThread(total, i, model, csc).start()
        }

        val tempList = IntArray(total) { 1 }
        var isCloseable = false
        Thread {
            for (i in 0..9) {
                Thread.sleep(1000)
                if (tempList.contentEquals(list)) {
                    isCloseable = true
                    break
                }
            }

            if (isCloseable) {
                intent.putExtra("search_code", 2)
                setResult(Activity.RESULT_OK, intent)
                finish()
                overridePendingTransition(0, 0)
            } else {
                intent.putExtra("search_code", 3)
                setResult(Activity.RESULT_OK, intent)
                finish()
                overridePendingTransition(0, 0)
            }
        }.start()
    }

    inner class SearchThread(private val total: Int, private val current: Int, private val model: String, private val csc: String) : Thread() {
        private var latestOfficial = ""
        private var officialAndroidVersion = ""
        private lateinit var officialFirmware: ArrayList<String>

        private var latestTest = ""
        private var testAndroidVersion = ""
        private lateinit var testFirmware: ArrayList<String>

        override fun run() {
            val urlOfficial = "$baseURL$csc/$model$officialURL"
            val urlTest = "$baseURL$csc/$model$testURL"
            officialFirmware = ArrayList()
            testFirmware = ArrayList()

            try {
                val official = Jsoup.parse(URL(urlOfficial).openStream(), "UTF-8", "", Parser.xmlParser())
                for (el in official.select("latest")) {
                    officialAndroidVersion = el.attr("o")
                    latestOfficial = el.text()
                }
                for (el in official.select("value")) {
                    val firmwares = el.text()
                    officialFirmware.add(firmwares)
                }

                val test = Jsoup.parse(URL(urlTest).openStream(), "UTF-8", "", Parser.xmlParser())
                for (el in test.select("latest")) {
                    testAndroidVersion = el.attr("o")
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
            } finally {
                if (settingPrefs.getBoolean("alphabetical", true)) {
                    officialFirmware.sort()
                    officialFirmware.reverse()
                    testFirmware.sort()
                    testFirmware.reverse()
                }

                if (latestOfficial.isBlank()) {
                    CheckFirm.searchResult[current].officialLatestFirmware = error
                } else {
                    CheckFirm.searchResult[current].officialLatestFirmware = latestOfficial
                    if (officialAndroidVersion.isBlank()) {
                        CheckFirm.searchResult[current].officialAndroidVersion = getString(R.string.unknown)
                    } else {
                        CheckFirm.searchResult[current].officialAndroidVersion = officialAndroidVersion
                    }
                }

                if (officialFirmware.isEmpty()) {
                    CheckFirm.searchResult[current].officialPreviousFirmware = arrayListOf(error)
                } else {
                    CheckFirm.searchResult[current].officialPreviousFirmware = officialFirmware
                }

                if (latestTest.isBlank()) {
                    CheckFirm.searchResult[current].testLatestFirmware = error
                } else {
                    CheckFirm.searchResult[current].testLatestFirmware = latestTest
                    if (testAndroidVersion.isBlank()) {
                        CheckFirm.searchResult[current].testAndroidVersion = getString(R.string.unknown)
                    } else {
                        CheckFirm.searchResult[current].testAndroidVersion = testAndroidVersion
                    }
                }

                if (testFirmware.isEmpty()) {
                    CheckFirm.searchResult[current].testPreviousFirmware = arrayListOf(error)
                } else {
                    CheckFirm.searchResult[current].testPreviousFirmware = testFirmware
                }
                intent.putExtra("total", total - 1)

                if (settingPrefs.getBoolean("firebase", false)) {
                    val currentOfficial = Tools.getFirmwareInfo(latestOfficial)
                    val currentTest = Tools.getFirmwareInfo(latestTest)

                    if (currentTest[2] == '?') {
                        CheckFirm.searchResult[current].testType = "null"
                        CheckFirm.searchResult[current].testDowngrade = "null"
                    } else {
                        val compare = currentOfficial[2].compareTo(currentTest[2])
                        when {
                            compare < 0 -> {
                                CheckFirm.searchResult[current].testType = getString(R.string.smart_search_type_major)
                            }
                            compare > 0 -> {
                                CheckFirm.searchResult[current].testType = getString(R.string.smart_search_type_rollback)
                            }
                            else -> {
                                CheckFirm.searchResult[current].testType = getString(R.string.smart_search_type_minor)
                            }
                        }

                        if (currentOfficial.substring(0, 2) == currentTest.substring(0, 2)) {
                            CheckFirm.searchResult[current].testDowngrade = getString(R.string.smart_search_downgrade_possible)
                        } else {
                            CheckFirm.searchResult[current].testDowngrade = getString(R.string.smart_search_downgrade_impossible)
                        }
                    }
                    CheckFirm.searchResult[current].testDiscoveryDate = getString(R.string.unknown)
                    CheckFirm.searchResult[current].testDecrypted = "null"
                    CheckFirm.searchResult[current].testDiscoverer = "null"
                    CheckFirm.searchResult[current].testWatson = "null"
                    list[current] = 1
                } else {
                    if (latestOfficial.isBlank() && latestTest.isBlank()) {
                        CheckFirm.searchResult[current].testDiscoveryDate = getString(R.string.unknown)
                        CheckFirm.searchResult[current].testDecrypted = "null"
                        CheckFirm.searchResult[current].testType = "null"
                        CheckFirm.searchResult[current].testDowngrade = "null"
                        CheckFirm.searchResult[current].testDiscoverer = "null"
                        CheckFirm.searchResult[current].testWatson = "null"
                        list[current] = 1
                    } else if (latestOfficial.isNotBlank() && latestTest.isBlank()) {
                        CheckFirm.searchResult[current].testDiscoveryDate = getString(R.string.unknown)
                        CheckFirm.searchResult[current].testDecrypted = "null"
                        CheckFirm.searchResult[current].testType = "null"
                        CheckFirm.searchResult[current].testDowngrade = "null"
                        CheckFirm.searchResult[current].testDiscoverer = "null"
                        CheckFirm.searchResult[current].testWatson = "null"
                        list[current] = 1
                    } else if (latestOfficial.isBlank() && latestTest.isNotBlank()) {
                        smartSearch(1, current)
                    } else {
                        smartSearch(0, current)
                    }
                }
            }
        }
    }

    private fun smartSearch(check: Int, current: Int) {
        val latestOfficial = CheckFirm.searchResult[current].officialLatestFirmware
        val latestTest = CheckFirm.searchResult[current].testLatestFirmware
        val model = CheckFirm.searchModel[current]
        val csc = CheckFirm.searchCSC[current]

        val docRef = db.collection(model).document(csc)
        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val doc = task.result as DocumentSnapshot
                val firestoreDate = doc.get("date_latest").toString()
                val firestoreLatest = doc.get("firmware_latest").toString()
                val firestoreDecrypted = doc.get("firmware_decrypted").toString()
                val firestoreDiscoverer = doc.get("discoverer").toString()
                val firestoreWatson = doc.get("watson").toString()

                if (firestoreDate == "null") {
                    add(check, current)
                } else {
                    val currentOfficial = if (check == 1) {
                        "??????"
                    } else {
                        Tools.getFirmwareInfo(latestOfficial)
                    }

                    val currentTest = Tools.getFirmwareInfo(latestTest)

                    if (firestoreDiscoverer == "null") {
                        CheckFirm.searchResult[current].testDiscoverer = "Unknown"
                    } else {
                        CheckFirm.searchResult[current].testDiscoverer = firestoreDiscoverer
                    }
                    CheckFirm.searchResult[current].testDiscoveryDate = firestoreDate

                    if (Tools.getMD5Hash(firestoreDecrypted) == firestoreLatest) {
                        CheckFirm.searchResult[current].testDecrypted = firestoreDecrypted
                        CheckFirm.searchResult[current].testWatson = firestoreWatson
                    } else {
                        CheckFirm.searchResult[current].testDecrypted = "null"
                        CheckFirm.searchResult[current].testWatson = "null"
                    }

                    if (currentTest[2] == '?') {
                        CheckFirm.searchResult[current].testType = "null"
                        CheckFirm.searchResult[current].testDowngrade = "null"
                    } else {
                        val compare = currentOfficial[2].compareTo(currentTest[2])
                        when {
                            compare < 0 -> {
                                CheckFirm.searchResult[current].testType = getString(R.string.smart_search_type_major)
                            }
                            compare > 0 -> {
                                CheckFirm.searchResult[current].testType = getString(R.string.smart_search_type_rollback)
                            }
                            else -> {
                                CheckFirm.searchResult[current].testType = getString(R.string.smart_search_type_minor)
                            }
                        }

                        if (currentOfficial.substring(0, 2) == currentTest.substring(0, 2)) {
                            CheckFirm.searchResult[current].testDowngrade = getString(R.string.smart_search_downgrade_possible)
                        } else {
                            CheckFirm.searchResult[current].testDowngrade = getString(R.string.smart_search_downgrade_impossible)
                        }
                    }

                    if (firestoreLatest != latestTest) {
                        val newDate = Tools.dateToString(Tools.getCurrentDateTime())
                        val discoverer = settingPrefs.getString("profile_user_name", "Unknown")!!
                        CheckFirm.searchResult[current].testDiscoveryDate = newDate
                        CheckFirm.searchResult[current].testDiscoverer = discoverer
                        CheckFirm.searchResult[current].testWatson = "null"
                        CheckFirm.searchResult[current].testDecrypted = "null"
                        update(current)
                    }
                }
                list[current] = 1
            }
        }
    }

    private fun update(current: Int) {
        val model = CheckFirm.searchModel[current]
        val csc = CheckFirm.searchCSC[current]
        val latestTest = CheckFirm.searchResult[current].testLatestFirmware
        val date = Tools.dateToString(Tools.getCurrentDateTime())
        val discoverer = settingPrefs.getString("profile_user_name", "Unknown")!!
        val docRef = db.collection(model).document(csc)
        docRef.update("date_latest", date)
        docRef.update("firmware_latest", latestTest)
        docRef.update("discoverer", discoverer)
        docRef.update("watson", "null")
        docRef.update("firmware_decrypted", "null")

        val items = HashMap<String, Any>()
        items["model"] = model
        items["csc"] = csc
        db.collection("A_NOTIFICATION").document("UPDATE").set(items)
        list[current] = 1
    }

    private fun add(check: Int, current: Int) {
        val model = CheckFirm.searchModel[current]
        val csc = CheckFirm.searchCSC[current]
        val latestOfficial = CheckFirm.searchResult[current].officialLatestFirmware
        val latestTest = CheckFirm.searchResult[current].testLatestFirmware
        val date = Tools.dateToString(Tools.getCurrentDateTime())
        val discoverer = settingPrefs.getString("profile_user_name", "Unknown")!!
        val items = HashMap<String, Any>()
        items["date_latest"] = date
        items["firmware_latest"] = latestTest
        items["discoverer"] = discoverer
        items["firmware_decrypted"] = "null"
        items["watson"] = "null"

        CheckFirm.searchResult[current].testDiscoveryDate = date

        val currentOfficial = if (check == 1) {
            "??????"
        } else {
            Tools.getFirmwareInfo(latestOfficial)
        }

        val currentTest = Tools.getFirmwareInfo(latestTest)

        val condition = (currentTest == "??????")

        CheckFirm.searchResult[current].testDecrypted = "null"
        CheckFirm.searchResult[current].testWatson = "null"
        CheckFirm.searchResult[current].testDiscoverer = discoverer

        if (currentOfficial == currentTest) {
            if (condition) {
                CheckFirm.searchResult[current].testType = "null"
                CheckFirm.searchResult[current].testDowngrade = "null"
            } else {
                CheckFirm.searchResult[current].testType = getString(R.string.smart_search_type_minor)
                CheckFirm.searchResult[current].testDowngrade = getString(R.string.smart_search_downgrade_possible)
            }
        } else {
            if (condition) {
                CheckFirm.searchResult[current].testType = "null"
            } else {
                val compare = currentOfficial[2].compareTo(currentTest[2])
                when {
                    compare < 0 -> {
                        CheckFirm.searchResult[current].testType = getString(R.string.smart_search_type_major)
                    }
                    compare > 0 -> {
                        CheckFirm.searchResult[current].testType = getString(R.string.smart_search_type_rollback)
                    }
                    else -> {
                        CheckFirm.searchResult[current].testType = getString(R.string.smart_search_type_minor)
                    }
                }
            }

            if (currentOfficial.substring(0, 2) == currentTest.substring(0, 2)) {
                if (condition) {
                    CheckFirm.searchResult[current].testDowngrade = "null"
                } else {
                    CheckFirm.searchResult[current].testDowngrade = getString(R.string.smart_search_downgrade_possible)
                }
            } else {
                CheckFirm.searchResult[current].testDowngrade = getString(R.string.smart_search_downgrade_impossible)
            }
        }

        db.collection(model).document(csc).set(items)

        list[current] = 1
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, getString(R.string.searching), Toast.LENGTH_SHORT).show()
    }
}
