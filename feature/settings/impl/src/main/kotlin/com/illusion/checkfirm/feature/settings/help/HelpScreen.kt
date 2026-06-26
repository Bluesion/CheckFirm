package com.illusion.checkfirm.feature.settings.help

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState())
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding() + 16.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OneCard {
                OneCardItem(
                    title = stringResource(FeatureR.string.help_manual),
                    description = stringResource(FeatureR.string.help_manual_description),
                    iconVector = OneIcons.HelpManual,
                    onClick = onNavigateToFirmwareManual,
                )
                OneDivider()
                OneCardItem(
                    title = stringResource(FeatureR.string.help_device_info),
                    description = stringResource(FeatureR.string.help_device_info_description),
                    iconVector = OneIcons.HelpDevice,
                    onClick = onNavigateToMyDevice,
                )
            }
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
