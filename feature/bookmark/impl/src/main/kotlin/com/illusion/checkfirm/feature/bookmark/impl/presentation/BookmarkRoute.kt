package com.illusion.checkfirm.feature.bookmark.impl.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BookmarkRoute(
    onNavigationIconClick: () -> Unit,
    viewModel: BookmarkListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BookmarkListScreen(
        uiState = uiState,
        onExpandedChange = viewModel::updateExpanded,
        onCategoryChange = viewModel::updateSelectedCategory,
        onNavigationIconClick = onNavigationIconClick,
        onAddBookmark = viewModel::addBookmark,
        onEditBookmark = viewModel::editBookmark,
        onDeleteBookmark = viewModel::deleteBookmark,
        onAddCategory = viewModel::addCategory,
        onEditCategory = viewModel::editCategory,
        onDeleteCategory = viewModel::deleteCategory,
    )
}
