package com.illusion.checkfirm.feature.search

data class SearchUiState(
    val model: String = "",
    val csc: String = "",
    val searchList: List<SearchDeviceItem> = emptyList(),
)
