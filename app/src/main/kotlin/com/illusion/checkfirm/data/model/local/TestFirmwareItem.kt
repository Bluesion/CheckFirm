package com.illusion.checkfirm.data.model.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.SortedMap

@Parcelize
data class TestFirmwareItem(
    var latestFirmware: String = "",
    var previousFirmware: SortedMap<String, String> = sortedMapOf(),
    var betaFirmware: SortedMap<String, String> = sortedMapOf(),
    var discoveryDate: String = "",
    var updateType: UpdateType = UpdateType.UNKNOWN,
    var androidVersion: String = "",
    var isDowngradable: Boolean = false,
    var decryptedFirmware: String = "",
    var discoverer: String = "",
    var watson: String = "",
    var clue: String = ""
) : Parcelable

enum class UpdateType {
    MAJOR, MINOR, ROLLBACK, UNKNOWN
}