package com.illusion.checkfirm.feature.bookmark.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.core.navigation.NavResultBus
import com.illusion.checkfirm.core.navigation.NavResultKey
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Device
import com.illusion.checkfirm.domain.repository.BCRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BookmarkListViewModel @Inject constructor(
    private val repository: BCRepository,
    private val resultBus: NavResultBus,
) : ViewModel() {

    private val _expanded = MutableStateFlow(false)

    // Empty string is the "All" sentinel; the screen substitutes the localized
    // R.string.category_all label when rendering the dropdown selection.
    private val _selectedCategory = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _bookmarksFlow = _selectedCategory.flatMapLatest { category ->
        if (category.isBlank()) {
            repository.getAllBookmark("date", true)
        } else {
            repository.getBookmarkByCategory("date", true, category)
        }
    }

    private data class UiFlags(
        val selectedTab: Int = 0,
        val editingBookmark: Bookmark? = null,
        val showNewBookmark: Boolean = false,
    )

    private val _uiFlags = MutableStateFlow(UiFlags())

    val uiState: StateFlow<BookmarkListUiState> = combine(
        _expanded,
        _selectedCategory,
        repository.getAllCategory(),
        _bookmarksFlow,
        _uiFlags,
    ) { expanded, selectedCategory, categories, bookmarks, flags ->
        BookmarkListUiState(
            expanded = expanded,
            selectedCategory = selectedCategory,
            categories = categories,
            bookmarks = bookmarks,
            selectedTab = flags.selectedTab,
            editingBookmark = flags.editingBookmark,
            showNewBookmark = flags.showNewBookmark,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BookmarkListUiState()
    )

    fun updateExpanded(value: Boolean) {
        _expanded.value = value
    }

    fun updateSelectedCategory(value: String) {
        _selectedCategory.value = value
    }

    fun updateSelectedTab(value: Int) {
        _uiFlags.update { it.copy(selectedTab = value) }
    }

    fun updateEditingBookmark(value: Bookmark?) {
        _uiFlags.update { it.copy(editingBookmark = value) }
    }

    fun updateShowNewBookmark(value: Boolean) {
        _uiFlags.update { it.copy(showNewBookmark = value) }
    }

    fun addBookmark(bookmark: Bookmark) {
        viewModelScope.launch { repository.addBookmark(bookmark) }
    }

    fun editBookmark(bookmark: Bookmark) {
        viewModelScope.launch { repository.editBookmark(bookmark) }
    }

    fun deleteBookmark(device: Device) {
        viewModelScope.launch { repository.deleteBookmark(device) }
    }

    fun deleteCategory(name: String) {
        viewModelScope.launch { repository.deleteCategory(name) }
    }

    fun emitItemPicked(bookmark: Bookmark) {
        viewModelScope.launch {
            resultBus.emit(
                NavResultKey.HomeBookmarkPick,
                bookmark.device.model to bookmark.device.csc,
            )
        }
    }
}
