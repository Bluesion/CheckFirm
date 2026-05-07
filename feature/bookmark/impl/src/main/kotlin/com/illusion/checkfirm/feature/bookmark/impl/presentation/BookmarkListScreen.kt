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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.component.OneTab
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Category
import com.illusion.checkfirm.domain.model.Device

@Composable
fun BookmarkListScreen(
    uiState: BookmarkListUiState = BookmarkListUiState(),
    onExpandedChange: (Boolean) -> Unit = {},
    onCategoryChange: (String) -> Unit = {},
    onNavigationIconClick: () -> Unit,
    onSelectedTabChange: (Int) -> Unit = {},
    onEditingBookmarkChange: (Bookmark?) -> Unit = {},
    onShowNewBookmarkChange: (Boolean) -> Unit = {},
    onAddBookmark: (Bookmark) -> Unit = {},
    onEditBookmark: (Bookmark) -> Unit = {},
    onDeleteBookmark: (Device) -> Unit = {},
    onDeleteCategory: (String) -> Unit = {},
    onItemClick: (Bookmark) -> Unit = {},
    onNewCategoryClick: () -> Unit = {},
    onEditCategoryClick: (Category) -> Unit = {},
) {
    val isBookmarkTab = uiState.selectedTab == 0
    val count = if (isBookmarkTab) uiState.bookmarks.size else uiState.categories.size
    val subtitle = pluralStringResource(
        id = if (isBookmarkTab) R.plurals.bookmark_subtitle else R.plurals.category_subtitle,
        count = count,
        count,
    )

    OneScaffold(
        title = stringResource(R.string.bookmark),
        subTitle = subtitle,
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
                    if (isBookmarkTab) onShowNewBookmarkChange(true)
                    else onNewCategoryClick()
                },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding()),
        ) {
            OneTab(
                titles = listOf(
                    stringResource(R.string.bookmark),
                    stringResource(R.string.category),
                ),
                selectedTabIndex = uiState.selectedTab,
                onTabSelected = onSelectedTabChange,
            )

            when (uiState.selectedTab) {
                0 -> BookmarkContent(
                    uiState = uiState,
                    onExpandedChange = onExpandedChange,
                    onCategoryChange = onCategoryChange,
                    onItemClick = onItemClick,
                    onEditClick = { onEditingBookmarkChange(it) },
                    onDeleteClick = { onDeleteBookmark(it.device) },
                )

                1 -> CategoryContent(
                    categories = uiState.categories,
                    onEditClick = { onEditCategoryClick(it) },
                    onDeleteClick = { onDeleteCategory(it.name) },
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
            },
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
            },
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
