package com.illusion.checkfirm.feature.bookmark.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
    initialTab: Int = 0,
    onExpandedChange: (Boolean) -> Unit,
    onCategoryChange: (String) -> Unit,
    onEditingBookmarkChange: (Bookmark?) -> Unit,
    onShowNewBookmarkChange: (Boolean) -> Unit,
    onAddBookmark: (Bookmark) -> Unit,
    onEditBookmark: (Bookmark) -> Unit,
    onDeleteBookmark: (Device) -> Unit,
    onItemClick: (Bookmark) -> Unit,
    onNavigationIconClick: () -> Unit,
    onCategoryClick: () -> Unit = {},
    onEditCategory: (String) -> Unit = {},
    onDeleteCategory: (String) -> Unit = {},
) {
    var tab by rememberSaveable { mutableIntStateOf(initialTab) }
    OneScaffold(
        title = stringResource(if (tab == 0) R.string.bookmark else R.string.category),
        navigationIcon = {
            OneNavButton(
                onClick = onNavigationIconClick,
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    imageVector = OneIcons.Back,
                    contentDescription = stringResource(com.illusion.checkfirm.core.designsystem.R.string.navigate_back),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp),
                )
            }
        },
        actions = {
            OneNavButton(onClick = { if (tab == 0) onShowNewBookmarkChange(true) else onCategoryClick() }, shape = CircleShape,
                modifier = Modifier.size(48.dp)) {
                Icon(Icons.Rounded.Add, contentDescription = stringResource(if (tab == 0) com.illusion.checkfirm.feature.bookmark.R.string.bookmark_new else R.string.category))
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                ),
        ) {
            androidx.compose.foundation.layout.Box(Modifier.weight(1f)) {
            if (tab == 0) BookmarkContent(
                uiState = uiState,
                onExpandedChange = onExpandedChange,
                onCategoryChange = onCategoryChange,
                onItemClick = onItemClick,
                onEditClick = { onEditingBookmarkChange(it) },
                onDeleteClick = { onDeleteBookmark(it.device) },
            )
            else LazyColumn(contentPadding = PaddingValues(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.categories, key = { it.id ?: it.name }) { category ->
                    com.illusion.checkfirm.core.designsystem.component.OneCard {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            TextButton(onClick = { onEditCategory(category.name) }, modifier = Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 14.dp, vertical = 13.dp)) {
                                Text(category.name, Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Clip)
                            }
                            IconButton(onClick = { onDeleteCategory(category.name) }) {
                                Icon(androidx.compose.ui.res.painterResource(R.drawable.oneui_ic_delete), stringResource(R.string.delete_item))
                            }
                        }
                    }
                }
            }
            }
            com.illusion.checkfirm.core.designsystem.component.OneTab(
                titles = listOf(stringResource(R.string.bookmark), stringResource(R.string.category)),
                selectedTabIndex = tab, onTabSelected = { tab = it },
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
