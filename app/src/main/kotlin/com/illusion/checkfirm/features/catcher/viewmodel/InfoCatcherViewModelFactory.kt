package com.illusion.checkfirm.features.catcher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.illusion.checkfirm.data.repository.InfoCatcherRepository

class InfoCatcherViewModelFactory(private val infoCatcherRepository: InfoCatcherRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InfoCatcherViewModel::class.java)) {
            return InfoCatcherViewModel(infoCatcherRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}