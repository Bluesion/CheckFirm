package com.illusion.checkfirm.features.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.data.source.remote.MainDataSource
import com.illusion.checkfirm.data.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val mainRepository: MainRepository

    private val _isOldVersion = MutableStateFlow<Boolean?>(null)
    val isOldVersion: StateFlow<Boolean?> = _isOldVersion.asStateFlow()

    init {
        val mainDatasource = MainDataSource()
        mainRepository = MainRepository(mainDatasource, application)
        checkAppVersion()
    }

    private fun checkAppVersion() = viewModelScope.launch {
        _isOldVersion.value = mainRepository.checkAppVersion()
    }
}