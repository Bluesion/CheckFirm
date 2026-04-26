package com.illusion.checkfirm.feature.bookmark.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Category
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
    private val repository: BCRepository
) : ViewModel() {

    private val _expanded = MutableStateFlow(false)
    private val _selectedCategory = MutableStateFlow("All")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _bookmarksFlow = _selectedCategory.flatMapLatest { category ->
        if (category == "All") {
            repository.getAllBookmark("date", true)
        } else {
            repository.getBookmarkByCategory("date", true, category)
        }
    }

    private data class UiFlags(
        val selectedTab: Int = 0,
        val editingBookmark: Bookmark? = null,
        val showNewBookmark: Boolean = false,
        val editingCategory: Category? = null,
        val showNewCategory: Boolean = false,
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
            editingCategory = flags.editingCategory,
            showNewCategory = flags.showNewCategory,
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

    fun updateEditingCategory(value: Category?) {
        _uiFlags.update { it.copy(editingCategory = value) }
    }

    fun updateShowNewCategory(value: Boolean) {
        _uiFlags.update { it.copy(showNewCategory = value) }
    }

    fun addBookmark(bookmark: Bookmark) {
        viewModelScope.launch { repository.addBookmark(bookmark) }
    }

    fun editBookmark(bookmark: Bookmark) {
        viewModelScope.launch { repository.editBookmark(bookmark) }
    }

    fun deleteBookmark(deviceModel: String) {
        viewModelScope.launch { repository.deleteBookmark(deviceModel) }
    }

    fun addCategory(category: Category) {
        viewModelScope.launch { repository.addCategory(category) }
    }

    fun editCategory(category: Category) {
        viewModelScope.launch { repository.editCategory(category) }
    }

    fun deleteCategory(name: String) {
        viewModelScope.launch { repository.deleteCategory(name) }
    }
}
