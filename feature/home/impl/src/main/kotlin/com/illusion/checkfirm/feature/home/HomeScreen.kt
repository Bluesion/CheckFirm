package com.illusion.checkfirm.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.feature.home.component.HelloDashboard
import com.illusion.checkfirm.feature.home.component.QuickSearchBar

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onSearchIconClick: () -> Unit,
    onBookmarkIconClick: () -> Unit,
    onPreferenceIconClick: () -> Unit,
    onWelcomeSearchClick: () -> Unit,
    onInfoCatcherClick: () -> Unit,
    onCategoryChipClick: (String) -> Unit,
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
            if (uiState.preference.isQuickSearchBarEnabled && uiState.categories.isNotEmpty()) {
                item("quick_search_bar") {
                    QuickSearchBar(
                        selected = uiState.selectedCategory,
                        categories = uiState.categories.map { it.name },
                        onChipClick = onCategoryChipClick,
                    )
                }
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
}

@ScreenPreview
@Composable
private fun HomeScreenPreview() {
    CheckFirmTheme {
        Surface {
            HomeScreen(
                uiState = HomeUiState(),
                onSearchIconClick = {},
                onBookmarkIconClick = {},
                onPreferenceIconClick = {},
                onWelcomeSearchClick = {},
                onInfoCatcherClick = {},
                onCategoryChipClick = {},
            )
        }
    }
}
