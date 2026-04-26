package com.illusion.checkfirm.feature.catcher.impl.presentation

import com.illusion.checkfirm.domain.model.Device

data class InfoCatcherUiState(
    val showDialog: Boolean = false,
    val isEnabled: Boolean = false,
    val devices: List<Device> = emptyList(),
    val dialogModel: String = "SM-",
    val dialogCsc: String = "",
)
