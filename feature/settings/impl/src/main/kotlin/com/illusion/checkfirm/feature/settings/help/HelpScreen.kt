package com.illusion.checkfirm.feature.settings.help

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneCard
import com.illusion.checkfirm.core.designsystem.component.OneCardItem
import com.illusion.checkfirm.core.designsystem.component.OneDivider
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneNavButton
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.feature.settings.R as FeatureR

@Composable
fun HelpScreen(
    uiState: HelpUiState = HelpUiState(),
    onNavigateBack: () -> Unit,
    onNavigateToFirmwareManual: () -> Unit,
    onNavigateToMyDevice: () -> Unit,
) {
    OneScaffold(
        title = stringResource(R.string.help),
        navigationIcon = {
            OneNavButton(
                onClick = onNavigateBack,
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
                .verticalScroll(rememberScrollState())
                .padding(
                    top = innerPadding.calculateTopPadding() + 12.dp,
                    bottom = innerPadding.calculateBottomPadding() + 16.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            HelpItem(stringResource(FeatureR.string.help_manual), stringResource(FeatureR.string.help_manual_description), R.drawable.ic_help_manual, onNavigateToFirmwareManual)
            HelpItem(stringResource(FeatureR.string.help_device_info), stringResource(FeatureR.string.help_device_info_description), R.drawable.ic_help_device, onNavigateToMyDevice)

        }
    }
}

@ScreenPreview
@Composable
private fun HelpScreenPreview() {
    CheckFirmTheme {
        Surface {
            HelpScreen(
                onNavigateBack = {},
                onNavigateToFirmwareManual = {},
                onNavigateToMyDevice = {},
            )
        }
    }
}

@Composable
private fun HelpItem(title: String, description: String, icon: Int, onClick: () -> Unit) {
    androidx.compose.material3.Card(onClick = onClick, shape = com.illusion.checkfirm.core.designsystem.component.OneCardShape,
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        androidx.compose.foundation.layout.Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Icon(androidx.compose.ui.res.painterResource(icon), null, Modifier.size(40.dp), tint = androidx.compose.ui.graphics.Color.Unspecified)
            Column(Modifier.padding(start = 12.dp)) {
                Text(title, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 17.sp))
                Text(description, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp), color = CheckFirmTheme.colors.settingsDescription)
            }
        }
    }
}
