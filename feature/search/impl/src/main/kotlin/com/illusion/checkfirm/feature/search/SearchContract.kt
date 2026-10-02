package com.illusion.checkfirm.feature.search

data class SearchUiState(
    val model: String = "SM-",
    val csc: String = "",
    val searchList: List<SearchDeviceItem> = emptyList(),
)
