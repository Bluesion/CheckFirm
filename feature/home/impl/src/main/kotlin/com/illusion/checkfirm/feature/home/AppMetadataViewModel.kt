package com.illusion.checkfirm.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.domain.model.ApiResponse
import com.illusion.checkfirm.domain.model.AppVersionStatus
import com.illusion.checkfirm.domain.repository.AppMetadataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AppMetadataViewModel @Inject constructor(
    private val appMetadataRepository: AppMetadataRepository
) : ViewModel() {

    private val _isOldVersion = MutableStateFlow<ApiResponse<AppVersionStatus>>(ApiResponse.Loading)
    val isOldVersion: StateFlow<ApiResponse<AppVersionStatus>> = _isOldVersion.asStateFlow()

    init {
        viewModelScope.launch {
            checkAppVersion()
        }
    }

    private fun checkAppVersion(currentAppVersion: Int = 0) =
        viewModelScope.launch(Dispatchers.IO) {
            _isOldVersion.value = appMetadataRepository.checkAppVersion(currentAppVersion)
        }
}