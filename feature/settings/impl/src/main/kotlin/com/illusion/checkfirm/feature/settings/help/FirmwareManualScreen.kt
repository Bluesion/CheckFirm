package com.illusion.checkfirm.feature.settings.help

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illusion.checkfirm.core.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirmwareManualScreen(
    uiState: FirmwareManualUiState = FirmwareManualUiState(),
    onNavigationIconClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.help_manual)) },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.help_firmware_manual_description),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp, start = 4.dp, end = 4.dp)
            )

            ManualSection(
                title = stringResource(R.string.sherlock_build),
                body = stringResource(R.string.help_firmware_manual_build_description)
            )
            ManualDetail(
                stringResource(R.string.model),
                stringResource(R.string.help_firmware_manual_build_device_description)
            )
            ManualDetail(
                stringResource(R.string.help_device_info),
                stringResource(R.string.help_firmware_manual_build_region_description)
            )
            ManualDetail(
                "Bootloader",
                stringResource(R.string.help_firmware_manual_build_bootloader_description)
            )
            ManualDetail(
                "One UI",
                stringResource(R.string.help_firmware_manual_build_one_ui_description)
            )
            ManualDetail(
                stringResource(R.string.smart_search_build_date) + " (Year)",
                stringResource(R.string.help_firmware_manual_build_year_description)
            )
            ManualDetail(
                stringResource(R.string.smart_search_build_date) + " (Month)",
                stringResource(R.string.help_firmware_manual_build_month_description)
            )
            ManualDetail(
                stringResource(R.string.smart_search_minor_version),
                stringResource(R.string.help_firmware_manual_build_revision_description)
            )

            ManualSection(
                title = "CSC",
                body = stringResource(R.string.help_firmware_manual_csc_description)
            )

            ManualSection(
                title = stringResource(R.string.sherlock_baseband),
                body = stringResource(R.string.help_firmware_manual_baseband_description)
            )
        }
    }
}

@Composable
private fun ManualSection(title: String, body: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = body, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ManualDetail(title: String, body: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Text(text = body, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun FirmwareManualRoute(
    onNavigationIconClick: () -> Unit = {},
    viewModel: FirmwareManualViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FirmwareManualScreen(
        uiState = uiState,
        onNavigationIconClick = onNavigationIconClick
    )
}
