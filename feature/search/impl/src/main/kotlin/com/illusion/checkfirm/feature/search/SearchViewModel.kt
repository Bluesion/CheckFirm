package com.illusion.checkfirm.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.Date
import com.illusion.checkfirm.core.domain.model.Device
import com.illusion.checkfirm.core.domain.model.SearchHistory
import com.illusion.checkfirm.core.domain.repository.BCRepository
import com.illusion.checkfirm.core.domain.repository.HistoryRepository
import com.illusion.checkfirm.core.navigation.NavResultBus
import com.illusion.checkfirm.core.navigation.NavResultKey
import com.illusion.checkfirm.feature.search.util.SearchValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

data class SearchUiState(
    val model: String = "",
    val csc: String = "",
    val searchList: List<SearchDeviceItem> = emptyList()
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    bcRepository: BCRepository,
    private val historyRepository: HistoryRepository,
    private val resultBus: NavResultBus,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    val bookmarks: StateFlow<List<Bookmark>> =
        bcRepository.getAllBookmark("date", true).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val historyList: StateFlow<List<SearchHistory>> = historyRepository.getAllHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun updateModel(value: String) {
        _uiState.value = _uiState.value.copy(model = value)
    }

    fun updateCsc(value: String) {
        _uiState.value = _uiState.value.copy(csc = value)
    }

    fun onAddClick(): SearchValidationResult {
        val currentModel = _uiState.value.model
        val currentCsc = _uiState.value.csc
        return addToSearchList(
            Device(
                currentModel.trim().uppercase(Locale.US),
                currentCsc.trim().uppercase(Locale.US)
            )
        )
    }

    fun addToSearchList(device: Device): SearchValidationResult {
        val currentList = _uiState.value.searchList

        for (item in currentList) {
            if (device == item.device) return SearchValidationResult.DUPLICATED_DEVICE
        }

        if (device.model.isBlank() || device.csc.isBlank()) {
            return SearchValidationResult.INVALID_DEVICE
        }

        if (MAX_SEARCH_CAPACITY - currentList.size == 0) {
            return SearchValidationResult.MAX_SEARCH_CAPACITY_EXCEEDED
        }

        val updatedList = currentList.toMutableList()
        updatedList.add(SearchDeviceItem(device))
        _uiState.value = _uiState.value.copy(
            searchList = updatedList,
            model = "",
            csc = ""
        )

        return SearchValidationResult.SUCCESS
    }

    fun removeFromSearchList(device: Device) {
        val currentList = _uiState.value.searchList
        _uiState.value = _uiState.value.copy(
            searchList = currentList.filter { it.device != device }
        )
    }

    suspend fun getAllHistoryList(): List<SearchHistory> {
        return historyRepository.getAllHistoryList()
    }

    fun cleanUpHistory() = viewModelScope.launch {
        historyRepository.cleanUpHistory()
    }

    fun createHistory(list: List<SearchDeviceItem>) {
        insert(list)
    }

    fun confirmAndEmit() = viewModelScope.launch {
        val devices = _uiState.value.searchList
        if (devices.isEmpty()) return@launch
        insert(devices)
        resultBus.emit(
            NavResultKey.HomeSearch,
            devices.map { it.device.model to it.device.csc },
        )
    }

    fun insert(list: List<SearchDeviceItem>) = viewModelScope.launch {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val previousHistoryList = getAllHistoryList()

        for (deviceItem in list) {
            val model = deviceItem.device.model
            val csc = deviceItem.device.csc

            for (history in previousHistoryList) {
                if (history.device.model == model && history.device.csc == csc) {
                    delete(model, csc)
                }
            }

            historyRepository.insert(SearchHistory(Device(model, csc), Date(year, month, day)))
        }

        cleanUpHistory()
    }

    fun update(model: String, csc: String, year: Int, month: Int, day: Int) =
        viewModelScope.launch {
            historyRepository.update(SearchHistory(Device(model, csc), Date(year, month, day)))
        }

    fun delete(model: String, csc: String) = viewModelScope.launch {
        historyRepository.delete(SearchHistory(Device(model, csc), Date(0, 0, 0)))
    }

    fun deleteAll() = viewModelScope.launch {
        historyRepository.deleteAll()
    }

    companion object {
        const val MAX_SEARCH_CAPACITY = 5
    }
}
