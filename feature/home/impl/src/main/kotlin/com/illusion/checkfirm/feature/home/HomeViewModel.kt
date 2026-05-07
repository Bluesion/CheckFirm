package com.illusion.checkfirm.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.core.navigation.NavResultBus
import com.illusion.checkfirm.core.navigation.NavResultKey
import com.illusion.checkfirm.core.preference.api.PreferenceRepository
import com.illusion.checkfirm.domain.model.Device
import com.illusion.checkfirm.domain.model.Firmware
import com.illusion.checkfirm.domain.model.SearchResult
import com.illusion.checkfirm.domain.remote.FirmwareFetcher
import com.illusion.checkfirm.domain.repository.BCRepository
import com.illusion.checkfirm.domain.repository.WelcomeSearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    preferenceRepository: PreferenceRepository,
    private val bcRepository: BCRepository,
    private val welcomeSearchRepository: WelcomeSearchRepository,
    private val firmwareFetcher: FirmwareFetcher,
    resultBus: NavResultBus,
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<String?>(null) // null = show all
    private val _showCategoryDialog = MutableStateFlow(false)

    private val _results = MutableStateFlow<List<SearchResult>>(emptyList())
    private val _resultState = MutableStateFlow<ResultState>(ResultState.Idle)
    private val _openedDialog = MutableStateFlow<SearchResult?>(null)

    init {
        // Auto-run a welcome search on startup if the preference is on; otherwise idle.
        viewModelScope.launch {
            val prefs = preferenceRepository.getSettings().first()
            if (prefs.isWelcomeSearchEnabled) {
                val devices = welcomeSearchRepository.allDevices.first()
                if (devices.isNotEmpty()) {
                    fetch(devices)
                }
            }
        }

        // Bus → search trigger.
        viewModelScope.launch {
            merge(
                resultBus.asSharedFlow(NavResultKey.HomeSearch),
                flow {
                    resultBus.asSharedFlow(NavResultKey.HomeBookmarkPick)
                        .collect { single -> emit(listOf(single)) }
                },
            ).collect { devices ->
                if (devices.isNotEmpty()) {
                    fetch(devices.map { (m, c) -> Device(m, c) })
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val bookmarksFlow = preferenceRepository.getSettings()
        .flatMapLatest { p ->
            bcRepository.getAllBookmark(p.bookmarkOrder, p.isBookmarkAscOrder)
        }

    val uiState: StateFlow<HomeUiState> = combine(
        listOf(
            preferenceRepository.getSettings(),
            bcRepository.getAllCategory(),
            bookmarksFlow,
            _selectedCategory,
            _showCategoryDialog,
            _results,
            _resultState,
            _openedDialog,
        ),
    ) { values ->
        @Suppress("UNCHECKED_CAST")
        HomeUiState(
            preference = values[0] as com.illusion.checkfirm.core.preference.api.Preference,
            categories = values[1] as List<com.illusion.checkfirm.domain.model.Category>,
            bookmarks = values[2] as List<com.illusion.checkfirm.domain.model.Bookmark>,
            selectedCategory = (values[3] as String?).orEmpty(),
            showCategoryDialog = values[4] as Boolean,
            results = values[5] as List<SearchResult>,
            resultState = values[6] as ResultState,
            openedDialog = values[7] as SearchResult?,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = HomeUiState(),
    )

    fun updateSelectedCategory(category: String) {
        _selectedCategory.value = category.takeIf { it.isNotBlank() }
    }

    fun showCategoryDialog(show: Boolean) {
        _showCategoryDialog.value = show
    }

    fun openResultDialog(result: SearchResult) {
        _openedDialog.value = result
    }

    fun closeResultDialog() {
        _openedDialog.value = null
    }

    fun searchSingle(device: Device) {
        fetch(listOf(device))
    }

    /** Run firmware fetches in parallel for [devices], updating state as we go. */
    private fun fetch(devices: List<Device>) {
        viewModelScope.launch {
            _resultState.value = ResultState.Loading
            val results = try {
                coroutineScope {
                    devices.map { device ->
                        async {
                            val official = firmwareFetcher.fetchOfficialFirmwareInfo(device)
                            val test = firmwareFetcher.fetchTestFirmwareInfo(device)
                            SearchResult(
                                device = device,
                                firmware = Firmware(
                                    officialFirmware = official
                                        ?: com.illusion.checkfirm.domain.model.OfficialFirmware(),
                                    testFirmware = test
                                        ?: com.illusion.checkfirm.domain.model.TestFirmware(),
                                ),
                            )
                        }
                    }.awaitAll()
                }
            } catch (_: Throwable) {
                _results.value = emptyList()
                _resultState.value = ResultState.NetworkError
                return@launch
            }
            _results.value = results
            _resultState.value = when {
                results.isEmpty() -> ResultState.Empty
                results.all { it.firmware.officialFirmware.latestFirmware.isBlank() } ->
                    ResultState.Empty

                else -> ResultState.Success
            }
        }
    }
}
