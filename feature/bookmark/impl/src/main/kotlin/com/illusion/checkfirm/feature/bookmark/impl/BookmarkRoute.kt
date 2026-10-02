package com.illusion.checkfirm.feature.bookmark.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BookmarkRoute(
    onNavigationIconClick: () -> Unit,
    onCategoryClick: () -> Unit,
    viewModel: BookmarkViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BookmarkScreen(
        uiState = uiState,
        onCategoryClick = onCategoryClick,
        onExpandedChange = viewModel::updateExpanded,
        onCategoryChange = viewModel::updateSelectedCategory,
        onEditingBookmarkChange = viewModel::updateEditingBookmark,
        onShowNewBookmarkChange = viewModel::updateShowNewBookmark,
        onAddBookmark = viewModel::addBookmark,
        onEditBookmark = viewModel::editBookmark,
        onDeleteBookmark = viewModel::deleteBookmark,
        onItemClick = { bookmark ->
            viewModel.emitItemPicked(bookmark)
            onNavigationIconClick()
        },
        onNavigationIconClick = onNavigationIconClick,
    )
}
