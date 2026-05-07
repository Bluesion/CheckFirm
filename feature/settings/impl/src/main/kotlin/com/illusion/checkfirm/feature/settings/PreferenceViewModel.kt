package com.illusion.checkfirm.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.core.preference.api.PreferenceRepository
import com.illusion.checkfirm.domain.repository.BCRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class PreferenceViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository,
    private val bcRepository: BCRepository,
) : ViewModel() {

    private val _activeDialog = MutableStateFlow(PreferenceDialog.None)

    val uiState: StateFlow<PreferenceUiState> = combine(
        preferenceRepository.getSettings(),
        _activeDialog,
    ) { preference, activeDialog ->
        PreferenceUiState(preference = preference, activeDialog = activeDialog)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, PreferenceUiState())

    fun updateActiveDialog(dialog: PreferenceDialog) {
        _activeDialog.value = dialog
    }

    fun updateProfileName(name: String) = viewModelScope.launch {
        preferenceRepository.updateSettings(uiState.value.preference.copy(profileName = name))
    }

    fun updateTheme(theme: String) = viewModelScope.launch {
        preferenceRepository.updateSettings(uiState.value.preference.copy(theme = theme))
    }

    fun updateLanguage(language: String) = viewModelScope.launch {
        preferenceRepository.updateSettings(uiState.value.preference.copy(language = language))
    }

    fun updateQuickSearchBar(enabled: Boolean) = viewModelScope.launch {
        preferenceRepository.updateSettings(uiState.value.preference.copy(isQuickSearchBarEnabled = enabled))
    }

    fun updateBookmarkOrder(order: String, ascending: Boolean) = viewModelScope.launch {
        preferenceRepository.updateSettings(
            uiState.value.preference.copy(bookmarkOrder = order, isBookmarkAscOrder = ascending)
        )
    }

    fun updateWelcomeSearch(enabled: Boolean) = viewModelScope.launch {
        preferenceRepository.updateSettings(uiState.value.preference.copy(isWelcomeSearchEnabled = enabled))
    }

    fun updateInfoCatcher(enabled: Boolean) = viewModelScope.launch {
        preferenceRepository.updateSettings(uiState.value.preference.copy(isInfoCatcherEnabled = enabled))
    }

    fun updateFirebase(enabled: Boolean) = viewModelScope.launch {
        preferenceRepository.updateSettings(uiState.value.preference.copy(isFirebaseEnabled = enabled))
    }

    fun resetBookmarks() = viewModelScope.launch {
        bcRepository.deleteAllBookmark()
        bcRepository.deleteAllCategory()
    }
}
