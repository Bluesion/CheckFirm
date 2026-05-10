package com.illusion.checkfirm.feature.bookmark.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneNavButton
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.Device

@Composable
fun BookmarkScreen(
    uiState: BookmarkUiState,
    onExpandedChange: (Boolean) -> Unit,
    onCategoryChange: (String) -> Unit,
    onEditingBookmarkChange: (Bookmark?) -> Unit,
    onShowNewBookmarkChange: (Boolean) -> Unit,
    onAddBookmark: (Bookmark) -> Unit,
    onEditBookmark: (Bookmark) -> Unit,
    onDeleteBookmark: (Device) -> Unit,
    onItemClick: (Bookmark) -> Unit,
    onNavigationIconClick: () -> Unit,
) {
    OneScaffold(
        title = stringResource(R.string.bookmark),
        navigationIcon = {
            OneNavButton(
                icon = OneIcons.IcBack,
                onClick = onNavigationIconClick,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onShowNewBookmarkChange(true) },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add",
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding()),
        ) {
            BookmarkContent(
                uiState = uiState,
                onExpandedChange = onExpandedChange,
                onCategoryChange = onCategoryChange,
                onItemClick = onItemClick,
                onEditClick = { onEditingBookmarkChange(it) },
                onDeleteClick = { onDeleteBookmark(it.device) },
            )
        }
    }

    val categoryNames =
        listOf(stringResource(R.string.category_all)) + uiState.categories.map { it.name }

    if (uiState.showNewBookmark) {
        BookmarkDialog(
            categories = categoryNames,
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
            categories = categoryNames,
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
private fun BookmarkScreenPreview() {
    CheckFirmTheme {
        Surface {
            BookmarkScreen(
                uiState = BookmarkUiState(),
                onExpandedChange = {},
                onCategoryChange = {},
                onEditingBookmarkChange = {},
                onShowNewBookmarkChange = {},
                onAddBookmark = {},
                onEditBookmark = {},
                onDeleteBookmark = {},
                onItemClick = {},
                onNavigationIconClick = {},
            )
        }
    }
}
