package com.illusion.checkfirm.data.source.remote

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.illusion.checkfirm.CheckFirm
import com.illusion.checkfirm.R
import com.illusion.checkfirm.data.model.FirmwareItem
import com.illusion.checkfirm.data.model.OfficialFirmwareItem
import com.illusion.checkfirm.data.model.TestFirmwareItem
import com.illusion.checkfirm.data.source.local.SettingsDataSource
import com.illusion.checkfirm.common.util.Tools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.first
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import java.net.URL

class FWFetcher(private val context: Context) {
    // Firebase
    private lateinit var db: FirebaseFirestore

    private lateinit var settingsDataSource: SettingsDataSource

    private var jobList = arrayListOf<Deferred<Unit>>()

    private var isFirebaseEnabled = false
    private var profileName = "Unknown"

    // ETC
    private val unknown = context.getString(R.string.unknown)

    suspend fun start(total: Int): Int {
        settingsDataSource = SettingsDataSource(context)
        db = FirebaseFirestore.getInstance()
        jobList.clear()

        for (i in 0..4) {
            CheckFirm.firmwareItems[i] = FirmwareItem(
                OfficialFirmwareItem(),
                TestFirmwareItem()
            )
        }

        isFirebaseEnabled = settingsDataSource.isFirebaseEnabled.first()
        profileName = settingsDataSource.getProfileName.first()

        val job = CoroutineScope(Dispatchers.IO).async {
            for (i in 0 until total) {
                jobList.add(async { search(i) })
            }
            jobList.awaitAll()
        }
        job.await()

        return 0
    }

    fun search(current: Int) {
        fetchOfficialFirmwareInfo(current)
        fetchOfficialFirmwareNotifyDoc(current)
        fetchTestFirmwareInfo(current)

        if (isFirebaseEnabled) {
            smartSearch(current)
        } else {
            val officialLatest = CheckFirm.firmwareItems[current].officialFirmwareItem.latestFirmware
            val testLatest = CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware
            val testPrevious = CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware

            val currentOfficial = Tools.getBuildInfo(officialLatest)
            val currentTest: String

            if (testLatest.isEmpty() && testPrevious.isNotEmpty()) {
                if (Character.isUpperCase(testPrevious.firstKey()[0])) {
                    CheckFirm.firmwareItems[current].testFirmwareItem.clue =
                        testPrevious.firstKey()
                    currentTest = testPrevious.firstKey()
                } else {
                    CheckFirm.firmwareItems[current].testFirmwareItem.clue = officialLatest
                    currentTest = Tools.getBuildInfo(officialLatest)
                }
            } else {
                currentTest = Tools.getBuildInfo(testLatest)
            }

            if (currentTest.isNotBlank()) {
                val compare = currentOfficial[2].compareTo(currentTest[2])
                when {
                    compare < 0 -> {
                        CheckFirm.firmwareItems[current].testFirmwareItem.updateType =
                            context.getString(R.string.smart_search_type_major)
                        getNextAndroidVersion(CheckFirm.firmwareItems[current].officialFirmwareItem.androidVersion)
                    }

                    compare > 0 -> {
                        CheckFirm.firmwareItems[current].testFirmwareItem.updateType =
                            context.getString(R.string.smart_search_type_rollback)
                    }

                    else -> {
                        CheckFirm.firmwareItems[current].testFirmwareItem.updateType =
                            context.getString(R.string.smart_search_type_minor)
                        CheckFirm.firmwareItems[current].testFirmwareItem.androidVersion =
                            CheckFirm.firmwareItems[current].officialFirmwareItem.androidVersion
                    }
                }

                CheckFirm.firmwareItems[current].testFirmwareItem.isDowngradable =
                    currentOfficial.substring(0, 2) == currentTest.substring(0, 2)
            }
            CheckFirm.firmwareItems[current].testFirmwareItem.discoveryDate = unknown
        }
    }

    private fun fetchOfficialFirmwareInfo(current: Int) {
        var officialLatest = ""
        val officialPrevious = HashMap<String, String>()

        val fetchUrl =
            "https://fota-cloud-dn.ospserver.net/firmware/${CheckFirm.searchCSC[current]}/${CheckFirm.searchModel[current]}/version.xml"

        try {
            val official =
                Jsoup.parse(
                    URL(fetchUrl).openStream(),
                    "UTF-8",
                    "",
                    Parser.xmlParser()
                )

            for (el in official.select("latest")) {
                officialLatest = el.text()
            }

            for (el in official.select("value")) {
                val firmwares = el.text()
                officialPrevious[firmwares] = firmwares
            }

            CheckFirm.firmwareItems[current].officialFirmwareItem.latestFirmware = officialLatest

            if (officialPrevious.isEmpty()) {
                CheckFirm.firmwareItems[current].officialFirmwareItem.previousFirmware =
                    emptyMap<String, String>().toSortedMap()
            } else if (officialPrevious.size == 1) {
                if (officialPrevious.containsKey("")) {
                    CheckFirm.firmwareItems[current].officialFirmwareItem.previousFirmware =
                        emptyMap<String, String>().toSortedMap()
                } else {
                    CheckFirm.firmwareItems[current].officialFirmwareItem.previousFirmware =
                        officialPrevious.toSortedMap()
                }
            } else {
                CheckFirm.firmwareItems[current].officialFirmwareItem.previousFirmware =
                    officialPrevious.toSortedMap(reverseOrder())
            }
        } catch (_: Exception) {
            CheckFirm.firmwareItems[current].officialFirmwareItem.latestFirmware = ""
            CheckFirm.firmwareItems[current].officialFirmwareItem.previousFirmware =
                emptyMap<String, String>().toSortedMap()
        }
    }

    private fun fetchOfficialFirmwareNotifyDoc(current: Int) {
        var officialAndroidVersion: String
        var deviceName: String
        var officialReleaseDate: String

        val fetchUrl = "https://doc.samsungmobile.com/${CheckFirm.searchModel[current]}/${CheckFirm.searchCSC[current]}/doc.html"

        // Official NotifyDoc
        try {
            val entireNotifyDoc = Jsoup.connect(fetchUrl).get().body()
            val rawNotifyDoc = entireNotifyDoc.select("input#dflt_page").attr("value")
            val notifyDoc =
                Jsoup.connect("https://doc.samsungmobile.com" + rawNotifyDoc.substring(5)).get()
                    .body()
            deviceName = notifyDoc.select("div > div:eq(2) > h1 > b").text()
            officialReleaseDate =
                notifyDoc.select("div > div:eq(4) > div:eq(2)").text()

            officialAndroidVersion = notifyDoc.select("div > div:eq(4) > div:eq(1)").text()

            deviceName.indexOf("(").let {
                if (it == -1) {
                    deviceName
                } else {
                    deviceName = deviceName.substring(0, it)
                }
            }

            officialReleaseDate.indexOf(":").let {
                officialReleaseDate = officialReleaseDate.substring(it + 2)
            }

            officialAndroidVersion.indexOf(":").let {
                officialAndroidVersion = officialAndroidVersion.substring(it + 2)
            }

            CheckFirm.firmwareItems[current].officialFirmwareItem.deviceName = deviceName
            CheckFirm.firmwareItems[current].officialFirmwareItem.releaseDate = officialReleaseDate
            CheckFirm.firmwareItems[current].officialFirmwareItem.androidVersion = getOfficialAndroidVersion(officialAndroidVersion)
        } catch (_: Exception) {
            CheckFirm.firmwareItems[current].officialFirmwareItem.deviceName = unknown
            CheckFirm.firmwareItems[current].officialFirmwareItem.releaseDate = unknown
            CheckFirm.firmwareItems[current].officialFirmwareItem.androidVersion = unknown
        }
    }

    private fun fetchTestFirmwareInfo(current: Int) {
        var testLatest = ""
        var testAndroidVersion = ""
        val testPrevious = HashMap<String, String>()
        val testBeta = HashMap<String, String>()

        val fetchUrl =
            "https://fota-cloud-dn.ospserver.net/firmware/${CheckFirm.searchCSC[current]}/${CheckFirm.searchModel[current]}/version.test.xml"

        // Test Firmware
        try {
            val test = Jsoup.parse(
                URL(fetchUrl).openStream(),
                "UTF-8",
                "",
                Parser.xmlParser()
            )
            for (el in test.select("latest")) {
                testAndroidVersion = el.attr("o")
                testLatest = el.text()
            }
            for (el in test.select("value")) {
                val firmwares = el.text()

                if (firmwares.isNotBlank()) {
                    if (Character.isUpperCase(firmwares[0])) {
                        if (Tools.isBetaFirmware(firmwares)) {
                            testBeta[firmwares] = firmwares
                        } else {
                            testPrevious[firmwares] = firmwares
                        }
                    } else {
                        testPrevious[firmwares] = firmwares
                    }
                }
            }

            CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware = testLatest

            if (testAndroidVersion.isBlank()) {
                CheckFirm.firmwareItems[current].testFirmwareItem.androidVersion = unknown
            } else {
                CheckFirm.firmwareItems[current].testFirmwareItem.androidVersion = testAndroidVersion
            }

            if (testPrevious.isEmpty()) {
                CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware =
                    emptyMap<String, String>().toSortedMap()
            } else if (testPrevious.size == 1) {
                if (testPrevious.containsKey("")) {
                    CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware =
                        emptyMap<String, String>().toSortedMap()
                } else {
                    CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware =
                        testPrevious.toSortedMap()
                }
            } else {
                CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware =
                    testPrevious.toSortedMap(reverseOrder())
            }

            CheckFirm.firmwareItems[current].testFirmwareItem.betaFirmware =
                testBeta.toSortedMap(reverseOrder())
        } catch (_: Exception) {
            CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware = ""
            CheckFirm.firmwareItems[current].testFirmwareItem.androidVersion = ""
            CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware =
                emptyMap<String, String>().toSortedMap()
            CheckFirm.firmwareItems[current].testFirmwareItem.betaFirmware =
                emptyMap<String, String>().toSortedMap()
        }
    }

    fun stopAllJobs() {
        for (job in jobList) {
            job.cancel()
        }
        jobList.clear()
    }

    private fun smartSearch(current: Int) {
        val docRef =
            db.collection(CheckFirm.searchModel[current]).document(CheckFirm.searchCSC[current])
        val task: Task<DocumentSnapshot> = docRef.get()
        val snap: DocumentSnapshot = Tasks.await(task)
        if (task.isSuccessful) {
            val firestoreDate = snap.get("date_latest").toString().replace("/", "-")
            val firestoreLatest = snap.get("firmware_latest").toString()
            val firestoreDecrypted = snap.get("firmware_decrypted").toString()
            val firestoreDiscoverer = snap.get("discoverer").toString()
            val firestoreWatson = snap.get("watson").toString()
            val firestoreCount = snap.get("count").toString()
            val firestoreClue = snap.get("clue").toString()

            if (firestoreDate == "null") {
                add(current)
            } else {
                if (firestoreDiscoverer == "null") {
                    CheckFirm.firmwareItems[current].testFirmwareItem.discoverer = "Unknown"
                } else {
                    CheckFirm.firmwareItems[current].testFirmwareItem.discoverer = firestoreDiscoverer
                }
                CheckFirm.firmwareItems[current].testFirmwareItem.discoveryDate = firestoreDate

                if (firestoreCount == "null") {
                    docRef.update(
                        "count",
                        CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware.size
                    )
                } else {
                    if (firestoreCount.toInt() != CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware.size) {
                        val newDate = Tools.dateToString(Tools.getCurrentDateTime())

                        docRef.update("date_latest", newDate)
                        docRef.update("discoverer", profileName)
                        docRef.update("firmware_decrypted", "null")
                        docRef.update(
                            "count",
                            CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware.size
                        )
                        updateNotification(
                            CheckFirm.searchModel[current],
                            CheckFirm.searchCSC[current]
                        )

                        CheckFirm.firmwareItems[current].testFirmwareItem.discoveryDate = newDate
                        CheckFirm.firmwareItems[current].testFirmwareItem.discoverer = profileName
                    }
                }

                if (CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware.isBlank()) {
                    if (CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware.isEmpty()) {
                        getFirmwareType(
                            current,
                            CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware
                        )
                        getDowngradeInfo(
                            current,
                            CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware
                        )
                    } else {
                        if (Character.isUpperCase(CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware.firstKey()[0])) {
                            CheckFirm.firmwareItems[current].testFirmwareItem.clue =
                                CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware.firstKey()
                        } else {
                            if (firestoreClue == "null") {
                                docRef.update(
                                    "clue",
                                    CheckFirm.firmwareItems[current].officialFirmwareItem.latestFirmware
                                )
                                CheckFirm.firmwareItems[current].testFirmwareItem.clue =
                                    CheckFirm.firmwareItems[current].officialFirmwareItem.latestFirmware
                                CheckFirm.firmwareItems[current].testFirmwareItem.watson = profileName
                            } else {
                                CheckFirm.firmwareItems[current].testFirmwareItem.clue = firestoreClue
                                CheckFirm.firmwareItems[current].testFirmwareItem.watson = firestoreWatson
                            }
                        }
                        getFirmwareType(current, CheckFirm.firmwareItems[current].testFirmwareItem.clue)
                        getDowngradeInfo(current, CheckFirm.firmwareItems[current].testFirmwareItem.clue)
                    }
                } else {
                    if (CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware.contains("/")) {
                        getFirmwareType(
                            current,
                            CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware
                        )
                        getDowngradeInfo(
                            current,
                            CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware
                        )
                    } else {
                        if (firestoreDecrypted != "null") {
                            if (Tools.getMD5Hash(firestoreDecrypted) == firestoreLatest) {
                                CheckFirm.firmwareItems[current].testFirmwareItem.decryptedFirmware =
                                    firestoreDecrypted
                                CheckFirm.firmwareItems[current].testFirmwareItem.watson = firestoreWatson
                                getFirmwareType(current, firestoreDecrypted)
                                getDowngradeInfo(current, firestoreDecrypted)
                            } else {
                                getFirmwareType(
                                    current,
                                    CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware
                                )
                                getDowngradeInfo(
                                    current,
                                    CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware
                                )
                            }
                        } else {
                            getFirmwareType(
                                current,
                                CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware
                            )
                            getDowngradeInfo(
                                current,
                                CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware
                            )
                        }
                    }

                    if (firestoreLatest != CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware) {
                        val newDate = Tools.dateToString(Tools.getCurrentDateTime())

                        docRef.update("date_latest", newDate)
                        docRef.update(
                            "firmware_latest",
                            CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware
                        )
                        docRef.update("discoverer", profileName)
                        docRef.update("watson", "null")
                        docRef.update("firmware_decrypted", "null")
                        docRef.update(
                            "count",
                            CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware.size
                        )
                        updateNotification(
                            CheckFirm.searchModel[current],
                            CheckFirm.searchCSC[current]
                        )

                        CheckFirm.firmwareItems[current].testFirmwareItem.discoveryDate = newDate
                        CheckFirm.firmwareItems[current].testFirmwareItem.discoverer = profileName
                        CheckFirm.firmwareItems[current].testFirmwareItem.watson = ""
                        CheckFirm.firmwareItems[current].testFirmwareItem.decryptedFirmware = ""
                    }
                }
            }
        }
    }

    private fun updateNotification(model: String, csc: String) {
        val items = HashMap<String, Any>()
        items["model"] = model
        items["csc"] = csc
        db.collection("A_NOTIFICATION").document("UPDATE").set(items)
    }

    private fun getFirmwareType(current: Int, testFirmware: String) {
        val currentFirmware = Tools.getBuildInfo(CheckFirm.firmwareItems[current].officialFirmwareItem.latestFirmware)
        val nextFirmware = Tools.getBuildInfo(testFirmware)

        if (currentFirmware.isEmpty() || nextFirmware.isEmpty()) {
            return
        }

        val compare =
            Tools.getBuildInfo(CheckFirm.firmwareItems[current].officialFirmwareItem.latestFirmware)[2].compareTo(
                Tools.getBuildInfo(testFirmware)[2]
            )
        when {
            compare < 0 -> {
                CheckFirm.firmwareItems[current].testFirmwareItem.updateType =
                    context.getString(R.string.smart_search_type_major)
            }

            compare > 0 -> {
                CheckFirm.firmwareItems[current].testFirmwareItem.updateType =
                    context.getString(R.string.smart_search_type_rollback)
            }

            else -> {
                CheckFirm.firmwareItems[current].testFirmwareItem.updateType =
                    context.getString(R.string.smart_search_type_minor)
                if (CheckFirm.firmwareItems[current].testFirmwareItem.androidVersion == unknown) {
                    CheckFirm.firmwareItems[current].testFirmwareItem.androidVersion =
                        CheckFirm.firmwareItems[current].officialFirmwareItem.androidVersion
                }
            }
        }
    }

    private fun getDowngradeInfo(current: Int, testFirmware: String) {
        val officialFirmwareBootloader = Tools.getBuildInfo(CheckFirm.firmwareItems[current].officialFirmwareItem.latestFirmware)
        val testFirmwareBootloader = Tools.getBuildInfo(testFirmware)

        if (officialFirmwareBootloader.isEmpty() || testFirmwareBootloader.isEmpty()) {
            return
        }

        CheckFirm.firmwareItems[current].testFirmwareItem.isDowngradable =
            officialFirmwareBootloader.substring(0, 2) == testFirmwareBootloader.substring(0, 2)
    }

    private fun getOfficialAndroidVersion(rawString: String): String {
        // CASE: {"Marshmallow(Android 6.0.1)", "SOMETHING(Android 7.0)", "Pie(Android 9)", "U(Android 14)", "RTOS 1.0"}
        val parenthesesIndex = rawString.indexOf("(")

        return if (parenthesesIndex == -1) {
            return rawString
        } else {
            try {
                rawString.substring(parenthesesIndex + 9, rawString.length - 1)
            } catch (_: Exception) {
                unknown
            }
        }
    }

    private fun getNextAndroidVersion(androidVersion: String): String {
        return if (androidVersion.isBlank()) {
            ""
        } else if (androidVersion.contains('.')) {
            "${Character.getNumericValue(androidVersion[0]) + 1}"
        } else {
            "${androidVersion.toInt() + 1}"
        }
    }

    private fun add(current: Int) {
        val date = Tools.dateToString(Tools.getCurrentDateTime())
        val items = HashMap<String, Any>()
        items["date_latest"] = date
        items["firmware_latest"] = CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware
        items["discoverer"] = profileName
        items["firmware_decrypted"] = "null"
        items["watson"] = "null"
        items["count"] = CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware.size
        db.collection(CheckFirm.searchModel[current]).document(CheckFirm.searchCSC[current])
            .set(items)

        CheckFirm.firmwareItems[current].testFirmwareItem.discoveryDate = date
        CheckFirm.firmwareItems[current].testFirmwareItem.discoverer = profileName

        if (CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware.isBlank()) {
            if (CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware.isEmpty()) {
                getFirmwareType(current, CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware)
                getDowngradeInfo(current, CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware)
            } else {
                if (Character.isUpperCase(CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware.firstKey()[0])) {
                    CheckFirm.firmwareItems[current].testFirmwareItem.clue =
                        CheckFirm.firmwareItems[current].testFirmwareItem.previousFirmware.firstKey()
                }
                getFirmwareType(current, CheckFirm.firmwareItems[current].testFirmwareItem.clue)
                getDowngradeInfo(current, CheckFirm.firmwareItems[current].testFirmwareItem.clue)
            }
        } else {
            getFirmwareType(current, CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware)
            getDowngradeInfo(current, CheckFirm.firmwareItems[current].testFirmwareItem.latestFirmware)
        }
    }
}
