package com.illusion.checkfirm.feature.sherlock

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SherlockUiState(
    val selectedTab: Int = 0,
    val pda: String = "",
    val csc: String = "",
    val baseband: String = "",
    val resultMessage: String? = null,
    val scriptStart: String = "",
    val scriptEnd: String = "",
)

@HiltViewModel
class SherlockViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(SherlockUiState())
    val uiState: StateFlow<SherlockUiState> = _uiState.asStateFlow()

    fun selectTab(index: Int) = _uiState.update { it.copy(selectedTab = index) }
    fun updatePda(value: String) = _uiState.update { it.copy(pda = value.uppercase()) }
    fun updateCsc(value: String) = _uiState.update { it.copy(csc = value.uppercase()) }
    fun updateBaseband(value: String) = _uiState.update { it.copy(baseband = value.uppercase()) }
    fun updateScriptStart(value: String) = _uiState.update { it.copy(scriptStart = value) }
    fun updateScriptEnd(value: String) = _uiState.update { it.copy(scriptEnd = value) }
    fun clearResult() = _uiState.update { it.copy(resultMessage = null) }
    fun setResult(message: String) = _uiState.update { it.copy(resultMessage = message) }
}
