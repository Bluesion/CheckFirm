package com.illusion.checkfirm.feature.settings.help.firmware

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.*
import com.illusion.checkfirm.feature.settings.R as FeatureR

@Composable
fun FirmwareManualScreen(
    uiState: FirmwareManualUiState = FirmwareManualUiState(),
    onNavigationIconClick: () -> Unit,
) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    var detail by rememberSaveable { mutableIntStateOf(0) }
    val codes = listOf("A720", "SKS", "U3", "B", "R", "K", "1")
    val descriptions = listOf(
        FeatureR.string.help_firmware_manual_build_device_description,
        FeatureR.string.help_firmware_manual_build_region_description,
        FeatureR.string.help_firmware_manual_build_bootloader_description,
        FeatureR.string.help_firmware_manual_build_one_ui_description,
        FeatureR.string.help_firmware_manual_build_year_description,
        FeatureR.string.help_firmware_manual_build_month_description,
        FeatureR.string.help_firmware_manual_build_revision_description,
    )
    OneScaffold(title = stringResource(FeatureR.string.help_manual), expandable = false,
        navigationIcon = {
            OneNavButton(onClick = onNavigationIconClick, shape = CircleShape, modifier = Modifier.size(48.dp)) {
                Icon(OneIcons.Back, stringResource(R.string.navigate_back))
            }
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(bottom = padding.calculateBottomPadding() + 12.dp)) {
            ManualCard(stringResource(FeatureR.string.help_firmware_manual_description), Modifier.padding(12.dp))
            OneTab(listOf("BUILD\nA720SKSU3BRK1", "CSC\nA720SSKC3BRK1", "BASEBAND\nA720NKOU3BRK1"),
                tab, { tab = it }, Modifier.padding(top = 16.dp), indicatorColor = MaterialTheme.colorScheme.primary, textStyle = MaterialTheme.typography.bodySmall)
            ManualCard(stringResource(when (tab) {
                0 -> FeatureR.string.help_firmware_manual_build_description
                1 -> FeatureR.string.help_firmware_manual_csc_description
                else -> FeatureR.string.help_firmware_manual_baseband_description
            }), Modifier.padding(12.dp))
            if (tab == 0) {
                FlowRow(Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)) {
                    codes.forEachIndexed { index, code ->
                        Card(onClick = { detail = index }, shape = OneCardShape,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                            Box(Modifier.padding(8.dp), contentAlignment = Alignment.Center) { Text(code, style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp)) }
                        }
                    }
                }
                OneCard(Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp, top = 16.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text(codes[detail], style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(stringResource(descriptions[detail]), Modifier.padding(top = 4.dp), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun ManualCard(text: String, modifier: Modifier = Modifier) {
    OneCard(modifier.fillMaxWidth()) {
        Text(text, Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 16.sp))
    }
}
