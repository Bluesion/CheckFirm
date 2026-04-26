
package com.illusion.checkfirm.feature.settings.help

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneListCard
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.component.OneSettingsDivider
import com.illusion.checkfirm.core.designsystem.component.OneSettingsItem
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

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
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = OneIcons.IcBack,
                    contentDescription = null,
                    tint = CheckFirmTheme.colors.toolbarIconTint,
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = innerPadding.calculateBottomPadding() + 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OneListCard {
                OneSettingsItem(
                    title = stringResource(R.string.help_manual),
                    description = stringResource(R.string.help_manual_description),
                    iconVector = OneIcons.IcHelpManual,
                    onClick = onNavigateToFirmwareManual,
                )
                OneSettingsDivider()
                OneSettingsItem(
                    title = stringResource(R.string.help_device_info),
                    description = stringResource(R.string.help_device_info_description),
                    iconVector = OneIcons.IcHelpDevice,
                    onClick = onNavigateToMyDevice,
                )
            }
        }
    }
}
