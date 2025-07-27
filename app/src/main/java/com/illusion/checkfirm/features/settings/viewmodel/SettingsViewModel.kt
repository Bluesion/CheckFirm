package com.illusion.checkfirm.features.settings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.data.model.SettingsItem
import com.illusion.checkfirm.data.source.local.SettingsDataSource
import com.illusion.checkfirm.data.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsRepository: SettingsRepository

    private val _settingsState = MutableStateFlow(SettingsItem())
    val settingsState: StateFlow<SettingsItem> = _settingsState.asStateFlow()

    init {
        val settingsDataSource = SettingsDataSource(application)
        settingsRepository = SettingsRepository(settingsDataSource)

        viewModelScope.launch {
            settingsRepository.getAllSettings().collect {
                _settingsState.value = it
            }
        }
    }

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
