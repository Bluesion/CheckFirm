package com.illusion.checkfirm.feature.settings.help.firmware

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FirmwareManualUiState(val dummy: Boolean = false)

@HiltViewModel
class FirmwareManualViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(FirmwareManualUiState())
    val uiState: StateFlow<FirmwareManualUiState> = _uiState.asStateFlow()
}
