package com.illusion.checkfirm.data.model

import java.util.SortedMap

data class OfficialFirmwareItem(
    var latestFirmware: String = "",
    var previousFirmware: SortedMap<String, String> = sortedMapOf(),
    var deviceName: String = "",
    var releaseDate: String = "",
    var androidVersion: String = ""
)