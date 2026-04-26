package com.illusion.checkfirm.feature.settings.help

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MyDeviceUiState(val dummy: Boolean = false)

@HiltViewModel
class MyDeviceViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(MyDeviceUiState())
    val uiState: StateFlow<MyDeviceUiState> = _uiState.asStateFlow()
}
