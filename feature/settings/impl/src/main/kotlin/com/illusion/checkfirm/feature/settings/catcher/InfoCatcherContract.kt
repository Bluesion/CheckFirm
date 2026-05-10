package com.illusion.checkfirm.feature.settings.catcher

import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.Device

data class InfoCatcherUiState(
    val showDialog: Boolean = false,
    val isEnabled: Boolean = false,
    val devices: List<Device> = emptyList(),
    val dialogModel: String = "SM-",
    val dialogCsc: String = "",
    val bookmarks: List<Bookmark> = emptyList(),
)
