package com.illusion.checkfirm.feature.bookmark.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BookmarkRoute(
    onNavigationIconClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onEditCategory: (String) -> Unit,
    viewModel: BookmarkViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BookmarkScreen(
        uiState = uiState,
        onCategoryClick = onCategoryClick,
        onEditCategory = onEditCategory,
        onDeleteCategory = viewModel::deleteCategory,
        onExpandedChange = viewModel::updateExpanded,
        onCategoryChange = viewModel::updateSelectedCategory,
        onEditingBookmarkChange = viewModel::updateEditingBookmark,
        onShowNewBookmarkChange = viewModel::updateShowNewBookmark,
        onAddBookmark = viewModel::addBookmark,
        onEditBookmark = viewModel::editBookmark,
        onDeleteBookmark = viewModel::deleteBookmark,
        onItemClick = { bookmark ->
            scope.launch {
                viewModel.emitItemPicked(bookmark)
                onNavigationIconClick()
            }
        },
        onNavigationIconClick = onNavigationIconClick,
    )
}
