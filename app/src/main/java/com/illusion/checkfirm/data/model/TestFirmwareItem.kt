package com.illusion.checkfirm.data.model

import java.util.SortedMap

data class TestFirmwareItem(
    var latestFirmware: String = "",
    var previousFirmware: SortedMap<String, String> = sortedMapOf(),
    var betaFirmware: SortedMap<String, String> = sortedMapOf(),
    var discoveryDate: String = "",
    var updateType: String = "",
    var androidVersion: String = "",
    var isDowngradable: Boolean = false,
    var decryptedFirmware: String = "",
    var discoverer: String = "",
    var watson: String = "",
    var clue: String = ""
)