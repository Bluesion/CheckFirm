package com.illusion.checkfirm.feature.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneCard
import com.illusion.checkfirm.core.designsystem.component.OneCheckbox
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Device
import com.illusion.checkfirm.domain.model.SearchHistory
import com.illusion.checkfirm.feature.search.util.SearchValidationResult

@Composable
fun SearchScreen(
    uiState: SearchUiState = SearchUiState(),
    historyList: List<SearchHistory> = emptyList(),
    bookmarks: List<Bookmark> = emptyList(),
    onModelChange: (String) -> Unit = {},
    onCscChange: (String) -> Unit = {},
    onTabIndexChange: (Int) -> Unit = {},
    onAddClick: () -> SearchValidationResult = { SearchValidationResult.SUCCESS },
    onDeviceClick: (Device) -> SearchValidationResult = { SearchValidationResult.SUCCESS },
    onRemoveFromSearchList: (Device) -> Unit = {},
    onDeleteHistory: (SearchHistory) -> Unit = {},
    onDeleteAllHistory: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onNavigationIconClick: () -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 96.dp,
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetDragHandle = null,
        sheetContent = {
            SearchBottomSheetContent(
                searchList = uiState.searchList,
                onRemove = onRemoveFromSearchList,
                onSearchClick = onSearchClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(top = 20.dp),
        ) {
            SearchTopBar(
                model = uiState.model,
                onModelChange = onModelChange,
                csc = uiState.csc,
                onCscChange = onCscChange,
                onNavigationIconClick = onNavigationIconClick,
                onAddClick = {
                    when (onAddClick()) {
                        SearchValidationResult.DUPLICATED_DEVICE -> toast(
                            context,
                            context.getString(R.string.search_duplicate_device),
                        )

                        SearchValidationResult.INVALID_DEVICE -> toast(
                            context,
                            context.getString(R.string.check_device),
                        )

                        SearchValidationResult.MAX_SEARCH_CAPACITY_EXCEEDED -> toast(
                            context,
                            context.getString(R.string.multi_search_limit),
                        )

                        SearchValidationResult.SUCCESS -> Unit
                    }
                },
            )

            Spacer(modifier = Modifier.height(12.dp))

            SubTabRow(
                titles = listOf(
                    stringResource(R.string.bookmark),
                    stringResource(R.string.search_history),
                ),
                selectedTabIndex = uiState.selectedTabIndex,
                onTabSelected = onTabIndexChange,
                modifier = Modifier.fillMaxWidth(),
            )

            OneCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                ) {
                    when (uiState.selectedTabIndex) {
                        0 -> BookmarkTab(
                            bookmarks = bookmarks,
                            onClick = {
                                when (onDeviceClick(it)) {
                                    SearchValidationResult.DUPLICATED_DEVICE -> toast(
                                        context,
                                        context.getString(R.string.search_duplicate_device),
                                    )

                                    SearchValidationResult.MAX_SEARCH_CAPACITY_EXCEEDED -> toast(
                                        context,
                                        context.getString(R.string.multi_search_limit),
                                    )

                                    else -> Unit
                                }
                            },
                        )

                        else -> HistoryTab(
                            historyList = historyList,
                            onDeleteAll = onDeleteAllHistory,
                            onDelete = onDeleteHistory,
                            onClick = {
                                when (onDeviceClick(it)) {
                                    SearchValidationResult.DUPLICATED_DEVICE -> toast(
                                        context,
                                        context.getString(R.string.search_duplicate_device),
                                    )

                                    SearchValidationResult.MAX_SEARCH_CAPACITY_EXCEEDED -> toast(
                                        context,
                                        context.getString(R.string.multi_search_limit),
                                    )

                                    else -> Unit
                                }
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SubTabRow(
    titles: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val tabWidth = maxWidth * 0.4f
        Row(modifier = Modifier.fillMaxWidth()) {
            titles.forEachIndexed { index, title ->
                val selected = selectedTabIndex == index
                val placement = when (index) {
                    0 -> Alignment.CenterStart
                    titles.lastIndex -> Alignment.CenterEnd
                    else -> Alignment.Center
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 0.dp),
                    contentAlignment = placement,
                ) {
                    SubTab(
                        title = title,
                        selected = selected,
                        onClick = { onTabSelected(index) },
                        modifier = Modifier.width(tabWidth),
                    )
                }
            }
        }
    }
}

@Composable
private fun SubTab(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val darkTheme = isSystemInDarkTheme()
    val selectedBg = if (darkTheme) Color(0xFF2D2D2D) else Color(0xFFE9E9EC)
    val unselectedBg = Color.Transparent
    val selectedText = if (darkTheme) Color(0xFFFFFFFF) else Color(0xFF000000)
    val unselectedText = if (darkTheme) Color(0xFFA9A9A9) else Color(0xFF636363)

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) selectedBg else unselectedBg,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = onClick,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = if (selected) selectedText else unselectedText,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            )
        }
    }
}

@Composable
private fun BookmarkTab(
    bookmarks: List<Bookmark>,
    onClick: (Device) -> Unit,
) {
    if (bookmarks.isEmpty()) {
        BookmarkEmptyState(onClick = { /* hint only */ })
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = bookmarks, key = { it.device.model + it.device.csc }) { bm ->
            BookmarkRow(
                name = bm.name,
                model = bm.device.model,
                csc = bm.device.csc,
                onClick = { onClick(bm.device) },
            )
        }
    }
}

@Composable
private fun BookmarkEmptyState(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.search_no_bookmark),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(26.dp),
        ) {
            Icon(
                imageVector = OneIcons.OneuiBookmark,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.search_add_bookmark))
        }
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(26.dp),
        ) {
            Icon(
                imageVector = OneIcons.OneuiSearch,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.suggestion_search_my_device))
        }
    }
}

@Composable
private fun BookmarkRow(
    name: String,
    model: String,
    csc: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(start = 12.dp, top = 12.dp, bottom = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$model${stringResource(R.string.default_symbol)}$csc",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
private fun HistoryTab(
    historyList: List<SearchHistory>,
    onDeleteAll: () -> Unit,
    onDelete: (SearchHistory) -> Unit,
    onClick: (Device) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(
                onClick = onDeleteAll,
                enabled = historyList.isNotEmpty(),
            ) {
                Text(text = stringResource(R.string.search_history_delete_all))
            }
        }

        if (historyList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.TopCenter,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.search_no_history),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { /* hint only */ },
                        shape = RoundedCornerShape(26.dp),
                    ) {
                        Icon(
                            imageVector = OneIcons.OneuiSearch,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(R.string.suggestion_search_my_device))
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                items(
                    items = historyList,
                    key = {
                        "${it.device.model}${it.device.csc}${it.date.year}${it.date.month}${it.date.day}"
                    },
                ) { item ->
                    HistoryRow(
                        history = item,
                        onClick = { onClick(item.device) },
                        onDelete = { onDelete(item) },
                    )
                }
            }
        }
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
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "${history.device.model} (${history.device.csc})",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 4.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${history.date.year}-${history.date.month.toString().padStart(2, '0')}-${
                history.date.day.toString().padStart(2, '0')
            }",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF7A7A7A),
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(onClick = onDelete),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = OneIcons.IcClear,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun SearchTopBar(
    model: String,
    onModelChange: (String) -> Unit,
    csc: String,
    onCscChange: (String) -> Unit,
    onNavigationIconClick: () -> Unit,
    onAddClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onNavigationIconClick,
            modifier = Modifier.size(56.dp),
        ) {
            Icon(
                imageVector = OneIcons.IcBack,
                contentDescription = stringResource(android.R.string.cancel),
                tint = CheckFirmTheme.colors.toolbarIconTint,
                modifier = Modifier.size(24.dp),
            )
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .height(36.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicTextField(
                    value = model,
                    onValueChange = onModelChange,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Next) },
                    ),
                    decorationBox = { innerTextField ->
                        if (model.isEmpty()) {
                            Text(
                                text = stringResource(R.string.model),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        innerTextField()
                    },
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant),
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = csc,
                    onValueChange = { if (it.length <= 3) onCscChange(it) },
                    modifier = Modifier.width(48.dp),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() },
                    ),
                    decorationBox = { innerTextField ->
                        if (csc.isEmpty()) {
                            Text(
                                text = stringResource(R.string.csc),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        innerTextField()
                    },
                )
            }
        }

        IconButton(
            onClick = onAddClick,
            modifier = Modifier.size(56.dp),
        ) {
            Icon(
                imageVector = OneIcons.OneuiAdd,
                contentDescription = stringResource(R.string.search_add_bookmark),
                tint = CheckFirmTheme.colors.toolbarIconTint,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Composable
private fun SearchBottomSheetContent(
    searchList: List<SearchDeviceItem>,
    onRemove: (Device) -> Unit,
    onSearchClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.15f)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.outlineVariant)
                .align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = pluralStringResource(
                    id = R.plurals.search_device_summary,
                    count = searchList.size,
                    searchList.size,
                ),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .padding(end = 16.dp),
            )
            Button(
                onClick = onSearchClick,
                enabled = searchList.isNotEmpty(),
                modifier = Modifier.height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF377BFF),
                    contentColor = MaterialTheme.colorScheme.surface,
                ),
            ) {
                Text(text = stringResource(R.string.search))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (searchList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.search_list_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                searchList.forEach { item ->
                    SearchDeviceRow(
                        item = item,
                        onRemove = { onRemove(item.device) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchDeviceRow(
    item: SearchDeviceItem,
    onRemove: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OneCheckbox(checked = item.isChecked, onCheckedChange = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "${item.device.model} (${item.device.csc})",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 4.dp),
        )
        if (item.additionalInfo.isNotBlank()) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = item.additionalInfo,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF7A7A7A),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (item.isDeleteButtonVisible) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable(onClick = onRemove),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = OneIcons.IcClear,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

private fun toast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@ScreenPreview
@Composable
private fun SearchScreenPreview() {
    CheckFirmTheme {
        Surface {
            SearchScreen(onNavigationIconClick = {})
        }
    }
}

