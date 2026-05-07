package com.illusion.checkfirm.feature.bookmark.impl.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BookmarkRoute(
    onNavigationIconClick: () -> Unit,
    onNewCategory: () -> Unit,
    onEditCategory: (categoryName: String) -> Unit,
    viewModel: BookmarkListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BookmarkListScreen(
        uiState = uiState,
        onExpandedChange = viewModel::updateExpanded,
        onCategoryChange = viewModel::updateSelectedCategory,
        onNavigationIconClick = onNavigationIconClick,
        onSelectedTabChange = viewModel::updateSelectedTab,
        onEditingBookmarkChange = viewModel::updateEditingBookmark,
        onShowNewBookmarkChange = viewModel::updateShowNewBookmark,
        onAddBookmark = viewModel::addBookmark,
        onEditBookmark = viewModel::editBookmark,
        onDeleteBookmark = viewModel::deleteBookmark,
        onDeleteCategory = viewModel::deleteCategory,
        onItemClick = { bookmark ->
            viewModel.emitItemPicked(bookmark)
            onNavigationIconClick()
        },
        onNewCategoryClick = onNewCategory,
        onEditCategoryClick = { category -> onEditCategory(category.name) },
    )
}
