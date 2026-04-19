package com.illusion.checkfirm.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
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
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
                actions = {
                    Row {
                        IconButton(onClick = onSearchIconClick) {
                            Icon(Icons.Rounded.Search, contentDescription = "Search")
                        }
                        IconButton(onClick = onBookmarkIconClick) {
                            Icon(Icons.Rounded.Bookmark, contentDescription = "Bookmarks")
                        }
                        IconButton(onClick = onPreferenceIconClick) {
                            Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding() + 12.dp,
                bottom = innerPadding.calculateBottomPadding() + 12.dp,
                start = 12.dp,
                end = 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState.preference.isQuickSearchBarEnabled && uiState.categories.isNotEmpty()) {
                item(key = "quick_search_bar") {
                    QuickSearchBar(
                        selected = uiState.selectedCategory,
                        categories = uiState.categories.map { it.name },
                        onChipClick = onCategoryChipClick
                    )
                }
            }

            item(key = "hello_dashboard") {
                HelloDashboard(
                    onSearchClick = onSearchIconClick,
                    onBookmarkClick = onBookmarkIconClick,
                    onWelcomeSearchClick = onWelcomeSearchClick,
                    onInfoCatcherClick = onInfoCatcherClick,
                    onSettingsClick = onPreferenceIconClick
                )
            }
        }
    }
}

@Composable
private fun QuickSearchBar(
    selected: String,
    categories: List<String>,
    onChipClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Category",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        val all = listOf("All") + categories
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(count = all.size, key = { all[it] }) { index ->
                val chip = all[index]
                FilterChip(
                    selected = chip == selected,
                    onClick = { onChipClick(chip) },
                    label = { Text(chip) }
                )
            }
        }
    }
}

@Composable
private fun HelloDashboard(
    onSearchClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onWelcomeSearchClick: () -> Unit,
    onInfoCatcherClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.CheckCircle,
            contentDescription = "App Icon",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.main_hello_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.main_hello_description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column {
                HelloShortcutItem(
                    icon = Icons.Rounded.Search,
                    title = stringResource(R.string.search),
                    description = stringResource(R.string.main_hello_search_description),
                    onClick = onSearchClick
                )
                HorizontalDivider()
                HelloShortcutItem(
                    icon = Icons.Rounded.Bookmark,
                    title = stringResource(R.string.bookmark),
                    description = stringResource(R.string.main_hello_bookmark_description),
                    onClick = onBookmarkClick
                )
                HorizontalDivider()
                HelloShortcutItem(
                    icon = Icons.Rounded.Face,
                    title = stringResource(R.string.welcome_search),
                    description = stringResource(R.string.settings_welcome_search_description),
                    onClick = onWelcomeSearchClick
                )
                HorizontalDivider()
                HelloShortcutItem(
                    icon = Icons.Rounded.Info,
                    title = stringResource(R.string.info_catcher),
                    description = stringResource(R.string.main_hello_info_catcher_description),
                    onClick = onInfoCatcherClick
                )
                HorizontalDivider()
                HelloShortcutItem(
                    icon = Icons.Rounded.Settings,
                    title = stringResource(R.string.settings),
                    description = stringResource(R.string.main_hello_settings_description),
                    onClick = onSettingsClick
                )
            }
        }
    }
}

@Composable
private fun HelloShortcutItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
