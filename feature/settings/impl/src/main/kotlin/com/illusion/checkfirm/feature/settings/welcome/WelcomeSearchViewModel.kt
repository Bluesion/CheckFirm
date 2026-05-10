package com.illusion.checkfirm.feature.settings.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.Device
import com.illusion.checkfirm.core.domain.repository.BCRepository
import com.illusion.checkfirm.core.domain.repository.WelcomeSearchRepository
import com.illusion.checkfirm.core.preference.api.PreferenceRepository
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
class WelcomeSearchViewModel @Inject constructor(
    private val welcomeSearchRepository: WelcomeSearchRepository,
    private val preferenceRepository: PreferenceRepository,
    bcRepository: BCRepository,
) : ViewModel() {

    private val _localState = MutableStateFlow(LocalState())

    val uiState: StateFlow<WelcomeSearchUiState> = combine(
        _localState,
        welcomeSearchRepository.allDevices,
        preferenceRepository.getSettings(),
        bcRepository.getAllBookmark("date", true),
    ) { local, devices, prefs, bookmarks ->
        WelcomeSearchUiState(
            isWelcomeSearchEnabled = prefs.isWelcomeSearchEnabled,
            showDialog = local.showDialog,
            model = local.model,
            csc = local.csc,
            selectedChip = local.selectedChip,
            devices = devices,
            bookmarks = bookmarks,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WelcomeSearchUiState(),
    )

    fun updateIsWelcomeSearchEnabled(value: Boolean) = viewModelScope.launch {
        val prefs = preferenceRepository.getSettings().first()
        preferenceRepository.updateSettings(prefs.copy(isWelcomeSearchEnabled = value))
    }

    fun updateShowDialog(value: Boolean) {
        _localState.update { it.copy(showDialog = value) }
    }

    fun updateModel(value: String) {
        _localState.update { it.copy(model = value) }
    }

    fun updateCsc(value: String) {
        _localState.update { it.copy(csc = value) }
    }

    fun selectBookmark(bookmark: Bookmark) {
        // Action chip semantics: clicking pre-fills the form.
        _localState.update {
            it.copy(
                selectedChip = bookmark.name,
                model = bookmark.device.model,
                csc = bookmark.device.csc,
            )
        }
    }

    fun addDevice(model: String, csc: String) = viewModelScope.launch {
        welcomeSearchRepository.insert(Device(model.trim(), csc.trim()))
        _localState.update { LocalState() }
    }

    fun removeDevice(device: Device) = viewModelScope.launch {
        welcomeSearchRepository.delete(device)
    }

    private data class LocalState(
        val showDialog: Boolean = false,
        val model: String = "SM-",
        val csc: String = "",
        val selectedChip: String? = null,
    )
}
