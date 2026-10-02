package com.illusion.checkfirm.feature.search

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.*
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.domain.model.*
import com.illusion.checkfirm.feature.search.util.SearchValidationResult
import com.illusion.checkfirm.feature.search.R as FeatureR

/** Search layout and selection controls matching the XML app. */
@Composable
fun SearchScreen(
    uiState: SearchUiState = SearchUiState(),
    categories: List<String> = emptyList(),
    onAddBookmarkClick: () -> Unit = {},
    onSearchThisDevice: () -> SearchValidationResult = { SearchValidationResult.SUCCESS },
    historyList: List<SearchHistory> = emptyList(), bookmarks: List<Bookmark> = emptyList(),
    onModelChange: (String) -> Unit = {}, onCscChange: (String) -> Unit = {},
    onAddClick: () -> SearchValidationResult = { SearchValidationResult.SUCCESS },
    onDeviceClick: (Device) -> SearchValidationResult = { SearchValidationResult.SUCCESS },
    onRemoveFromSearchList: (Device) -> Unit = {}, onDeleteHistory: (SearchHistory) -> Unit = {},
    onDeleteAllHistory: () -> Unit = {}, onSearchClick: () -> Unit = {}, onNavigationIconClick: () -> Unit,
) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var category by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val focus = LocalFocusManager.current
    fun validate(result: SearchValidationResult) {
        val message = when (result) {
            SearchValidationResult.DUPLICATED_DEVICE -> FeatureR.string.search_duplicate_device
            SearchValidationResult.INVALID_DEVICE -> R.string.check_device
            SearchValidationResult.MAX_SEARCH_CAPACITY_EXCEEDED -> FeatureR.string.multi_search_limit
            SearchValidationResult.SUCCESS -> null
        }
        message?.let { Toast.makeText(context, context.getString(it), Toast.LENGTH_SHORT).show() }
    }
    CompositionLocalProvider(LocalOneToolbarButtonBackgroundAlpha provides { 0f }) {
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
        .windowInsetsPadding(WindowInsets.statusBars).imePadding().navigationBarsPadding()) {
        Row(Modifier.fillMaxWidth().padding(top = 20.dp).height(56.dp), verticalAlignment = Alignment.CenterVertically) {
            OneNavButton(onNavigationIconClick, CircleShape, Modifier.size(56.dp)) {
                Icon(OneIcons.Back, stringResource(R.string.navigate_back))
            }
            Row(Modifier.weight(1f).height(36.dp).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp)).padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                SearchInput(uiState.model, onModelChange, stringResource(R.string.model), Modifier.weight(1f), ImeAction.Next) { focus.moveFocus(androidx.compose.ui.focus.FocusDirection.Next) }
                VerticalDivider(Modifier.height(24.dp).padding(horizontal = 8.dp))
                SearchInput(uiState.csc, onCscChange, stringResource(R.string.csc), Modifier.weight(1f), ImeAction.Done) { validate(onAddClick()); focus.clearFocus() }
            }
            OneNavButton({ validate(onAddClick()); focus.clearFocus() }, CircleShape, Modifier.size(56.dp)) {
                Icon(OneIcons.Add, stringResource(FeatureR.string.search_add_device))
            }
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = 28.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            listOf(stringResource(R.string.bookmark), stringResource(FeatureR.string.search_history)).forEachIndexed { index, title ->
                TextButton(onClick = { tab = index }, modifier = Modifier.weight(1f), colors = ButtonDefaults.textButtonColors(
                    containerColor = if (tab == index) CheckFirmTheme.colors.searchAddButtonBackground else androidx.compose.ui.graphics.Color.Transparent,
                    contentColor = if (tab == index) MaterialTheme.colorScheme.onSurface else CheckFirmTheme.colors.settingsDescription)) {
                    Text(title, style = MaterialTheme.typography.bodyMedium, color = if (tab == index) MaterialTheme.colorScheme.onSurface else CheckFirmTheme.colors.settingsDescription)
                }
            }
        }
        OneCard(Modifier.weight(1f).fillMaxWidth().padding(horizontal = 12.dp)) {
            if (tab == 0) {
                val all = stringResource(R.string.category_all)
                if (categories.isNotEmpty()) OneSpinner(listOf(all) + categories, category.ifBlank { all }, { category = if (it == all) "" else it }, Modifier.padding(12.dp))
                val visible = bookmarks.filter { category.isBlank() || it.category == category }
                if (visible.isEmpty()) SearchEmptyState(stringResource(R.string.search_no_bookmark), onAddBookmarkClick) { validate(onSearchThisDevice()) }
                else LazyColumn {
                    items(visible, key = { it.id ?: "${it.name}:${it.device}" }) { bookmark ->
                        val selected = uiState.searchList.any { it.device == bookmark.device }
                        Row(Modifier.fillMaxWidth().toggleable(selected, role = Role.Checkbox) { validate(onDeviceClick(bookmark.device)) }.padding(horizontal = 16.dp, vertical = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                            OneCheckbox(selected, { validate(onDeviceClick(bookmark.device)) }, Modifier.clearAndSetSemantics {})
                            Spacer(Modifier.width(8.dp))
                            FlowRow(maxLines = 1, horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.Center) {
                                Text(bookmark.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                                Text("${bookmark.device.model} · ${bookmark.device.csc}", style = MaterialTheme.typography.bodySmall, color = CheckFirmTheme.colors.settingsDescription)
                            }
                        }
                    }
                }
            } else {
                if (historyList.isNotEmpty()) TextButton(onDeleteAllHistory, Modifier.align(Alignment.End)) { Text(stringResource(R.string.delete_item)) }
                if (historyList.isEmpty()) SearchEmptyState(stringResource(R.string.search_no_history), null) { validate(onSearchThisDevice()) }
                else LazyColumn {
                    items(historyList, key = { it.device.toString() }) { history ->
                        Row(Modifier.fillMaxWidth().clickable { validate(onDeviceClick(history.device)) }.padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f).padding(vertical = 12.dp)) {
                                Text("${history.device.model} (${history.device.csc})")
                                Text("%04d-%02d-%02d".format(history.date.year, history.date.month, history.date.day), style = MaterialTheme.typography.bodySmall, color = CheckFirmTheme.colors.settingsDescription)
                            }
                            IconButton({ onDeleteHistory(history) }) { Icon(OneIcons.Clear, stringResource(R.string.delete_item)) }
                        }
                    }
                }
            }
        }
        OneCard(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)) {
                val selectedDevicesLabel = stringResource(FeatureR.string.search_selected_devices)
                Box(Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(0.15f).height(4.dp).clickable { expanded = !expanded }.semantics { contentDescription = selectedDevicesLabel }) {
                    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.outlineVariant, CircleShape))
                }
                if (expanded) uiState.searchList.forEach { item ->
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("${item.device.model} (${item.device.csc})", Modifier.weight(1f))
                        IconButton({ onRemoveFromSearchList(item.device) }) { Icon(OneIcons.Clear, stringResource(R.string.delete_item)) }
                    }
                }
                Row(Modifier.fillMaxWidth().padding(top = 4.dp).height(48.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(FeatureR.string.search_device_count, uiState.searchList.size), Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = { focus.clearFocus(); onSearchClick() }, colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF377BFF), contentColor = MaterialTheme.colorScheme.surface)) { Text(stringResource(R.string.search)) }
                }
            }
        }
    }
    }
}

@Composable
private fun SearchEmptyState(label: String, addBookmark: (() -> Unit)?, searchDevice: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            if (addBookmark != null) {
                OutlinedButton(addBookmark, colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary), contentPadding = PaddingValues(horizontal = 12.dp)) {
                    Icon(OneIcons.Bookmark, null, Modifier.size(18.dp))
                    Text(stringResource(FeatureR.string.search_add_bookmark), Modifier.padding(start = 4.dp))
                }
            }
            OutlinedButton(searchDevice, colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary), contentPadding = PaddingValues(horizontal = 12.dp)) {
                Icon(OneIcons.Search, null, Modifier.size(18.dp))
                Text(stringResource(FeatureR.string.suggestion_search_my_device), Modifier.padding(start = 4.dp))
            }
        }
    }
}

@Composable
private fun SearchInput(value: String, onChange: (String) -> Unit, hint: String, modifier: Modifier, action: ImeAction, onAction: () -> Unit) {
    BasicTextField(value, onChange, modifier, singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters, imeAction = action),
        keyboardActions = KeyboardActions(onNext = { onAction() }, onDone = { onAction() }), decorationBox = { field ->
            if (value.isEmpty()) Text(hint, color = CheckFirmTheme.colors.settingsDescription)
            field()
        })
}
