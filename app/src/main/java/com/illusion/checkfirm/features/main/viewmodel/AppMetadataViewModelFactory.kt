package com.illusion.checkfirm.features.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.illusion.checkfirm.data.repository.AppMetadataRepository

class AppMetadataViewModelFactory(
    private val appMetadataRepository: AppMetadataRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppMetadataViewModel::class.java)) {
            return AppMetadataViewModel(appMetadataRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}