package com.illusion.checkfirm.feature.bookmark.impl

import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.Category

data class BookmarkUiState(
    val expanded: Boolean = false,
    val selectedCategory: String = "",
    val categories: List<Category> = emptyList(),
    val bookmarks: List<Bookmark> = emptyList(),
    val editingBookmark: Bookmark? = null,
    val showNewBookmark: Boolean = false,
)
