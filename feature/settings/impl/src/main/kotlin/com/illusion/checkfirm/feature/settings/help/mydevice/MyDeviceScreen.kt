package com.illusion.checkfirm.feature.settings.help.mydevice

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.*
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.feature.settings.R as FeatureR

@Composable
fun MyDeviceScreen(
    uiState: MyDeviceUiState, onNavigationIconClick: () -> Unit,
    onEditDeviceName: () -> Unit = {}, onAddBookmark: () -> Unit = {},
) {
    OneScaffold(title = stringResource(FeatureR.string.help_device_info), expandable = false,
        navigationIcon = { OneNavButton(onNavigationIconClick, CircleShape, Modifier.size(48.dp)) {
            Icon(OneIcons.Back, stringResource(R.string.navigate_back))
        } },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding())) {
            Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(uiState.userName, style = MaterialTheme.typography.headlineSmall, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                AssistChip(onClick = onEditDeviceName, label = { Text(stringResource(FeatureR.string.help_device_info_edit)) })
                Spacer(Modifier.height(16.dp))
                DeviceRow(stringResource(FeatureR.string.help_device_name), uiState.deviceName)
                DeviceRow(stringResource(R.string.model), uiState.model)
                DeviceRow(stringResource(R.string.csc), uiState.csc)
            }
            Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                colors = CardDefaults.cardColors(containerColor = CheckFirmTheme.colors.tipCardBackground), shape = OneCardShape) {
                Column(Modifier.padding(16.dp)) {
                    Text(stringResource(FeatureR.string.suggestion_title), style = MaterialTheme.typography.bodyLarge)
                    TextButton(onClick = onAddBookmark) {
                        Text(stringResource(FeatureR.string.suggestion_bookmark_my_device), color = CheckFirmTheme.colors.tipText)
                    }
                }
            }
        }
    }
}

@Composable
private fun DeviceRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = CheckFirmTheme.colors.settingsDescription)
        Text(value.ifBlank { stringResource(FeatureR.string.unknown) }, modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium, textAlign = androidx.compose.ui.text.style.TextAlign.End)
    }
}
