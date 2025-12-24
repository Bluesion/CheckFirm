package com.illusion.checkfirm.data.source.remote

import com.fleeksoft.ksoup.Ksoup
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.illusion.checkfirm.common.util.Tools
import com.illusion.checkfirm.data.model.local.DeviceItem
import com.illusion.checkfirm.data.model.local.FirmwareItem
import com.illusion.checkfirm.data.model.local.OfficialFirmwareItem
import com.illusion.checkfirm.data.model.local.SearchResultItem
import com.illusion.checkfirm.data.model.local.TestFirmwareItem
import com.illusion.checkfirm.data.model.local.UpdateType
import com.illusion.checkfirm.data.model.remote.ApiException
import com.illusion.checkfirm.data.model.remote.FirmwareVersionInfo
import com.illusion.checkfirm.data.util.FirmwareXmlConverter
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import nl.adaptivity.xmlutil.serialization.XML
import java.net.UnknownHostException

class FirmwareFetcher(
    private val db: FirebaseFirestore
) {

    private val commonClient = HttpClient(Android) {
        /*
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    CheckFirmLogger.d(message)
                }
            }
            level = LogLevel.ALL
        }
        */

        expectSuccess = false
        HttpResponseValidator {
            validateResponse { response ->
                if (!response.status.isSuccess()) {
                    throw Exception("HTTP error: ${response.status.value}")
                }
            }
            handleResponseExceptionWithRequest { exception, request ->
                val clientException =
                    exception as? ApiException ?: return@handleResponseExceptionWithRequest
                throw clientException
            }
        }
    }

    private val xmlClient = commonClient.config {
        install(ContentNegotiation) {
            register(ContentType.Application.Xml, FirmwareXmlConverter(XML()))
        }
    }

    suspend fun search(
        device: DeviceItem,
        isFirebaseEnabled: Boolean,
        profileName: String
    ): SearchResultItem = withContext(Dispatchers.IO) {
        val firmwareItem = fetchFirmwareInfo(device)

        if (isFirebaseEnabled) {
            smartSearch(device, firmwareItem, profileName)
        } else {
            defaultSearch(firmwareItem)
        }

        return@withContext SearchResultItem(device, firmwareItem)
    }

    private suspend fun fetchFirmwareInfo(device: DeviceItem): FirmwareItem {
        val tempOfficialFirmwareInfo = fetchOfficialFirmwareInfo(device)
        val tempUrl = fetchOfficialFirmwareNotifyDoc(device)

        return FirmwareItem(
            officialFirmwareItem = fetchOfficialFirmwareNotifyDoc(
                tempUrl,
                tempOfficialFirmwareInfo
            ),
            testFirmwareItem = fetchTestFirmwareInfo(device)
        )
    }

    private suspend fun fetchOfficialFirmwareInfo(
        device: DeviceItem
    ): OfficialFirmwareItem {
        val officialFirmwareItem = OfficialFirmwareItem()
        val tempHashMap = HashMap<String, String>()

        try {
            val response =
                xmlClient.get {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = OSP_PREFIX
                        encodedPathSegments = listOf(device.csc, device.model, OSP_SUFFIX_OFFICIAL)
                    }
                }

            val fetchResult = response.body<FirmwareVersionInfo>()

            officialFirmwareItem.latestFirmware = fetchResult.firmware.version.latest.firmware
            officialFirmwareItem.androidVersion = fetchResult.firmware.version.latest.androidVersion
            fetchResult.firmware.version.previous.list.forEach {
                tempHashMap[it.firmware] = it.firmware
            }

            officialFirmwareItem.previousFirmware =
                tempHashMap.toSortedMap(reverseOrder())

            return officialFirmwareItem
        } catch (_: UnknownHostException) {
            return officialFirmwareItem
        } catch (_: ApiException) {
            return officialFirmwareItem
        } catch (_: Exception) {
            return officialFirmwareItem
        }
    }

    private suspend fun fetchOfficialFirmwareNotifyDoc(
        device: DeviceItem
    ): String {
        try {
            val response =
                commonClient.get {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = NOTIFY_DOC_PREFIX
                        encodedPathSegments = listOf(device.model, device.csc, NOTIFY_DOC_SUFFIX)
                    }
                }

            val fetchResult = response.body<String>()
            val doc = Ksoup.parse(html = fetchResult)

            return doc.select("input#dflt_page").attr("value").substring(6)
        } catch (_: UnknownHostException) {
            return ""
        } catch (_: ApiException) {
            return ""
        } catch (_: Exception) {
            return ""
        }
    }

    // example url = SM-A720S/000065170818/kor.html
    private suspend fun fetchOfficialFirmwareNotifyDoc(
        url: String,
        officialFirmwareItem: OfficialFirmwareItem
    ): OfficialFirmwareItem {
        var officialAndroidVersion: String
        var deviceName: String
        var officialReleaseDate: String

        try {
            val response =
                commonClient.get {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = NOTIFY_DOC_PREFIX
                        encodedPath = url
                    }
                }

            val fetchResult = response.body<String>()
            val notifyDoc = Ksoup.parse(html = fetchResult)

            deviceName = notifyDoc.select("div > div:eq(2) > h1 > b").text()
            deviceName.indexOf("(").let {
                if (it == -1) {
                    deviceName
                } else {
                    deviceName = deviceName.substring(0, it)
                }
            }
            officialFirmwareItem.deviceName = deviceName

            var latestVersionFromDoc = notifyDoc.select("div > div:eq(4) > div:eq(0)").text()
            latestVersionFromDoc.indexOf(":").let {
                latestVersionFromDoc = latestVersionFromDoc.substring(it + 2)
            }

            // Notify doc에는 최신 빌드 버전이 늦게 반영되는 경우가 있음
            // 최신 빌드 버전이 늦게 반영되는 경우, 릴리즈 날짜는 공백으로, 안드로이드 버전은 XML에서 가져온 버전을 그대로 사용한다.
            if (Tools.getShortBuildInfo(officialFirmwareItem.latestFirmware)
                == Tools.getShortBuildInfo(latestVersionFromDoc)) {

                officialReleaseDate =
                    notifyDoc.select("div > div:eq(4) > div:eq(2)").text()
                officialReleaseDate.indexOf(":").let {
                    officialReleaseDate = officialReleaseDate.substring(it + 2)
                }
                officialFirmwareItem.releaseDate = officialReleaseDate

                officialAndroidVersion = notifyDoc.select("div > div:eq(4) > div:eq(1)").text()
                officialAndroidVersion.indexOf(":").let {
                    officialAndroidVersion = officialAndroidVersion.substring(it + 2)
                }
                officialFirmwareItem.androidVersion = getOfficialAndroidVersion(officialAndroidVersion)
            }
            return officialFirmwareItem
        } catch (_: UnknownHostException) {
            return officialFirmwareItem
        } catch (_: ApiException) {
            return officialFirmwareItem
        } catch (_: Exception) {
            return officialFirmwareItem
        }
    }

    private suspend fun fetchTestFirmwareInfo(
        device: DeviceItem
    ): TestFirmwareItem {
        val testFirmwareItem = TestFirmwareItem()
        val previousHashMap = HashMap<String, String>()
        val betaHashMap = HashMap<String, String>()

        try {
            val response =
                xmlClient.get {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = OSP_PREFIX
                        encodedPathSegments = listOf(device.csc, device.model, OSP_SUFFIX_TEST)
                    }
                }

            val fetchResult = response.body<FirmwareVersionInfo>()

            testFirmwareItem.latestFirmware = fetchResult.firmware.version.latest.firmware
            testFirmwareItem.androidVersion = fetchResult.firmware.version.latest.androidVersion

            fetchResult.firmware.version.previous.list.forEach {
                if (Character.isUpperCase(it.firmware[0])) {
                    if (Tools.isBetaFirmware(it.firmware)) {
                        betaHashMap[it.firmware] = it.firmware
                    } else {
                        previousHashMap[it.firmware] = it.firmware
                    }
                } else {
                    previousHashMap[it.firmware] = it.firmware
                }
            }

            testFirmwareItem.previousFirmware =
                previousHashMap.toSortedMap(reverseOrder())

            testFirmwareItem.betaFirmware =
                betaHashMap.toSortedMap(reverseOrder())

            return testFirmwareItem
        } catch (_: UnknownHostException) {
            return testFirmwareItem
        } catch (_: ApiException) {
            return testFirmwareItem
        } catch (_: Exception) {
            return testFirmwareItem
        }
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
                ""
            }
        }
    }

    private fun smartSearch(
        device: DeviceItem,
        firmwareItem: FirmwareItem,
        profileName: String
    ) {
        val docRef =
            db.collection(device.model).document(device.csc)
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

            // If there is no document, add new document and return
            // This is for new found model
            if (firestoreDate == "null") {
                add(docRef, firmwareItem, profileName)
                return
            }

            if (firestoreDiscoverer == "null") {
                firmwareItem.testFirmwareItem.discoverer = "Unknown"
            } else {
                firmwareItem.testFirmwareItem.discoverer = firestoreDiscoverer
            }
            firmwareItem.testFirmwareItem.discoveryDate = firestoreDate

            if (firestoreCount == "null") {
                docRef.update(
                    "count",
                    firmwareItem.testFirmwareItem.previousFirmware.size
                )
            } else {
                if (firestoreCount.toInt() != firmwareItem.testFirmwareItem.previousFirmware.size) {
                    val newDate = Tools.dateToString(Tools.getCurrentDateTime())

                    docRef.update("date_latest", newDate)
                    docRef.update("discoverer", profileName)
                    docRef.update("firmware_decrypted", "null")
                    docRef.update(
                        "count",
                        firmwareItem.testFirmwareItem.previousFirmware.size
                    )
                    updateNotification(device)

                    firmwareItem.testFirmwareItem.discoveryDate = newDate
                    firmwareItem.testFirmwareItem.discoverer = profileName
                }
            }

            if (firmwareItem.testFirmwareItem.latestFirmware.isBlank()) {
                if (firmwareItem.testFirmwareItem.previousFirmware.isEmpty()) {
                    getFirmwareType(
                        firmwareItem,
                        firmwareItem.testFirmwareItem.latestFirmware
                    )
                    getDowngradeInfo(
                        firmwareItem,
                        firmwareItem.testFirmwareItem.latestFirmware
                    )
                } else {
                    if (Character.isUpperCase(firmwareItem.testFirmwareItem.previousFirmware.firstKey()[0])) {
                        firmwareItem.testFirmwareItem.clue =
                            firmwareItem.testFirmwareItem.previousFirmware.firstKey()
                    } else {
                        if (firestoreClue == "null") {
                            docRef.update(
                                "clue",
                                firmwareItem.officialFirmwareItem.latestFirmware
                            )
                            firmwareItem.testFirmwareItem.clue =
                                firmwareItem.officialFirmwareItem.latestFirmware
                            firmwareItem.testFirmwareItem.watson = profileName
                        } else {
                            firmwareItem.testFirmwareItem.clue = firestoreClue
                            firmwareItem.testFirmwareItem.watson = firestoreWatson
                        }
                    }
                    getFirmwareType(firmwareItem, firmwareItem.testFirmwareItem.clue)
                    getDowngradeInfo(firmwareItem, firmwareItem.testFirmwareItem.clue)
                }
            } else {
                if (firmwareItem.testFirmwareItem.latestFirmware.contains("/")) {
                    getFirmwareType(
                        firmwareItem,
                        firmwareItem.testFirmwareItem.latestFirmware
                    )
                    getDowngradeInfo(
                        firmwareItem,
                        firmwareItem.testFirmwareItem.latestFirmware
                    )
                } else {
                    if (firestoreDecrypted != "null") {
                        if (Tools.getMD5Hash(firestoreDecrypted) == firestoreLatest) {
                            firmwareItem.testFirmwareItem.decryptedFirmware =
                                firestoreDecrypted
                            firmwareItem.testFirmwareItem.watson = firestoreWatson
                            getFirmwareType(firmwareItem, firestoreDecrypted)
                            getDowngradeInfo(firmwareItem, firestoreDecrypted)
                        } else {
                            getFirmwareType(
                                firmwareItem,
                                firmwareItem.testFirmwareItem.latestFirmware
                            )
                            getDowngradeInfo(
                                firmwareItem,
                                firmwareItem.testFirmwareItem.latestFirmware
                            )
                        }
                    } else {
                        getFirmwareType(
                            firmwareItem,
                            firmwareItem.testFirmwareItem.latestFirmware
                        )
                        getDowngradeInfo(
                            firmwareItem,
                            firmwareItem.testFirmwareItem.latestFirmware
                        )
                    }
                }

                if (firestoreLatest != firmwareItem.testFirmwareItem.latestFirmware) {
                    val newDate = Tools.dateToString(Tools.getCurrentDateTime())

                    docRef.update("date_latest", newDate)
                    docRef.update(
                        "firmware_latest",
                        firmwareItem.testFirmwareItem.latestFirmware
                    )
                    docRef.update("discoverer", profileName)
                    docRef.update("watson", "null")
                    docRef.update("firmware_decrypted", "null")
                    docRef.update(
                        "count",
                        firmwareItem.testFirmwareItem.previousFirmware.size
                    )
                    updateNotification(device)

                    firmwareItem.testFirmwareItem.discoveryDate = newDate
                    firmwareItem.testFirmwareItem.discoverer = profileName
                    firmwareItem.testFirmwareItem.watson = ""
                    firmwareItem.testFirmwareItem.decryptedFirmware = ""
                }
            }
        }
    }

    private fun updateNotification(device: DeviceItem) {
        val items = HashMap<String, Any>()
        items["model"] = device.model
        items["csc"] = device.csc
        db.collection("A_NOTIFICATION").document("UPDATE").set(items)
    }

    private fun defaultSearch(firmwareItem: FirmwareItem) {
        val officialLatest = firmwareItem.officialFirmwareItem.latestFirmware
        val testLatest = firmwareItem.testFirmwareItem.latestFirmware
        val testPrevious = firmwareItem.testFirmwareItem.previousFirmware

        val currentOfficial = Tools.getBuildInfo(officialLatest)
        val currentTest: String

        if (testLatest.isEmpty() && testPrevious.isNotEmpty()) {
            if (Character.isUpperCase(testPrevious.firstKey()[0])) {
                firmwareItem.testFirmwareItem.clue =
                    testPrevious.firstKey()
                currentTest = testPrevious.firstKey()
            } else {
                firmwareItem.testFirmwareItem.clue = officialLatest
                currentTest = Tools.getBuildInfo(officialLatest)
            }
        } else {
            currentTest = Tools.getBuildInfo(testLatest)
        }

        if (currentOfficial.isNotEmpty() && currentTest.isNotBlank()) {
            val compare = currentOfficial[2].compareTo(currentTest[2])
            when {
                compare < 0 -> {
                    firmwareItem.testFirmwareItem.updateType =
                        UpdateType.MAJOR
                    getNextAndroidVersion(firmwareItem.officialFirmwareItem.androidVersion)
                }

                compare > 0 -> {
                    firmwareItem.testFirmwareItem.updateType =
                        UpdateType.ROLLBACK
                }

                else -> {
                    firmwareItem.testFirmwareItem.updateType =
                        UpdateType.MINOR
                    firmwareItem.testFirmwareItem.androidVersion =
                        firmwareItem.officialFirmwareItem.androidVersion
                }
            }

            firmwareItem.testFirmwareItem.isDowngradable =
                currentOfficial.substring(0, 2) == currentTest.substring(0, 2)
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

    private fun add(docRef: DocumentReference, firmwareItem: FirmwareItem, profileName: String) {
        val date = Tools.dateToString(Tools.getCurrentDateTime())
        val items = HashMap<String, Any>()
        items["date_latest"] = date
        items["firmware_latest"] = firmwareItem.testFirmwareItem.latestFirmware
        items["discoverer"] = profileName
        items["firmware_decrypted"] = "null"
        items["watson"] = "null"
        items["count"] = firmwareItem.testFirmwareItem.previousFirmware.size
        docRef.set(items)

        firmwareItem.testFirmwareItem.discoveryDate = date
        firmwareItem.testFirmwareItem.discoverer = profileName

        if (firmwareItem.testFirmwareItem.latestFirmware.isBlank()) {
            if (firmwareItem.testFirmwareItem.previousFirmware.isEmpty()) {
                getFirmwareType(firmwareItem, firmwareItem.testFirmwareItem.latestFirmware)
                getDowngradeInfo(firmwareItem, firmwareItem.testFirmwareItem.latestFirmware)
            } else {
                if (Character.isUpperCase(firmwareItem.testFirmwareItem.previousFirmware.firstKey()[0])) {
                    firmwareItem.testFirmwareItem.clue =
                        firmwareItem.testFirmwareItem.previousFirmware.firstKey()
                }
                getFirmwareType(firmwareItem, firmwareItem.testFirmwareItem.clue)
                getDowngradeInfo(firmwareItem, firmwareItem.testFirmwareItem.clue)
            }
        } else {
            getFirmwareType(firmwareItem, firmwareItem.testFirmwareItem.latestFirmware)
            getDowngradeInfo(firmwareItem, firmwareItem.testFirmwareItem.latestFirmware)
        }
    }

    private fun getFirmwareType(firmwareItem: FirmwareItem, testFirmware: String) {
        val currentFirmware = Tools.getBuildInfo(firmwareItem.officialFirmwareItem.latestFirmware)
        val nextFirmware = Tools.getBuildInfo(testFirmware)

        if (currentFirmware.isEmpty() || nextFirmware.isEmpty()) {
            return
        }

        val compare =
            Tools.getBuildInfo(firmwareItem.officialFirmwareItem.latestFirmware)[2].compareTo(
                Tools.getBuildInfo(testFirmware)[2]
            )
        when {
            compare < 0 -> {
                firmwareItem.testFirmwareItem.updateType = UpdateType.MAJOR
            }

            compare > 0 -> {
                firmwareItem.testFirmwareItem.updateType = UpdateType.ROLLBACK
            }

            else -> {
                firmwareItem.testFirmwareItem.updateType = UpdateType.MINOR
                if (firmwareItem.testFirmwareItem.androidVersion.isBlank()) {
                    firmwareItem.testFirmwareItem.androidVersion =
                        firmwareItem.officialFirmwareItem.androidVersion
                }
            }
        }
    }

    private fun getDowngradeInfo(firmwareItem: FirmwareItem, testFirmware: String) {
        val officialFirmwareBootloader =
            Tools.getBuildInfo(firmwareItem.officialFirmwareItem.latestFirmware)
        val testFirmwareBootloader = Tools.getBuildInfo(testFirmware)

        if (officialFirmwareBootloader.isEmpty() || testFirmwareBootloader.isEmpty()) {
            return
        }

        firmwareItem.testFirmwareItem.isDowngradable =
            officialFirmwareBootloader.substring(0, 2) == testFirmwareBootloader.substring(0, 2)
    }

    companion object {
        const val OSP_PREFIX = "fota-cloud-dn.ospserver.net/firmware"
        const val OSP_SUFFIX_OFFICIAL = "version.xml"
        const val OSP_SUFFIX_TEST = "version.test.xml"
        const val NOTIFY_DOC_PREFIX = "doc.samsungmobile.com"
        const val NOTIFY_DOC_SUFFIX = "doc.html"
    }
}
