package com.illusion.checkfirm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.core.preference.api.PreferenceRepository
import com.illusion.checkfirm.feature.forceupdate.ForceUpdateRouteNavKey
import com.illusion.checkfirm.feature.home.api.AppVersionStatus
import com.illusion.checkfirm.feature.home.api.FetchLatestAppVersionUseCase
import com.illusion.checkfirm.feature.home.api.HomeRouteNavKey
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val fetchLatestAppVersion: FetchLatestAppVersionUseCase,
    private val preferenceRepository: PreferenceRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    val appTheme = preferenceRepository.getSettings()
        .map { it.theme }
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, "system")

    private val _nextRoute = MutableStateFlow<Any?>(null)
    val nextRoute = _nextRoute.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _isLoading.value = true
        viewModelScope.launch {
            val themeDeferred = async(Dispatchers.IO) {
                preferenceRepository.getSettings().first().theme
            }

            val versionDeferred = async(Dispatchers.IO) {
                withTimeoutOrNull(timeout = 3.seconds) {
                    fetchLatestAppVersion()
                }
            }

            themeDeferred.await()
            val apiResponse = versionDeferred.await()

            when (apiResponse) {
                AppVersionStatus.UPDATE_REQUIRED -> {
                    _nextRoute.value = ForceUpdateRouteNavKey
                }

                else -> {
                    _nextRoute.value = HomeRouteNavKey
                }
            }

            _isLoading.value = false
        }
    }
}
