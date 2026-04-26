package com.illusion.checkfirm.feature.settings.welcome

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class WelcomeSearchViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(WelcomeSearchUiState())
    val uiState: StateFlow<WelcomeSearchUiState> = _uiState.asStateFlow()

    fun updateIsWelcomeSearchEnabled(value: Boolean) {
        _uiState.update { it.copy(isWelcomeSearchEnabled = value) }
    }

    fun updateShowDialog(value: Boolean) {
        _uiState.update { it.copy(showDialog = value) }
    }

    fun updateModel(value: String) {
        _uiState.update { it.copy(model = value) }
    }

    fun updateCsc(value: String) {
        _uiState.update { it.copy(csc = value) }
    }

    fun updateSelectedChip(value: String?) {
        _uiState.update { it.copy(selectedChip = value) }
    }

    fun addDevice(model: String, csc: String) {
        _uiState.update {
            it.copy(
                devices = it.devices + WelcomeSearchDevice(model, csc),
                showDialog = false,
                model = "SM-",
                csc = "",
                selectedChip = null,
            )
        }
    }

    fun removeDevice(device: WelcomeSearchDevice) {
        _uiState.update { it.copy(devices = it.devices - device) }
    }
}
