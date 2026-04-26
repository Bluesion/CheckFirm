package com.illusion.checkfirm.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Device
import com.illusion.checkfirm.domain.repository.BCRepository
import com.illusion.checkfirm.feature.search.util.SearchValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.Locale

data class SearchUiState(
    val model: String = "",
    val csc: String = "",
    val selectedTabIndex: Int = 0,
    val searchList: List<SearchDeviceItem> = emptyList()
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    bcRepository: BCRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    val bookmarks: StateFlow<List<Bookmark>> =
        bcRepository.getAllBookmark("date", true).stateIn(
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

    fun updateSelectedTabIndex(value: Int) {
        _uiState.value = _uiState.value.copy(selectedTabIndex = value)
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

    companion object {
        const val MAX_SEARCH_CAPACITY = 5
    }
}
