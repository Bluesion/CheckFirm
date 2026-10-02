package com.illusion.checkfirm.feature.category.impl

import androidx.compose.foundation.selection.toggleable
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneCheckbox
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneNavButton
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.Device
import com.illusion.checkfirm.feature.category.R as FeatureR

@Composable
fun CategoryScreen(
    uiState: CategoryEditUiState,
    onNameChange: (String) -> Unit,
    onToggleBookmark: (Bookmark) -> Unit,
    onSave: () -> Unit,
    onNavigationIconClick: () -> Unit,
) {
    OneScaffold(
        title = "",
        expandable = false,
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .padding(
                    top = 0.dp,
                    bottom = innerPadding.calculateBottomPadding(),
                ),
        ) {
            com.illusion.checkfirm.core.designsystem.component.OneCard(
                Modifier.weight(1f).fillMaxWidth().padding(top = 12.dp),
            ) {
                Column(Modifier.fillMaxSize().padding(12.dp)) {
                    androidx.compose.foundation.text.BasicTextField(
                        value = uiState.name, onValueChange = onNameChange, singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                        textStyle = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onSurface),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.onSurface),
                        decorationBox = { field ->
                            if (uiState.name.isEmpty()) Text(stringResource(FeatureR.string.category_name), color = MaterialTheme.colorScheme.onSurfaceVariant)
                            field()
                        },
                    )
                    uiState.nameError?.let { error ->
                        Text(stringResource(when (error) {
                            NameError.Blank -> FeatureR.string.category_name_error_empty
                            NameError.Reserved -> FeatureR.string.category_name_error_all
                            NameError.NoDevices -> FeatureR.string.category_devices_error_empty
                            NameError.Duplicate -> FeatureR.string.category_name_error_duplicate
                        }), color = MaterialTheme.colorScheme.error)
                    }
                    Text(stringResource(FeatureR.string.category_devices_title), Modifier.padding(top = 16.dp, bottom = 8.dp), style = MaterialTheme.typography.bodyMedium)
                    LazyColumn(Modifier.weight(1f).fillMaxWidth()) {
                        items(uiState.bookmarks, key = { it.id ?: "${it.name}:${it.device}" }) { bookmark ->
                            DeviceCheckRow(bookmark, bookmark.deviceKey() in uiState.selected) { onToggleBookmark(bookmark) }
                        }
                    }
                }
            }
            androidx.compose.material3.TextButton(onSave, Modifier.fillMaxWidth().height(56.dp)) {
                Text(stringResource(R.string.bookmark_save), color = MaterialTheme.colorScheme.onSurface, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun DeviceCheckRow(bookmark: Bookmark, checked: Boolean, onCheckedChange: () -> Unit) {
    Row(Modifier.fillMaxWidth().toggleable(checked, role = androidx.compose.ui.semantics.Role.Checkbox) { onCheckedChange() }.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(bookmark.name, style = MaterialTheme.typography.titleLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text("${bookmark.device.model} · ${bookmark.device.csc}", style = MaterialTheme.typography.bodyMedium)
        }
        OneCheckbox(checked, { onCheckedChange() }, Modifier.clearAndSetSemantics {})
    }
}

@ScreenPreview
@Composable
private fun CategoryScreenPreview() {
    CheckFirmTheme {
        Surface {
            CategoryScreen(
                uiState = CategoryEditUiState(
                    name = "Galaxy S",
                    bookmarks = listOf(
                        Bookmark("S24", Device("SM-S928B", "KOO"), "Galaxy S"),
                        Bookmark("Z Fold5", Device("SM-F946B", "KOO"), "Galaxy Z"),
                    ),
                    selected = setOf(DeviceKey("Device(model=SM-S928B, csc=KOO)")),
                ),
                onNameChange = {},
                onToggleBookmark = {},
                onSave = {},
                onNavigationIconClick = {},
            )
        }
    }
}
