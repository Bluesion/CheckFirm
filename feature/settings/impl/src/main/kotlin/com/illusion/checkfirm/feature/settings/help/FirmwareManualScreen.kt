
package com.illusion.checkfirm.feature.settings.help

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun FirmwareManualScreen(
    uiState: FirmwareManualUiState = FirmwareManualUiState(),
    onNavigationIconClick: () -> Unit,
) {
    OneScaffold(
        title = stringResource(R.string.help_manual),
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
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
                .padding(horizontal = 12.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = innerPadding.calculateBottomPadding() + 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ManualCard(stringResource(R.string.help_firmware_manual_description))

            ManualSection(
                title = stringResource(R.string.sherlock_build),
                body = stringResource(R.string.help_firmware_manual_build_description),
            )
            ManualDetail(
                stringResource(R.string.model),
                stringResource(R.string.help_firmware_manual_build_device_description),
            )
            ManualDetail(
                stringResource(R.string.help_device_info),
                stringResource(R.string.help_firmware_manual_build_region_description),
            )
            ManualDetail(
                "Bootloader",
                stringResource(R.string.help_firmware_manual_build_bootloader_description),
            )
            ManualDetail(
                "One UI",
                stringResource(R.string.help_firmware_manual_build_one_ui_description),
            )
            ManualDetail(
                stringResource(R.string.smart_search_build_date) + " (Year)",
                stringResource(R.string.help_firmware_manual_build_year_description),
            )
            ManualDetail(
                stringResource(R.string.smart_search_build_date) + " (Month)",
                stringResource(R.string.help_firmware_manual_build_month_description),
            )
            ManualDetail(
                stringResource(R.string.smart_search_minor_version),
                stringResource(R.string.help_firmware_manual_build_revision_description),
            )

            ManualSection(
                title = "CSC",
                body = stringResource(R.string.help_firmware_manual_csc_description),
            )

            ManualSection(
                title = stringResource(R.string.sherlock_baseband),
                body = stringResource(R.string.help_firmware_manual_baseband_description),
            )
        }
    }
}

@Composable
private fun ManualCard(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        )
    }
}

@Composable
private fun ManualSection(title: String, body: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun ManualDetail(title: String, body: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
