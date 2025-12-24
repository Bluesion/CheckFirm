package com.illusion.checkfirm.data.model.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.SortedMap

@Parcelize
data class OfficialFirmwareItem(
    var latestFirmware: String = "",
    var previousFirmware: SortedMap<String, String> = sortedMapOf(),
    var deviceName: String = "",
    var releaseDate: String = "",
    var androidVersion: String = ""
) : Parcelable