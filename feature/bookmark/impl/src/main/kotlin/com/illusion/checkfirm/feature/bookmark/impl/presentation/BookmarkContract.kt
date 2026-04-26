package com.illusion.checkfirm.feature.bookmark.impl.presentation

import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Category

data class BookmarkListUiState(
    val expanded: Boolean = false,
    val selectedCategory: String = "All",
    val categories: List<Category> = emptyList(),
    val bookmarks: List<Bookmark> = emptyList(),
    val selectedTab: Int = 0,
    val editingBookmark: Bookmark? = null,
    val showNewBookmark: Boolean = false,
    val editingCategory: Category? = null,
    val showNewCategory: Boolean = false,
)
