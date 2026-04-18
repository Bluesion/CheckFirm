package com.illusion.checkfirm.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.domain.model.Preference
import com.illusion.checkfirm.domain.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class PreferenceDialog {
    None, Profile, Theme, Language, BookmarkOrder, BookmarkReset
}

@HiltViewModel
class PreferenceViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    val preference: StateFlow<Preference> = preferenceRepository.getSettings()
        .stateIn(viewModelScope, SharingStarted.Eagerly, Preference())

    fun updateProfileName(name: String) = viewModelScope.launch {
        preferenceRepository.updateSettings(preference.value.copy(profileName = name))
    }

    fun updateTheme(theme: String) = viewModelScope.launch {
        preferenceRepository.updateSettings(preference.value.copy(theme = theme))
    }

    fun updateLanguage(language: String) = viewModelScope.launch {
        preferenceRepository.updateSettings(preference.value.copy(language = language))
    }

    fun updateQuickSearchBar(enabled: Boolean) = viewModelScope.launch {
        preferenceRepository.updateSettings(preference.value.copy(isQuickSearchBarEnabled = enabled))
    }

    fun updateBookmarkOrder(order: String, ascending: Boolean) = viewModelScope.launch {
        preferenceRepository.updateSettings(
            preference.value.copy(bookmarkOrder = order, isBookmarkAscOrder = ascending)
        )
    }

    fun updateWelcomeSearch(enabled: Boolean) = viewModelScope.launch {
        preferenceRepository.updateSettings(preference.value.copy(isWelcomeSearchEnabled = enabled))
    }

    fun updateInfoCatcher(enabled: Boolean) = viewModelScope.launch {
        preferenceRepository.updateSettings(preference.value.copy(isInfoCatcherEnabled = enabled))
    }

    fun updateFirebase(enabled: Boolean) = viewModelScope.launch {
        preferenceRepository.updateSettings(preference.value.copy(isFirebaseEnabled = enabled))
    }
}
