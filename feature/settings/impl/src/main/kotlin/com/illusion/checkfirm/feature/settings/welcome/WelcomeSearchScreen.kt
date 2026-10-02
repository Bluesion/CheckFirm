package com.illusion.checkfirm.feature.settings.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneNavButton
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.component.OneSwitchCard
import com.illusion.checkfirm.core.designsystem.component.OneCard
import com.illusion.checkfirm.core.designsystem.component.OneSwitch
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.Device
import com.illusion.checkfirm.feature.settings.R as FeatureR

@Composable
fun WelcomeSearchScreen(
    uiState: WelcomeSearchUiState = WelcomeSearchUiState(),
    onIsWelcomeSearchEnabledChange: (Boolean) -> Unit = {},
    onShowDialogChange: (Boolean) -> Unit = {},
    onModelChange: (String) -> Unit = {},
    onCscChange: (String) -> Unit = {},
    onSelectBookmark: (Bookmark) -> Unit = {},
    onAddDevice: (String, String) -> Unit = { _, _ -> },
    onRemoveDevice: (Device) -> Unit = {},
    onNavigationIconClick: () -> Unit,
) {
    val context = LocalContext.current
    if (uiState.showDialog) {
        WelcomeSearchDialog(
            uiState = uiState,
            onModelChange = onModelChange,
            onCscChange = onCscChange,
            onSelectBookmark = onSelectBookmark,
            onDismissRequest = { onShowDialogChange(false) },
            onAddDevice = onAddDevice,
        )
    }

    OneScaffold(
        title = stringResource(R.string.welcome_search),
        actions = {
            OneNavButton({
                if (uiState.devices.size < 5) onShowDialogChange(true)
                else Toast.makeText(context, context.getString(FeatureR.string.welcome_search_empty_device_list), Toast.LENGTH_SHORT).show()
            }, CircleShape, Modifier.size(48.dp)) {
                Icon(Icons.Rounded.Add, stringResource(FeatureR.string.welcome_search_add_device))
            }
        },
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(bottom = innerPadding.calculateBottomPadding())
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState())
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            OneSwitchCard(checked = uiState.isWelcomeSearchEnabled,
                onCheckedChange = onIsWelcomeSearchEnabledChange,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp))

            Text(
                text = stringResource(FeatureR.string.welcome_search_description),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(horizontal = 12.dp)
            )

            if (uiState.devices.isNotEmpty()) {
                OneCard(Modifier.fillMaxWidth().padding(top = 24.dp)) {
                    uiState.devices.forEachIndexed { index, device ->
                        WelcomeSearchItem(device = device, onDelete = { onRemoveDevice(device) })
                        if (index < uiState.devices.lastIndex) androidx.compose.material3.HorizontalDivider(Modifier.padding(horizontal = 12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeSearchItem(
    device: Device,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${device.model} (${device.csc})",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            maxLines = 1
        )

        IconButton(
            onClick = onDelete,
            modifier = Modifier.padding(end = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = stringResource(R.string.delete_item)
            )
        }
    }
}

@ScreenPreview
@Composable
private fun WelcomeSearchScreenPreview() {
    CheckFirmTheme {
        Surface {
            WelcomeSearchScreen(onNavigationIconClick = {})
        }
    }
}

@ComponentPreview
@Composable
private fun WelcomeSearchItemPreview() {
    CheckFirmTheme {
        Surface {
            WelcomeSearchItem(
                device = Device(model = "SM-S928B", csc = "KOO"),
                onDelete = {},
            )
        }
    }
}
