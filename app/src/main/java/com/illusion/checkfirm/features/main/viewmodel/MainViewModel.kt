package com.illusion.checkfirm.features.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.data.model.remote.ApiResponse
import com.illusion.checkfirm.data.model.remote.AppVersionStatus
import com.illusion.checkfirm.data.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _isOldVersion = MutableStateFlow<ApiResponse<AppVersionStatus>>(ApiResponse.Loading)
    val isOldVersion: StateFlow<ApiResponse<AppVersionStatus>> = _isOldVersion.asStateFlow()

    init {
        viewModelScope.launch {
            checkAppVersion()
        }
    }

    private fun checkAppVersion() = viewModelScope.launch(Dispatchers.IO) {
        _isOldVersion.value = mainRepository.checkAppVersion()
    }
}