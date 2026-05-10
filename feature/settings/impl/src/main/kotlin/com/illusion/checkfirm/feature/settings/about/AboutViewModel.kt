package com.illusion.checkfirm.feature.settings.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.core.domain.model.ApiResponse
import com.illusion.checkfirm.core.domain.model.CheckFirmBuildConfig
import com.illusion.checkfirm.core.domain.repository.AppMetadataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val appMetadataRepository: AppMetadataRepository,
    private val buildConfig: CheckFirmBuildConfig,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AboutUiState())
    val uiState: StateFlow<AboutUiState> = _uiState.asStateFlow()

    init {
        checkVersion()
    }

    fun showDialog(dialogType: DialogType) {
        _uiState.update { it.copy(activeDialog = dialogType) }
    }

    fun hideDialog() {
        _uiState.update { it.copy(activeDialog = DialogType.NONE) }
    }

    fun checkVersion() {
        viewModelScope.launch {
            _uiState.update { it.copy(versionCheck = VersionCheckState.Loading) }
            val state = when (val response = appMetadataRepository.fetchAppMetadata()) {
                is ApiResponse.Success -> {
                    val latest = response.data.appVersionData.latest.versionCode
                    if (buildConfig.versionCode >= latest) VersionCheckState.Latest
                    else VersionCheckState.Outdated
                }

                is ApiResponse.Error.NetworkError -> VersionCheckState.NetworkError
                is ApiResponse.Error -> VersionCheckState.Latest
            }
            _uiState.update { it.copy(versionCheck = state) }
        }
    }
}
