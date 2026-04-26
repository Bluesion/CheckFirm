package com.illusion.checkfirm.feature.settings.about

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class AboutViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(AboutUiState())
    val uiState: StateFlow<AboutUiState> = _uiState.asStateFlow()

    fun showDialog(dialogType: DialogType) {
        _uiState.update {
            it.copy(
                activeDialog = dialogType,
            )
        }
    }

    fun hideDialog() {
        _uiState.update {
            it.copy(
                activeDialog = DialogType.NONE,
            )
        }
    }
}
