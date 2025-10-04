package com.illusion.checkfirm.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.data.model.local.SettingsItem
import com.illusion.checkfirm.data.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    val settingsState: StateFlow<SettingsItem> = settingsRepository.getAllSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = SettingsItem()
        )

    suspend fun getAllSettings(): SettingsItem {
        return settingsRepository.getAllSettings().first()
    }

    fun setProfileName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.setProfileName(name)
        }
    }

    fun setTheme(theme: String) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.setTheme(theme)
        }
    }

    fun enableDynamicColor(enable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.enableDynamicColor(enable)
        }
    }

    fun setAppLanguage(language: String) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.setAppLanguage(language)
        }
    }

    fun enableQuickSearchBar(enable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.enableQuickSearchBar(enable)
        }
    }

    fun setBookmarkOrder(order: String) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.setBookmarkOrder(order)
        }
    }

    fun setBookmarkAscOrder(isAscOrder: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.setBookmarkAscOrder(isAscOrder)
        }
    }

    fun enableWelcomeSearch(enable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.enableWelcomeSearch(enable)
        }
    }

    fun enableInfoCatcher(enable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.enableInfoCatcher(enable)
        }
    }

    fun enableFirebase(enable: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.enableFirebase(enable)
        }
    }
}
