package com.illusion.checkfirm.feature.bookmark.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Category
import com.illusion.checkfirm.domain.repository.BCRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookmarkListUiState(
    val expanded: Boolean = false,
    val selectedCategory: String = "All",
    val categories: List<Category> = emptyList(),
    val bookmarks: List<Bookmark> = emptyList()
)

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

    val uiState: StateFlow<BookmarkListUiState> = combine(
        _expanded,
        _selectedCategory,
        repository.getAllCategory(),
        _bookmarksFlow
    ) { expanded, selectedCategory, categories, bookmarks ->
        BookmarkListUiState(
            expanded = expanded,
            selectedCategory = selectedCategory,
            categories = categories,
            bookmarks = bookmarks
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
