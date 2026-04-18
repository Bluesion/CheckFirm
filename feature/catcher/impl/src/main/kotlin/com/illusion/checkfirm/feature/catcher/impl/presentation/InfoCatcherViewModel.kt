package com.illusion.checkfirm.feature.catcher.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.domain.model.Device
import com.illusion.checkfirm.domain.repository.InfoCatcherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InfoCatcherUiState(
    val showDialog: Boolean = false,
    val isEnabled: Boolean = false,
    val devices: List<Device> = emptyList()
)

@HiltViewModel
class InfoCatcherViewModel @Inject constructor(
    private val repository: InfoCatcherRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(InfoCatcherUiState())

    val uiState: StateFlow<InfoCatcherUiState> =
        combine(_uiState, repository.allDevices) { state, devices ->
            state.copy(devices = devices)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InfoCatcherUiState()
        )

    fun showDialog() {
        _uiState.update { it.copy(showDialog = true) }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showDialog = false) }
    }

    fun toggleEnabled(enabled: Boolean) {
        _uiState.update { it.copy(isEnabled = enabled) }
    }

    fun addDevice(model: String, csc: String) {
        viewModelScope.launch {
            repository.insert(Device(model, csc))
            _uiState.update { it.copy(showDialog = false) }
        }
    }

    fun removeDevice(device: Device) {
        viewModelScope.launch {
            repository.delete(device)
        }
    }
}
