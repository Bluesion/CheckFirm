package com.illusion.checkfirm.feature.home

import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Category
import com.illusion.checkfirm.domain.model.Preference

data class HomeUiState(
    val preference: Preference = Preference(),
    val categories: List<Category> = emptyList(),
    val bookmarks: List<Bookmark> = emptyList(),
    val selectedCategory: String = "All",
)
