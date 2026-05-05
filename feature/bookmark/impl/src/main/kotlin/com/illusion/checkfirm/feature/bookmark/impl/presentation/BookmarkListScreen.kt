package com.illusion.checkfirm.feature.bookmark.impl.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Category

@Composable
fun BookmarkListScreen(
    uiState: BookmarkListUiState = BookmarkListUiState(),
    onExpandedChange: (Boolean) -> Unit = {},
    onCategoryChange: (String) -> Unit = {},
    onNavigationIconClick: () -> Unit,
    onSelectedTabChange: (Int) -> Unit = {},
    onEditingBookmarkChange: (Bookmark?) -> Unit = {},
    onShowNewBookmarkChange: (Boolean) -> Unit = {},
    onEditingCategoryChange: (Category?) -> Unit = {},
    onShowNewCategoryChange: (Boolean) -> Unit = {},
    onAddBookmark: (Bookmark) -> Unit = {},
    onEditBookmark: (Bookmark) -> Unit = {},
    onDeleteBookmark: (String) -> Unit = {},
    onAddCategory: (Category) -> Unit = {},
    onEditCategory: (Category) -> Unit = {},
    onDeleteCategory: (String) -> Unit = {},
) {
    OneScaffold(
        title = stringResource(R.string.bookmark),
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = OneIcons.IcBack,
                    contentDescription = null,
                    tint = CheckFirmTheme.colors.toolbarIconTint,
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (uiState.selectedTab == 0) onShowNewBookmarkChange(true)
                    else onShowNewCategoryChange(true)
                },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            TabRow(selectedTabIndex = uiState.selectedTab) {
                Tab(
                    selected = uiState.selectedTab == 0,
                    onClick = { onSelectedTabChange(0) },
                    text = { Text(text = stringResource(R.string.bookmark)) }
                )
                Tab(
                    selected = uiState.selectedTab == 1,
                    onClick = { onSelectedTabChange(1) },
                    text = { Text(text = stringResource(R.string.category)) }
                )
            }

            when (uiState.selectedTab) {
                0 -> BookmarkContent(
                    uiState = uiState,
                    onExpandedChange = onExpandedChange,
                    onCategoryChange = onCategoryChange,
                    onEditClick = { onEditingBookmarkChange(it) },
                    onDeleteClick = { onDeleteBookmark(it.device.model) }
                )

                1 -> CategoryContent(
                    categories = uiState.categories,
                    onEditClick = { onEditingCategoryChange(it) },
                    onDeleteClick = { onDeleteCategory(it.name) }
                )
            }
        }
    }

    val categoryNames =
        listOf(stringResource(R.string.category_all)) + uiState.categories.map { it.name }

    if (uiState.showNewBookmark) {
        BookmarkDialog(
            categories = categoryNames.drop(1),
            onDismiss = { onShowNewBookmarkChange(false) },
            onConfirm = {
                onAddBookmark(it)
                onShowNewBookmarkChange(false)
            }
        )
    }

    uiState.editingBookmark?.let { bm ->
        BookmarkDialog(
            initial = bm,
            categories = categoryNames.drop(1),
            onDismiss = { onEditingBookmarkChange(null) },
            onConfirm = {
                onEditBookmark(it)
                onEditingBookmarkChange(null)
            }
        )
    }

    if (uiState.showNewCategory) {
        CategoryDialog(
            initial = null,
            onDismiss = { onShowNewCategoryChange(false) },
            onConfirm = {
                onAddCategory(Category(it))
                onShowNewCategoryChange(false)
            }
        )
    }

    uiState.editingCategory?.let { cat ->
        CategoryDialog(
            initial = cat.name,
            onDismiss = { onEditingCategoryChange(null) },
            onConfirm = {
                onEditCategory(Category(it))
                onEditingCategoryChange(null)
            }
        )
    }
}

@ScreenPreview
@Composable
private fun BookmarkListScreenPreview() {
    CheckFirmTheme {
        Surface {
            BookmarkListScreen(onNavigationIconClick = {})
        }
    }
}
