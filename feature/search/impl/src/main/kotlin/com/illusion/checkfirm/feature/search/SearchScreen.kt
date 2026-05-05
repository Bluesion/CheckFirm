package com.illusion.checkfirm.feature.search

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
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
        sheetContent = {
            SearchBottomSheetContent(
                searchList = uiState.searchList,
                onRemove = onRemoveFromSearchList,
                onSearchClick = onSearchClick
            )
        },
        topBar = {
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
                            "Duplicated device"
                        )

                        SearchValidationResult.INVALID_DEVICE -> toast(
                            context,
                            context.getString(R.string.check_device)
                        )

                        SearchValidationResult.MAX_SEARCH_CAPACITY_EXCEEDED -> toast(
                            context,
                            context.getString(R.string.multi_search_limit)
                        )

                        SearchValidationResult.SUCCESS -> Unit
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = uiState.selectedTabIndex) {
                Tab(
                    selected = uiState.selectedTabIndex == 0,
                    onClick = { onTabIndexChange(0) },
                    text = { Text(text = stringResource(R.string.bookmark)) }
                )
                Tab(
                    selected = uiState.selectedTabIndex == 1,
                    onClick = { onTabIndexChange(1) },
                    text = { Text(text = stringResource(R.string.search_history)) }
                )
            }

            when (uiState.selectedTabIndex) {
                0 -> BookmarkTab(
                    bookmarks = bookmarks,
                    onClick = {
                        when (onDeviceClick(it)) {
                            SearchValidationResult.DUPLICATED_DEVICE -> toast(
                                context,
                                "Already added"
                            )

                            SearchValidationResult.MAX_SEARCH_CAPACITY_EXCEEDED -> toast(
                                context,
                                context.getString(R.string.multi_search_limit)
                            )

                            else -> Unit
                        }
                    }
                )

                else -> HistoryTab(
                    historyList = historyList,
                    onDeleteAll = onDeleteAllHistory,
                    onDelete = onDeleteHistory,
                    onClick = {
                        when (onDeviceClick(it)) {
                            SearchValidationResult.DUPLICATED_DEVICE -> toast(
                                context,
                                "Already added"
                            )

                            SearchValidationResult.MAX_SEARCH_CAPACITY_EXCEEDED -> toast(
                                context,
                                context.getString(R.string.multi_search_limit)
                            )

                            else -> Unit
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun BookmarkTab(
    bookmarks: List<Bookmark>,
    onClick: (Device) -> Unit,
) {
    if (bookmarks.isEmpty()) {
        EmptyMessage(text = stringResource(R.string.search_no_bookmark))
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = bookmarks, key = { it.device.model + it.device.csc }) { bm ->
            SearchResultRow(
                title = bm.name,
                subtitle = "${bm.device.model} / ${bm.device.csc}",
                onClick = { onClick(bm.device) }
            )
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = onDeleteAll,
                enabled = historyList.isNotEmpty()
            ) {
                Text(text = stringResource(R.string.search_history_delete_all))
            }
        }

        if (historyList.isEmpty()) {
            EmptyMessage(text = stringResource(R.string.search_no_history))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = historyList,
                    key = { "${it.device.model}${it.device.csc}${it.date.year}${it.date.month}${it.date.day}" }) { item ->
                    SearchResultRow(
                        title = "${item.device.model} / ${item.device.csc}",
                        subtitle = "${item.date.year}-${item.date.month}-${item.date.day}",
                        trailing = {
                            IconButton(onClick = { onDelete(item) }) {
                                Icon(Icons.Rounded.Delete, contentDescription = "Delete")
                            }
                        },
                        onClick = { onClick(item.device) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyMessage(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SearchResultRow(
    title: String,
    subtitle: String,
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            trailing?.invoke()
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onNavigationIconClick) {
            Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
        }

        Card(
            modifier = Modifier
                .weight(1f)
                .height(40.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = model,
                    onValueChange = onModelChange,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Next
                    ),
                    decorationBox = { innerTextField ->
                        if (model.isEmpty()) {
                            Text(
                                stringResource(R.string.model),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        innerTextField()
                    }
                )
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = csc,
                    onValueChange = { if (it.length <= 3) onCscChange(it) },
                    modifier = Modifier.width(60.dp),
                    singleLine = true,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Done
                    ),
                    decorationBox = { innerTextField ->
                        if (csc.isEmpty()) {
                            Text(
                                stringResource(R.string.csc),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }

        IconButton(onClick = onAddClick) {
            Icon(Icons.Rounded.Add, contentDescription = "Add")
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
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "(${searchList.size})",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = onSearchClick, enabled = searchList.isNotEmpty()) {
                Text(text = stringResource(R.string.search))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (searchList.isEmpty()) {
            Text(
                text = stringResource(R.string.search_list_empty),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                searchList.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${item.device.model} / ${item.device.csc}",
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { onRemove(item.device) }) {
                            Icon(Icons.Rounded.Close, contentDescription = "Remove")
                        }
                    }
                }
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
