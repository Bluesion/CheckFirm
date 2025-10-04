package com.illusion.checkfirm.data.model.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FirmwareItem(
    var officialFirmwareItem: OfficialFirmwareItem = OfficialFirmwareItem(),
    var testFirmwareItem: TestFirmwareItem = TestFirmwareItem()
) : Parcelable