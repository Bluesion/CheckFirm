package com.illusion.checkfirm.feature.bookmark.impl.presentation

import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Category

data class BookmarkListUiState(
    val expanded: Boolean = false,
    /** Empty == All. Screens substitute R.string.category_all when displaying. */
    val selectedCategory: String = "",
    val categories: List<Category> = emptyList(),
    val bookmarks: List<Bookmark> = emptyList(),
    val selectedTab: Int = 0,
    val editingBookmark: Bookmark? = null,
    val showNewBookmark: Boolean = false,
)
