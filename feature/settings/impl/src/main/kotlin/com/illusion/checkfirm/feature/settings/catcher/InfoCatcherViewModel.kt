package com.illusion.checkfirm.feature.settings.catcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.illusion.checkfirm.core.preference.api.PreferenceRepository
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Device
import com.illusion.checkfirm.domain.repository.BCRepository
import com.illusion.checkfirm.domain.repository.InfoCatcherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class InfoCatcherViewModel @Inject constructor(
    private val repository: InfoCatcherRepository,
    private val preferenceRepository: PreferenceRepository,
    private val bcRepository: BCRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(InfoCatcherUiState())

    val uiState: StateFlow<InfoCatcherUiState> =
        combine(
            _uiState,
            repository.allDevices,
            preferenceRepository.getSettings(),
            bcRepository.getAllBookmark("date", true),
        ) { state, devices, prefs, bookmarks ->
            state.copy(
                devices = devices,
                isEnabled = prefs.isInfoCatcherEnabled,
                bookmarks = bookmarks,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InfoCatcherUiState()
        )

    fun selectBookmark(bookmark: Bookmark) {
        _uiState.update {
            it.copy(
                dialogModel = bookmark.device.model,
                dialogCsc = bookmark.device.csc,
            )
        }
    }

    fun showDialog() {
        _uiState.update { it.copy(showDialog = true) }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(showDialog = false, dialogModel = "SM-", dialogCsc = "") }
    }

    fun toggleEnabled(enabled: Boolean) {
        viewModelScope.launch {
            val current = preferenceRepository.getSettings().first()
            preferenceRepository.updateSettings(current.copy(isInfoCatcherEnabled = enabled))
        }
    }

    fun updateDialogModel(value: String) {
        _uiState.update { it.copy(dialogModel = value) }
    }

    fun updateDialogCsc(value: String) {
        _uiState.update { it.copy(dialogCsc = value) }
    }

    fun addDevice(model: String, csc: String) {
        viewModelScope.launch {
            repository.insert(Device(model, csc))
            FirebaseMessaging.getInstance().subscribeToTopic(model + csc)
            _uiState.update { it.copy(showDialog = false, dialogModel = "SM-", dialogCsc = "") }
        }
    }

    fun removeDevice(device: Device) {
        viewModelScope.launch {
            repository.delete(device)
            FirebaseMessaging.getInstance().unsubscribeFromTopic(device.model + device.csc)
        }
    }
}
