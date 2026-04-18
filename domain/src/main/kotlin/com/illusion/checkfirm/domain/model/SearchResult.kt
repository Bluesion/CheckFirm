package com.illusion.checkfirm.domain.model

/**
 * 실제 검색에 사용하는 data class
 */
data class SearchResult(
    val device: Device,
    val firmware: Firmware
)