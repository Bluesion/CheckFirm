package com.illusion.checkfirm.data.model.local

/**
 * Bookmark는 additionalInfo에 북마크 이름, isDeleteButtonVisible은 false
 * Search History는 additionalInfo에 날짜, isDeleteButtonVisible은 true
 * Search Device List는 additionalInfo에 공백, isDeleteButtonVisible은 true
 * 날짜는 년-월-일로 저장하며, 년은 4자리, 월과 일은 2자리로 표시한다.
 */
data class SearchDeviceItem(
    val device: DeviceItem,
    val additionalInfo: String = "",
    val isDeleteButtonVisible: Boolean = true,
    var isChecked: Boolean = false
)