package com.illusion.checkfirm.feature.settings.catcher

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneCard
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneNavButton
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.component.OneSwitchCard
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.feature.settings.R as FeatureR

@Composable
fun InfoCatcherScreen(
    uiState: InfoCatcherUiState,
    onNavigationIconClick: () -> Unit,
    onEnableChange: (Boolean) -> Unit,
    onAddDeviceClick: () -> Unit,
    onDeleteDevice: (com.illusion.checkfirm.core.domain.model.Device) -> Unit,
    onDialogDismiss: () -> Unit,
    onDialogModelChange: (String) -> Unit,
    onDialogCscChange: (String) -> Unit,
    onSelectBookmark: (com.illusion.checkfirm.core.domain.model.Bookmark) -> Unit,
    onAddDevice: (String, String) -> Unit,
) {
    OneScaffold(
        title = stringResource(R.string.info_catcher),
        navigationIcon = {
            OneNavButton(icon = OneIcons.IcBack, onClick = onNavigationIconClick)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = innerPadding.calculateBottomPadding())
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = innerPadding.calculateTopPadding()),
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            OneSwitchCard(
                checked = uiState.isEnabled,
                onCheckedChange = onEnableChange,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(FeatureR.string.info_catcher_description),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 16.dp, bottom = 24.dp),
            )

            OneCard(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column {
                    if (uiState.devices.isEmpty()) {
                        Text(
                            text = stringResource(FeatureR.string.info_catcher_no_device),
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    } else {
                        uiState.devices.forEach { device ->
                            InfoCatcherItem(
                                deviceText = "${device.model} / ${device.csc}",
                                onDelete = { onDeleteDevice(device) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onAddDeviceClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(FeatureR.string.welcome_search_add_device))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (uiState.showDialog) {
        InfoCatcherDialog(
            model = uiState.dialogModel,
            csc = uiState.dialogCsc,
            bookmarks = uiState.bookmarks,
            onModelChange = onDialogModelChange,
            onCscChange = onDialogCscChange,
            onSelectBookmark = onSelectBookmark,
            onDismissRequest = onDialogDismiss,
            onAdd = onAddDevice,
        )
    }
}

@Composable
fun InfoCatcherItem(
    deviceText: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = deviceText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            maxLines = 1
        )

        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .size(48.dp)
                .padding(12.dp)
                .clip(CircleShape)
        ) {
            Icon(Icons.Rounded.Close, contentDescription = "Delete")
        }
    }
}

@ScreenPreview
@Composable
private fun InfoCatcherScreenPreview() {
    CheckFirmTheme {
        Surface {
            InfoCatcherScreen(
                uiState = InfoCatcherUiState(),
                onNavigationIconClick = {},
                onEnableChange = {},
                onAddDeviceClick = {},
                onDeleteDevice = {},
                onDialogDismiss = {},
                onDialogModelChange = {},
                onDialogCscChange = {},
                onSelectBookmark = {},
                onAddDevice = { _, _ -> },
            )
        }
    }
}

@ComponentPreview
@Composable
private fun InfoCatcherItemPreview() {
    CheckFirmTheme {
        Surface {
            InfoCatcherItem(
                deviceText = "SM-S928B / KOO",
                onDelete = {},
            )
        }
    }
}
