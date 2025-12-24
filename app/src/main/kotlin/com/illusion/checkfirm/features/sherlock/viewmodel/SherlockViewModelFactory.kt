package com.illusion.checkfirm.features.sherlock.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.illusion.checkfirm.data.repository.SettingsRepository

class SherlockViewModelFactory(private val settingsRepository: SettingsRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SherlockViewModel::class.java)) {
            return SherlockViewModel(settingsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}