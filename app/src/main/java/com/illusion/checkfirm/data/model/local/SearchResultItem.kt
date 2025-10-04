package com.illusion.checkfirm.data.model.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 실제 검색에 사용하는 data class
 */
@Parcelize
data class SearchResultItem(val device: DeviceItem, val firmware: FirmwareItem) : Parcelable