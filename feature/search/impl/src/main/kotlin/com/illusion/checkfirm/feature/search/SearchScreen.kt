package com.illusion.checkfirm.feature.search

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.base.bounceClick
import com.illusion.checkfirm.core.designsystem.component.OneCard
import com.illusion.checkfirm.core.designsystem.component.OneDivider
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneNavButton
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.Date
import com.illusion.checkfirm.core.domain.model.Device
import com.illusion.checkfirm.core.domain.model.SearchHistory
import com.illusion.checkfirm.feature.search.util.SearchValidationResult
import com.illusion.checkfirm.feature.search.R as FeatureR

private const val ALL_FILTER = ""

@Composable
fun SearchScreen(
    uiState: SearchUiState = SearchUiState(),
    historyList: List<SearchHistory> = emptyList(),
    bookmarks: List<Bookmark> = emptyList(),
    onModelChange: (String) -> Unit = {},
    onCscChange: (String) -> Unit = {},
    onAddClick: () -> SearchValidationResult = { SearchValidationResult.SUCCESS },
    onDeviceClick: (Device) -> SearchValidationResult = { SearchValidationResult.SUCCESS },
    onRemoveFromSearchList: (Device) -> Unit = {},
    onDeleteHistory: (SearchHistory) -> Unit = {},
    onDeleteAllHistory: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onNavigationIconClick: () -> Unit,
) {
    val context = LocalContext.current
    val handleResult: (SearchValidationResult) -> Unit = { result ->
        when (result) {
            SearchValidationResult.DUPLICATED_DEVICE ->
                toast(context, context.getString(FeatureR.string.search_duplicate_device))

            SearchValidationResult.INVALID_DEVICE ->
                toast(context, context.getString(R.string.check_device))

            SearchValidationResult.MAX_SEARCH_CAPACITY_EXCEEDED ->
                toast(context, context.getString(FeatureR.string.multi_search_limit))

            SearchValidationResult.SUCCESS -> Unit
        }
    }

    OneScaffold(
        expandable = false,
        navigationIcon = {
            OneNavButton(
                onClick = onNavigationIconClick,
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    imageVector = OneIcons.Back,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 12.dp,
                end = 12.dp,
                top = 12.dp,
                bottom = innerPadding.calculateBottomPadding() + 12.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(key = "new-search") {
                NewSearchCard(
                    model = uiState.model,
                    csc = uiState.csc,
                    searchList = uiState.searchList,
                    onModelChange = onModelChange,
                    onCscChange = onCscChange,
                    onAddClick = { handleResult(onAddClick()) },
                    onRemove = onRemoveFromSearchList,
                    onSearchClick = onSearchClick,
                )
            }
            item(key = "bookmarks") {
                BookmarksCard(
                    bookmarks = bookmarks,
                    onClick = { handleResult(onDeviceClick(it)) },
                )
            }
            item(key = "recent-history") {
                RecentHistoryCard(
                    historyList = historyList,
                    onDeleteAll = onDeleteAllHistory,
                    onDelete = onDeleteHistory,
                    onClick = { handleResult(onDeviceClick(it)) },
                )
            }
        }
    }
}

@Composable
private fun NewSearchCard(
    model: String,
    csc: String,
    searchList: List<SearchDeviceItem>,
    onModelChange: (String) -> Unit,
    onCscChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onRemove: (Device) -> Unit,
    onSearchClick: () -> Unit,
) {
    OneCard {
        SectionHeader(title = stringResource(FeatureR.string.search_new_search))
        OneDivider(modifier = Modifier.padding(horizontal = 20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            PillTextField(
                value = model,
                onValueChange = onModelChange,
                hint = stringResource(FeatureR.string.search_model_hint),
                modifier = Modifier.weight(2f),
                imeAction = ImeAction.Next,
                maxLength = 12,
            )
            PillTextField(
                value = csc,
                onValueChange = onCscChange,
                hint = stringResource(R.string.csc),
                modifier = Modifier.weight(1f),
                imeAction = ImeAction.Done,
                maxLength = 3,
            )
        }

        if (searchList.isNotEmpty()) {
            SelectedDeviceList(
                items = searchList,
                onRemove = onRemove,
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            PillIconButton(
                icon = OneIcons.Add,
                text = stringResource(FeatureR.string.search_add_comparison),
                onClick = onAddClick,
            )
        }

        SearchPrimaryButton(
            enabled = searchList.isNotEmpty(),
            onClick = onSearchClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
        )
    }
}

@Composable
private fun BookmarksCard(
    bookmarks: List<Bookmark>,
    onClick: (Device) -> Unit,
) {
    val categories = remember(bookmarks) {
        bookmarks.map { it.category }.filter { it.isNotBlank() }.distinct()
    }
    var selected by rememberSaveable { mutableStateOf(ALL_FILTER) }
    val filtered = remember(bookmarks, selected) {
        if (selected == ALL_FILTER) bookmarks else bookmarks.filter { it.category == selected }
    }

    OneCard {
        SectionHeader(
            title = stringResource(FeatureR.string.search_bookmarks),
            trailingIcon = OneIcons.Bookmark,
        )
        OneDivider(modifier = Modifier.padding(horizontal = 20.dp))

        if (categories.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item(key = ALL_FILTER) {
                    FilterChip(
                        text = stringResource(R.string.category_all),
                        selected = selected == ALL_FILTER,
                        onClick = { selected = ALL_FILTER },
                    )
                }
                items(items = categories, key = { it }) { category ->
                    FilterChip(
                        text = category,
                        selected = selected == category,
                        onClick = { selected = category },
                    )
                }
            }
        }

        if (filtered.isEmpty()) {
            EmptyState(text = stringResource(R.string.search_no_bookmark))
        } else {
            filtered.forEach { bookmark ->
                BookmarkRow(
                    bookmark = bookmark,
                    onClick = { onClick(bookmark.device) },
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun RecentHistoryCard(
    historyList: List<SearchHistory>,
    onDeleteAll: () -> Unit,
    onDelete: (SearchHistory) -> Unit,
    onClick: (Device) -> Unit,
) {
    OneCard {
        SectionHeader(
            title = stringResource(FeatureR.string.search_recent_history),
            trailing = {
                if (historyList.isNotEmpty()) {
                    Text(
                        text = stringResource(FeatureR.string.search_clear_all),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(percent = 50))
                            .bounceClick(onClick = onDeleteAll)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
            },
        )
        OneDivider(modifier = Modifier.padding(horizontal = 20.dp))

        if (historyList.isEmpty()) {
            EmptyState(text = stringResource(R.string.search_no_history))
        } else {
            historyList.forEach { item ->
                HistoryRow(
                    history = item,
                    onClick = { onClick(item.device) },
                    onDelete = { onDelete(item) },
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    trailingIcon: ImageVector? = null,
    trailing: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
        )
        when {
            trailing != null -> trailing()
            trailingIcon != null -> Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun PillTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done,
    maxLength: Int = Int.MAX_VALUE,
) {
    val focusManager = LocalFocusManager.current
    val textColor = MaterialTheme.colorScheme.onSurface
    val hintColor = MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(CheckFirmTheme.colors.searchAddButtonBackground)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        BasicTextField(
            value = value,
            onValueChange = { input -> if (input.length <= maxLength) onValueChange(input) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = textColor),
            cursorBrush = SolidColor(textColor),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                imeAction = imeAction,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Next) },
                onDone = { focusManager.clearFocus() },
            ),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodyMedium,
                        color = hintColor,
                    )
                }
                innerTextField()
            },
        )
    }
}

@Composable
private fun PillIconButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(CheckFirmTheme.colors.searchAddButtonBackground)
            .bounceClick(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun SearchPrimaryButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (enabled) {
        Color(0xFF0F4ABE)
    } else {
        CheckFirmTheme.colors.searchAddButtonBackground
    }
    val contentColor = if (enabled) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(percent = 50))
            .background(containerColor)
            .then(if (enabled) Modifier.bounceClick(onClick = onClick) else Modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = OneIcons.Search,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(R.string.search),
            style = MaterialTheme.typography.titleMedium,
            color = contentColor,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun FilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val container = if (selected) {
        Color(0xFF0F4ABE)
    } else {
        CheckFirmTheme.colors.searchAddButtonBackground
    }
    val content = if (selected) Color.White else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(percent = 50))
            .background(container)
            .bounceClick(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = content,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
        )
    }
}

@Composable
private fun BookmarkRow(
    bookmark: Bookmark,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(CheckFirmTheme.colors.searchAddButtonBackground),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = OneIcons.Device,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp),
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = bookmark.name.ifBlank { bookmark.device.model },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
            Text(
                text = bookmark.device.csc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Icon(
            imageVector = OneIcons.Bookmark,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun HistoryRow(
    history: SearchHistory,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = OneIcons.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "${history.device.model} / ${history.device.csc}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = formatDate(history.date),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .bounceClick(onClick = onDelete),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = OneIcons.Clear,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

@Composable
private fun SelectedDeviceList(
    items: List<SearchDeviceItem>,
    onRemove: (Device) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(percent = 50))
                    .background(CheckFirmTheme.colors.searchAddButtonBackground)
                    .padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${item.device.model} (${item.device.csc})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .bounceClick { onRemove(item.device) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = OneIcons.Clear,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

private fun formatDate(date: Date): String {
    val month = date.month.toString().padStart(2, '0')
    val day = date.day.toString().padStart(2, '0')
    return "${date.year}-$month-$day"
}

private fun toast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@ScreenPreview
@Composable
private fun SearchScreenPreview() {
    CheckFirmTheme {
        Surface {
            SearchScreen(
                uiState = SearchUiState(
                    model = "",
                    csc = "",
                    searchList = emptyList(),
                ),
                bookmarks = listOf(
                    Bookmark(
                        name = "S23 Ultra",
                        device = Device(model = "SM-S918B", csc = "EUX"),
                        category = "Phone",
                    ),
                    Bookmark(
                        name = "Tab S9",
                        device = Device(model = "SM-X710", csc = "KOO"),
                        category = "Tablet",
                    ),
                ),
                historyList = listOf(
                    SearchHistory(
                        device = Device(model = "SM-S918B", csc = "EUX"),
                        date = Date(2026, 5, 9),
                    ),
                    SearchHistory(
                        device = Device(model = "SM-X710", csc = "KOO"),
                        date = Date(2026, 5, 8),
                    ),
                    SearchHistory(
                        device = Device(model = "SM-F946B", csc = "SEK"),
                        date = Date(2026, 5, 1),
                    ),
                ),
                onNavigationIconClick = {},
            )
        }
    }
}
