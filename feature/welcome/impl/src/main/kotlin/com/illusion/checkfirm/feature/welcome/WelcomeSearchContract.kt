package com.illusion.checkfirm.feature.welcome

data class WelcomeSearchDevice(val model: String, val csc: String)

data class WelcomeSearchUiState(
    val isWelcomeSearchEnabled: Boolean = true,
    val showDialog: Boolean = false,
    val model: String = "SM-",
    val csc: String = "",
    val selectedChip: String? = null,
    val devices: List<WelcomeSearchDevice> = emptyList(),
)
