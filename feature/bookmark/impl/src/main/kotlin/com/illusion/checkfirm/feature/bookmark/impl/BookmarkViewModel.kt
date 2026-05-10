package com.illusion.checkfirm.feature.bookmark.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.Device
import com.illusion.checkfirm.core.domain.repository.BCRepository
import com.illusion.checkfirm.core.navigation.NavResultBus
import com.illusion.checkfirm.core.navigation.NavResultKey
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val repository: BCRepository,
    private val resultBus: NavResultBus,
) : ViewModel() {

    // Empty selectedCategory is the "All" sentinel; the screen substitutes the
    // localized R.string.category_all label when rendering the dropdown selection.
    private val _uiState = MutableStateFlow(BookmarkUiState())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val bookmarksFlow = _uiState
        .map { it.selectedCategory }
        .distinctUntilChanged()
        .flatMapLatest { category ->
            if (category.isBlank()) {
                repository.getAllBookmark("date", true)
            } else {
                repository.getBookmarkByCategory("date", true, category)
            }
        }

    val uiState: StateFlow<BookmarkUiState> = combine(
        _uiState,
        repository.getAllCategory(),
        bookmarksFlow,
    ) { state, categories, bookmarks ->
        state.copy(categories = categories, bookmarks = bookmarks)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BookmarkUiState()
    )

    fun updateExpanded(value: Boolean) {
        _uiState.update { it.copy(expanded = value) }
    }

    fun updateSelectedCategory(value: String) {
        _uiState.update { it.copy(selectedCategory = value) }
    }

    fun updateEditingBookmark(value: Bookmark?) {
        _uiState.update { it.copy(editingBookmark = value) }
    }

    fun updateShowNewBookmark(value: Boolean) {
        _uiState.update { it.copy(showNewBookmark = value) }
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
