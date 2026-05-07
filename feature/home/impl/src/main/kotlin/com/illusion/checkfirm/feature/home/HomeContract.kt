package com.illusion.checkfirm.feature.home

import com.illusion.checkfirm.core.preference.api.Preference
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Category
import com.illusion.checkfirm.domain.model.SearchResult

data class HomeUiState(
    val preference: Preference = Preference(),
    val categories: List<Category> = emptyList(),
    val bookmarks: List<Bookmark> = emptyList(),
    val selectedCategory: String = "",
    val showCategoryDialog: Boolean = false,
    val results: List<SearchResult> = emptyList(),
    val resultState: ResultState = ResultState.Idle,
    val openedDialog: SearchResult? = null,
)

sealed interface ResultState {
    /** No search has been kicked off. */
    data object Idle : ResultState

    /** A search is in flight. */
    data object Loading : ResultState

    /** Last search hit a network failure (no devices loaded). */
    data object NetworkError : ResultState

    /** Last search returned no usable firmware data. */
    data object Empty : ResultState

    /** Last search succeeded — see [HomeUiState.results] for the data. */
    data object Success : ResultState
}
