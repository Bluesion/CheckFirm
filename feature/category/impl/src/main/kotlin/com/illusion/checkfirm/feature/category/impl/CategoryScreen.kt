package com.illusion.checkfirm.feature.category.impl

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
        title = stringResource(R.string.category),
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
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                ),
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                label = { Text(stringResource(FeatureR.string.category_name)) },
                singleLine = true,
                isError = uiState.nameError != null,
                supportingText = {
                    when (uiState.nameError) {
                        NameError.Blank -> Text(stringResource(FeatureR.string.category_name_error_empty))
                        NameError.Reserved -> Text(stringResource(FeatureR.string.category_name_error_all))
                        null -> {}
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(FeatureR.string.category_devices_title),
                style = MaterialTheme.typography.titleMedium,
            )

            if (uiState.bookmarks.isEmpty()) {
                Text(
                    text = stringResource(R.string.search_no_bookmark),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp),
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(
                        items = uiState.bookmarks,
                        key = { it.device.toString() },
                    ) { bookmark ->
                        DeviceCheckRow(
                            bookmark = bookmark,
                            checked = DeviceKey(bookmark.device.toString()) in uiState.selected,
                            onCheckedChange = { onToggleBookmark(bookmark) },
                        )
                    }
                }
            }

            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            ) {
                Text(stringResource(R.string.bookmark_save))
            }
        }
    }
}

@Composable
private fun DeviceCheckRow(
    bookmark: Bookmark,
    checked: Boolean,
    onCheckedChange: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange() }
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = bookmark.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = "${bookmark.device.model} / ${bookmark.device.csc}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        OneCheckbox(
            isChecked = checked,
            onCheckedChange = { onCheckedChange() }
        )
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
