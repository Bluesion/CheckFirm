package com.illusion.checkfirm.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.SearchResult
import com.illusion.checkfirm.feature.home.component.HelloDashboard
import com.illusion.checkfirm.feature.home.component.HomeErrorState
import com.illusion.checkfirm.feature.home.component.HomeFirmwareDialog
import com.illusion.checkfirm.feature.home.component.HomeResultCard
import com.illusion.checkfirm.feature.home.component.QuickSearchBar

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    visibleBookmarks: List<Bookmark>,
    onSearchIconClick: () -> Unit,
    onBookmarkIconClick: () -> Unit,
    onPreferenceIconClick: () -> Unit,
    onWelcomeSearchClick: () -> Unit,
    onInfoCatcherClick: () -> Unit,
    onCategoryIconClick: () -> Unit,
    onCategoryPick: (String) -> Unit,
    onBookmarkChipClick: (Bookmark) -> Unit,
    onCategoryDialogDismiss: () -> Unit,
    onResultClick: (SearchResult) -> Unit,
    onResultDismiss: () -> Unit,
    onCopy: (String) -> Unit,
    onOpenOfficialDoc: (SearchResult) -> Unit,
    onOpenSherlock: (SearchResult) -> Unit,
    onOpenReport: (SearchResult) -> Unit,
    onOpenFirmwareManual: () -> Unit,
) {
    OneScaffold(
        title = stringResource(R.string.app_name),
        actions = {
            IconButton(onClick = onSearchIconClick) {
                Icon(
                    imageVector = OneIcons.OneuiSearch,
                    contentDescription = stringResource(R.string.search),
                    tint = CheckFirmTheme.colors.toolbarIconTint,
                )
            }
            IconButton(onClick = onBookmarkIconClick) {
                Icon(
                    imageVector = OneIcons.OneuiBookmark,
                    contentDescription = stringResource(R.string.bookmark),
                    tint = CheckFirmTheme.colors.toolbarIconTint,
                )
            }
            IconButton(onClick = onPreferenceIconClick) {
                Icon(
                    imageVector = OneIcons.OneuiSettings,
                    contentDescription = stringResource(R.string.settings),
                    tint = CheckFirmTheme.colors.toolbarIconTint,
                )
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                top = 0.dp,
                bottom = innerPadding.calculateBottomPadding() + 24.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (uiState.preference.isQuickSearchBarEnabled && visibleBookmarks.isNotEmpty()) {
                item("quick_search_bar") {
                    QuickSearchBar(
                        bookmarks = visibleBookmarks,
                        onCategoryIconClick = onCategoryIconClick,
                        onBookmarkClick = onBookmarkChipClick,
                    )
                }
            }

            when (uiState.resultState) {
                ResultState.Loading -> item("loading") {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }

                ResultState.NetworkError, ResultState.Empty -> item("error_state") {
                    HomeErrorState(state = uiState.resultState)
                }

                ResultState.Success -> {
                    items(
                        count = uiState.results.size,
                        key = { uiState.results[it].device.toString() },
                    ) { idx ->
                        val result = uiState.results[idx]
                        HomeResultCard(
                            result = result,
                            onCardClick = { onResultClick(result) },
                        )
                    }
                }

                ResultState.Idle -> {}
            }

            item("hello_dashboard") {
                HelloDashboard(
                    onSearchClick = onSearchIconClick,
                    onBookmarkClick = onBookmarkIconClick,
                    onWelcomeSearchClick = onWelcomeSearchClick,
                    onInfoCatcherClick = onInfoCatcherClick,
                    onSettingsClick = onPreferenceIconClick,
                )
            }
        }
    }

    if (uiState.showCategoryDialog) {
        CategoryDialog(
            selected = uiState.selectedCategory.ifBlank { stringResource(R.string.category_all) },
            categories = uiState.categories.map { it.name },
            onDismiss = onCategoryDialogDismiss,
            onCategoryPick = onCategoryPick,
        )
    }

    uiState.openedDialog?.let { result ->
        HomeFirmwareDialog(
            result = result,
            onDismiss = onResultDismiss,
            onCopy = onCopy,
            onOpenOfficialDoc = { onOpenOfficialDoc(result) },
            onOpenSherlock = { onOpenSherlock(result) },
            onOpenReport = { onOpenReport(result) },
            onOpenFirmwareManual = onOpenFirmwareManual,
        )
    }
}

@ScreenPreview
@Composable
private fun HomeScreenPreview() {
    CheckFirmTheme {
        Surface {
            HomeScreen(
                uiState = HomeUiState(),
                visibleBookmarks = emptyList(),
                onSearchIconClick = {},
                onBookmarkIconClick = {},
                onPreferenceIconClick = {},
                onWelcomeSearchClick = {},
                onInfoCatcherClick = {},
                onCategoryIconClick = {},
                onCategoryPick = {},
                onBookmarkChipClick = {},
                onCategoryDialogDismiss = {},
                onResultClick = {},
                onResultDismiss = {},
                onCopy = {},
                onOpenOfficialDoc = {},
                onOpenSherlock = {},
                onOpenReport = {},
                onOpenFirmwareManual = {},
            )
        }
    }
}
