package com.illusion.checkfirm.feature.settings.help

import android.os.Build
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class MyDeviceViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(
        MyDeviceUiState(
            model = Build.MODEL ?: "",
            hardware = Build.DEVICE ?: "",
            manufacturer = Build.MANUFACTURER ?: "",
            sdk = Build.VERSION.SDK_INT.toString(),
            release = Build.VERSION.RELEASE ?: "",
        )
    )
    val uiState: StateFlow<MyDeviceUiState> = _uiState.asStateFlow()
}
