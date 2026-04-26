package com.illusion.checkfirm.feature.welcome

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class WelcomeSearchUiState(
    val isWelcomeSearchEnabled: Boolean = true,
    val showDialog: Boolean = false,
    val model: String = "SM-",
    val csc: String = "",
    val selectedChip: String? = null,
)

@HiltViewModel
class WelcomeSearchViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(WelcomeSearchUiState())
    val uiState: StateFlow<WelcomeSearchUiState> = _uiState.asStateFlow()

    fun updateIsWelcomeSearchEnabled(value: Boolean) {
        _uiState.value = _uiState.value.copy(isWelcomeSearchEnabled = value)
    }

    fun updateShowDialog(value: Boolean) {
        _uiState.value = _uiState.value.copy(showDialog = value)
    }

    fun updateModel(value: String) {
        _uiState.value = _uiState.value.copy(model = value)
    }

    fun updateCsc(value: String) {
        _uiState.value = _uiState.value.copy(csc = value)
    }

    fun updateSelectedChip(value: String?) {
        _uiState.value = _uiState.value.copy(selectedChip = value)
    }
}
