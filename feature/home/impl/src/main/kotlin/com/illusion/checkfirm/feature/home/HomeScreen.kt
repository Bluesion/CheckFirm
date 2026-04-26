
package com.illusion.checkfirm.feature.home

import androidx.compose.foundation.background
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneListCard
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.component.OneSettingsDivider
import com.illusion.checkfirm.core.designsystem.component.OneSettingsItem
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

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

@Composable
private fun QuickSearchBar(
    selected: String,
    categories: List<String>,
    onChipClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = OneIcons.IcCategory,
                contentDescription = stringResource(R.string.category),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp),
            )
        }
        Spacer(Modifier.width(8.dp))
        val all = listOf(stringResource(R.string.category_all)) + categories
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(count = all.size, key = { all[it] }) { idx ->
                val chip = all[idx]
                FilterChip(
                    selected = chip == selected,
                    onClick = { onChipClick(chip) },
                    label = { Text(chip) },
                    shape = CircleShape,
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
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = OneIcons.CheckfirmIcon,
            contentDescription = null,
            tint = androidx.compose.ui.graphics.Color.Unspecified,
            modifier = Modifier.size(64.dp),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.main_hello_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.main_hello_description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(24.dp))

        OneListCard {
            OneSettingsItem(
                title = stringResource(R.string.search),
                description = stringResource(R.string.main_hello_search_description),
                iconVector = OneIcons.OneuiSearch,
                onClick = onSearchClick,
            )
            OneSettingsDivider()
            OneSettingsItem(
                title = stringResource(R.string.bookmark),
                description = stringResource(R.string.main_hello_bookmark_description),
                iconVector = OneIcons.OneuiBookmark,
                onClick = onBookmarkClick,
            )
            OneSettingsDivider()
            OneSettingsItem(
                title = stringResource(R.string.welcome_search),
                description = stringResource(R.string.settings_welcome_search_description),
                iconVector = OneIcons.IcWelcomeSearch,
                onClick = onWelcomeSearchClick,
            )
            OneSettingsDivider()
            OneSettingsItem(
                title = stringResource(R.string.info_catcher),
                description = stringResource(R.string.main_hello_info_catcher_description),
                iconVector = OneIcons.IcInfoCatcher,
                onClick = onInfoCatcherClick,
            )
            OneSettingsDivider()
            OneSettingsItem(
                title = stringResource(R.string.settings),
                description = stringResource(R.string.main_hello_settings_description),
                iconVector = OneIcons.OneuiSettings,
                onClick = onSettingsClick,
            )
        }
    }
}
