package com.illusion.checkfirm.feature.settings.welcome

import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.Device

data class WelcomeSearchUiState(
    val isWelcomeSearchEnabled: Boolean = true,
    val showDialog: Boolean = false,
    val model: String = "SM-",
    val csc: String = "",
    val selectedChip: String? = null,
    val devices: List<Device> = emptyList(),
    val bookmarks: List<Bookmark> = emptyList(),
)
