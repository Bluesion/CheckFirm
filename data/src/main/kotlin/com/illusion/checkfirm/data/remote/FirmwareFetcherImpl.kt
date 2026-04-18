package com.illusion.checkfirm.data.remote

import com.fleeksoft.ksoup.Ksoup
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.illusion.checkfirm.domain.model.Device
import com.illusion.checkfirm.domain.model.Firmware
import com.illusion.checkfirm.domain.model.FirmwareUpdateType
import com.illusion.checkfirm.domain.model.OfficialFirmware
import com.illusion.checkfirm.domain.model.SearchResult
import com.illusion.checkfirm.domain.model.TestFirmware
import com.illusion.checkfirm.domain.remote.FirmwareFetcher
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

private data class OfficialFirmwareDetail(
    val deviceName: String,
    val releaseDate: String?,
    val androidVersion: String?
)

class FirmwareFetcherImpl @Inject constructor(
    private val xmlClient: HttpClient,
    private val commonClient: HttpClient,
    private val db: FirebaseFirestore
) : FirmwareFetcher {

    suspend fun search(
        device: Device,
        isFirebaseEnabled: Boolean,
        profileName: String
    ): SearchResult = withContext(Dispatchers.IO) {
        val firmware = fetchFirmwareInfo(device)

        if (isFirebaseEnabled) {
            smartSearch(device, firmware, profileName)
        } else {
            defaultSearch(firmware)
        }

        SearchResult(device, firmware)
    }

    private suspend fun fetchFirmwareInfo(device: Device): Firmware {
        val officialFirmwareInfo = fetchOfficialFirmwareInfo(device)
        val officialFirmwareNotifyDocUrl = fetchOfficialFirmwareNotifyDocUrl(device)
        val officialFirmwareNotifyDoc =
            fetchOfficialFirmwareNotifyDoc(
                officialFirmwareNotifyDocUrl,
                officialFirmwareInfo?.latestFirmware ?: ""
            )

        return Firmware(
            officialFirmware = OfficialFirmware(
                latestFirmware = officialFirmwareInfo?.latestFirmware ?: "",
                previousFirmware = officialFirmwareInfo?.previousFirmware ?: emptyMap(),
                deviceName = officialFirmwareNotifyDoc?.deviceName ?: "",
                releaseDate = officialFirmwareNotifyDoc?.releaseDate ?: "",
                androidVersion = officialFirmwareNotifyDoc?.androidVersion ?: ""
            ),
            testFirmware = fetchTestFirmwareInfo(device)
        )
    }

    override suspend fun fetchOfficialFirmwareInfo(
        device: Device
    ): OfficialFirmware? {
        return try {
            val response =
                xmlClient.get {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = OSP_PREFIX
                        encodedPathSegments = listOf(device.csc, device.model, OSP_SUFFIX_OFFICIAL)
                    }
                }

            val fetchResult = response.body<FirmwareVersionInfo>()

            val tempHashMap = HashMap<String, String>()
            fetchResult.firmware.version.previous.list.forEach {
                tempHashMap[it.firmware] = it.firmware
            }

            OfficialFirmware(
                latestFirmware = fetchResult.firmware.version.latest.firmware,
                previousFirmware = tempHashMap.toSortedMap(reverseOrder()),
                deviceName = "",
                releaseDate = "",
                androidVersion = fetchResult.firmware.version.latest.androidVersion,
            )
        } catch (_: Exception) {
            null
        }
    }

    private suspend fun fetchOfficialFirmwareNotifyDocUrl(
        device: Device
    ): String? {
        return try {
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

            doc.select("input#dflt_page").attr("value").take(6)
        } catch (_: Exception) {
            null
        }
    }

    // example url = SM-A720S/000065170818/kor.html
    private suspend fun fetchOfficialFirmwareNotifyDoc(
        url: String?,
        officialLatest: String = ""
    ): OfficialFirmwareDetail? {
        return try {
            if (url == null) return null

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

            val rawDeviceName = notifyDoc.select("div > div:eq(2) > h1 > b").text()
            val deviceName = rawDeviceName.indexOf("(").let {
                if (it == -1) {
                    rawDeviceName
                } else {
                    rawDeviceName.take(it)
                }
            }

            var latestVersionFromDoc = notifyDoc.select("div > div:eq(4) > div:eq(0)").text()
            latestVersionFromDoc.indexOf(":").let {
                latestVersionFromDoc = latestVersionFromDoc.substring(it + 2)
            }

            var officialReleaseDate: String? = null
            var officialAndroidVersion: String? = null
            // Notify doc에는 최신 빌드 버전이 늦게 반영되는 경우가 있음
            // 최신 빌드 버전이 늦게 반영되는 경우, 릴리즈 날짜는 공백으로, 안드로이드 버전은 XML에서 가져온 버전을 그대로 사용한다.
            if (officialLatest.isNotBlank() && FirmwareTools.getShortBuildInfo(officialLatest)
                == FirmwareTools.getShortBuildInfo(latestVersionFromDoc)
            ) {

                officialReleaseDate =
                    notifyDoc.select("div > div:eq(4) > div:eq(2)").text()
                officialReleaseDate.indexOf(":").let {
                    officialReleaseDate = officialReleaseDate.take(it + 2)
                }

                officialAndroidVersion = notifyDoc.select("div > div:eq(4) > div:eq(1)").text()
                officialAndroidVersion.indexOf(":").let {
                    officialAndroidVersion = officialAndroidVersion.take(it + 2)
                }
            }

            OfficialFirmwareDetail(
                deviceName = deviceName,
                releaseDate = officialReleaseDate,
                androidVersion = getOfficialAndroidVersion(officialAndroidVersion)
            )
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun fetchTestFirmwareInfo(
        device: Device
    ): TestFirmware {
        val testFirmware = TestFirmware()
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

            testFirmware.latestFirmware = fetchResult.firmware.version.latest.firmware
            testFirmware.androidVersion = fetchResult.firmware.version.latest.androidVersion

            fetchResult.firmware.version.previous.list.forEach {
                if (Character.isUpperCase(it.firmware[0])) {
                    if (FirmwareTools.isBetaFirmware(it.firmware)) {
                        betaHashMap[it.firmware] = it.firmware
                    } else {
                        previousHashMap[it.firmware] = it.firmware
                    }
                } else {
                    previousHashMap[it.firmware] = it.firmware
                }
            }

            testFirmware.previousFirmware =
                previousHashMap.toSortedMap(reverseOrder())

            testFirmware.betaFirmware =
                betaHashMap.toSortedMap(reverseOrder())

            return testFirmware
        } catch (_: UnknownHostException) {
            return testFirmware
        } catch (_: ApiException) {
            return testFirmware
        } catch (_: Exception) {
            return testFirmware
        }
    }

    private fun getOfficialAndroidVersion(rawString: String?): String? {
        if (rawString == null) return null

        // CASE: {"Marshmallow(Android 6.0.1)", "SOMETHING(Android 7.0)", "Pie(Android 9)", "U(Android 14)", "RTOS 1.0"}
        val parenthesesIndex = rawString.indexOf("(")

        return if (parenthesesIndex == -1) {
            rawString
        } else {
            try {
                rawString.substring(parenthesesIndex + 9, rawString.length - 1)
            } catch (_: Exception) {
                null
            }
        }
    }

    private fun smartSearch(
        device: Device,
        firmware: Firmware,
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
                add(docRef, firmware, profileName)
                return
            }

            if (firestoreDiscoverer == "null") {
                firmware.testFirmware.discoverer = "Unknown"
            } else {
                firmware.testFirmware.discoverer = firestoreDiscoverer
            }
            firmware.testFirmware.discoveryDate = firestoreDate

            if (firestoreCount == "null") {
                docRef.update(
                    "count",
                    firmware.testFirmware.previousFirmware.size
                )
            } else {
                if (firestoreCount.toInt() != firmware.testFirmware.previousFirmware.size) {
                    val newDate = FirmwareTools.dateToString(FirmwareTools.getCurrentDateTime())

                    docRef.update("date_latest", newDate)
                    docRef.update("discoverer", profileName)
                    docRef.update("firmware_decrypted", "null")
                    docRef.update(
                        "count",
                        firmware.testFirmware.previousFirmware.size
                    )
                    updateNotification(device)

                    firmware.testFirmware.discoveryDate = newDate
                    firmware.testFirmware.discoverer = profileName
                }
            }

            if (firmware.testFirmware.latestFirmware.isBlank()) {
                if (firmware.testFirmware.previousFirmware.isEmpty()) {
                    getFirmwareType(
                        firmware,
                        firmware.testFirmware.latestFirmware
                    )
                    getDowngradeInfo(
                        firmware,
                        firmware.testFirmware.latestFirmware
                    )
                } else {
                    if (Character.isUpperCase(firmware.testFirmware.previousFirmware.keys.first()[0])) {
                        firmware.testFirmware.clue =
                            firmware.testFirmware.previousFirmware.keys.first()
                    } else {
                        if (firestoreClue == "null") {
                            docRef.update(
                                "clue",
                                firmware.officialFirmware.latestFirmware
                            )
                            firmware.testFirmware.clue =
                                firmware.officialFirmware.latestFirmware
                            firmware.testFirmware.watson = profileName
                        } else {
                            firmware.testFirmware.clue = firestoreClue
                            firmware.testFirmware.watson = firestoreWatson
                        }
                    }
                    getFirmwareType(firmware, firmware.testFirmware.clue)
                    getDowngradeInfo(firmware, firmware.testFirmware.clue)
                }
            } else {
                if (firmware.testFirmware.latestFirmware.contains("/")) {
                    getFirmwareType(
                        firmware,
                        firmware.testFirmware.latestFirmware
                    )
                    getDowngradeInfo(
                        firmware,
                        firmware.testFirmware.latestFirmware
                    )
                } else {
                    if (firestoreDecrypted != "null") {
                        if (FirmwareTools.getMD5Hash(firestoreDecrypted) == firestoreLatest) {
                            firmware.testFirmware.decryptedFirmware =
                                firestoreDecrypted
                            firmware.testFirmware.watson = firestoreWatson
                            getFirmwareType(firmware, firestoreDecrypted)
                            getDowngradeInfo(firmware, firestoreDecrypted)
                        } else {
                            getFirmwareType(
                                firmware,
                                firmware.testFirmware.latestFirmware
                            )
                            getDowngradeInfo(
                                firmware,
                                firmware.testFirmware.latestFirmware
                            )
                        }
                    } else {
                        getFirmwareType(
                            firmware,
                            firmware.testFirmware.latestFirmware
                        )
                        getDowngradeInfo(
                            firmware,
                            firmware.testFirmware.latestFirmware
                        )
                    }
                }

                if (firestoreLatest != firmware.testFirmware.latestFirmware) {
                    val newDate = FirmwareTools.dateToString(FirmwareTools.getCurrentDateTime())

                    docRef.update("date_latest", newDate)
                    docRef.update(
                        "firmware_latest",
                        firmware.testFirmware.latestFirmware
                    )
                    docRef.update("discoverer", profileName)
                    docRef.update("watson", "null")
                    docRef.update("firmware_decrypted", "null")
                    docRef.update(
                        "count",
                        firmware.testFirmware.previousFirmware.size
                    )
                    updateNotification(device)

                    firmware.testFirmware.discoveryDate = newDate
                    firmware.testFirmware.discoverer = profileName
                    firmware.testFirmware.watson = ""
                    firmware.testFirmware.decryptedFirmware = ""
                }
            }
        }
    }

    private fun updateNotification(device: Device) {
        val items = HashMap<String, Any>()
        items["model"] = device.model
        items["csc"] = device.csc
        db.collection("A_NOTIFICATION").document("UPDATE").set(items)
    }

    private fun defaultSearch(firmware: Firmware) {
        val officialLatest = firmware.officialFirmware.latestFirmware
        val testLatest = firmware.testFirmware.latestFirmware
        val testPrevious = firmware.testFirmware.previousFirmware

        val currentOfficial = FirmwareTools.getBuildInfo(officialLatest)
        val currentTest: String

        if (testLatest.isEmpty() && testPrevious.isNotEmpty()) {
            if (Character.isUpperCase(testPrevious.keys.first()[0])) {
                firmware.testFirmware.clue =
                    testPrevious.keys.first()
                currentTest = testPrevious.keys.first()
            } else {
                firmware.testFirmware.clue = officialLatest
                currentTest = FirmwareTools.getBuildInfo(officialLatest)
            }
        } else {
            currentTest = FirmwareTools.getBuildInfo(testLatest)
        }

        if (currentOfficial.isNotEmpty() && currentTest.isNotBlank()) {
            val compare = currentOfficial[2].compareTo(currentTest[2])
            when {
                compare < 0 -> {
                    firmware.testFirmware.firmwareUpdateType =
                        FirmwareUpdateType.MAJOR
                    getNextAndroidVersion(firmware.officialFirmware.androidVersion)
                }

                compare > 0 -> {
                    firmware.testFirmware.firmwareUpdateType =
                        FirmwareUpdateType.ROLLBACK
                }

                else -> {
                    firmware.testFirmware.firmwareUpdateType =
                        FirmwareUpdateType.MINOR
                    firmware.testFirmware.androidVersion =
                        firmware.officialFirmware.androidVersion
                }
            }

            firmware.testFirmware.isDowngradable =
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

    private fun add(docRef: DocumentReference, firmware: Firmware, profileName: String) {
        val date = FirmwareTools.dateToString(FirmwareTools.getCurrentDateTime())
        val items = HashMap<String, Any>()
        items["date_latest"] = date
        items["firmware_latest"] = firmware.testFirmware.latestFirmware
        items["discoverer"] = profileName
        items["firmware_decrypted"] = "null"
        items["watson"] = "null"
        items["count"] = firmware.testFirmware.previousFirmware.size
        docRef.set(items)

        firmware.testFirmware.discoveryDate = date
        firmware.testFirmware.discoverer = profileName

        if (firmware.testFirmware.latestFirmware.isBlank()) {
            if (firmware.testFirmware.previousFirmware.isEmpty()) {
                getFirmwareType(firmware, firmware.testFirmware.latestFirmware)
                getDowngradeInfo(firmware, firmware.testFirmware.latestFirmware)
            } else {
                if (Character.isUpperCase(firmware.testFirmware.previousFirmware.keys.first()[0])) {
                    firmware.testFirmware.clue =
                        firmware.testFirmware.previousFirmware.keys.first()
                }
                getFirmwareType(firmware, firmware.testFirmware.clue)
                getDowngradeInfo(firmware, firmware.testFirmware.clue)
            }
        } else {
            getFirmwareType(firmware, firmware.testFirmware.latestFirmware)
            getDowngradeInfo(firmware, firmware.testFirmware.latestFirmware)
        }
    }

    private fun getFirmwareType(firmware: Firmware, testFirmware: String) {
        val currentFirmware = FirmwareTools.getBuildInfo(firmware.officialFirmware.latestFirmware)
        val nextFirmware = FirmwareTools.getBuildInfo(testFirmware)

        if (currentFirmware.isEmpty() || nextFirmware.isEmpty()) {
            return
        }

        val compare =
            FirmwareTools.getBuildInfo(firmware.officialFirmware.latestFirmware)[2].compareTo(
                FirmwareTools.getBuildInfo(testFirmware)[2]
            )
        when {
            compare < 0 -> {
                firmware.testFirmware.firmwareUpdateType = FirmwareUpdateType.MAJOR
            }

            compare > 0 -> {
                firmware.testFirmware.firmwareUpdateType = FirmwareUpdateType.ROLLBACK
            }

            else -> {
                firmware.testFirmware.firmwareUpdateType = FirmwareUpdateType.MINOR
                if (firmware.testFirmware.androidVersion.isBlank()) {
                    firmware.testFirmware.androidVersion =
                        firmware.officialFirmware.androidVersion
                }
            }
        }
    }

    private fun getDowngradeInfo(firmware: Firmware, testFirmware: String) {
        val officialFirmwareBootloader =
            FirmwareTools.getBuildInfo(firmware.officialFirmware.latestFirmware)
        val testFirmwareBootloader = FirmwareTools.getBuildInfo(testFirmware)

        if (officialFirmwareBootloader.isEmpty() || testFirmwareBootloader.isEmpty()) {
            return
        }

        firmware.testFirmware.isDowngradable =
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
