package com.illusion.checkfirm.feature.home.component

import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneCard
import com.illusion.checkfirm.core.designsystem.component.OneCardShape
import com.illusion.checkfirm.core.designsystem.component.OneTab
import com.illusion.checkfirm.core.domain.model.SearchResult
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.illusion.checkfirm.feature.home.R as FeatureR

@Composable
internal fun HomeFirmwareDialog(
    result: SearchResult, isOfficial: Boolean = true,
    onDismiss: () -> Unit, onCopy: (String) -> Unit, onOpenOfficialDoc: () -> Unit,
    onOpenSherlock: () -> Unit, onOpenReport: () -> Unit, onOpenFirmwareManual: () -> Unit,
) {
    val official = result.firmware.officialFirmware
    val test = result.firmware.testFirmware
    val latest = if (isOfficial) official.latestFirmware else test.latestFirmware
    val smartFirmware = if (isOfficial) latest else test.clue.ifBlank { test.decryptedFirmware.ifBlank { latest } }
    val previous = if (isOfficial) official.previousFirmware else test.previousFirmware
    val beta = if (isOfficial) emptyMap() else test.betaFirmware
    var tab by rememberSaveable(result.device, isOfficial) { mutableIntStateOf(0) }
    val body = firmwareBody(smartFirmware)
    val officialBody = firmwareBody(official.latestFirmware)
    val locale = LocalConfiguration.current.locales[0]
    val date = remember(body, locale) {
        if (body.length == 6 && body[3] in 'A'..'Z' && body[4] in 'A'..'L')
            LocalDate.of(2000 + body[3].code - 'A'.code + 1, body[4].code - 'A'.code + 1, 1).format(DateTimeFormatter.ofPattern("MMMM, yyyy", locale))
        else ""
    }
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        scrimColor = Color.Black.copy(alpha = 0.8f),
        containerColor = Color.Transparent, tonalElevation = 0.dp, dragHandle = null) {
        Column(Modifier.fillMaxWidth().heightIn(max = LocalWindowInfo.current.containerDpSize.height * 0.9f)
            .verticalScroll(rememberScrollState()).padding(horizontal = 24.dp).padding(top = 24.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(onClick = onOpenReport, modifier = Modifier.align(Alignment.End), shape = OneCardShape,
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFDBC9))) {
                Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.ic_report), null, Modifier.size(24.dp), tint = Color.Unspecified)
                    Text(stringResource(R.string.report).uppercase(locale), Modifier.padding(start = 4.dp), style = MaterialTheme.typography.labelSmall, color = Color(0xFF341000))
                }
            }
            OneCard {
                Column(Modifier.padding(14.dp)) {
                    Text(stringResource(if (isOfficial) R.string.official_latest else R.string.test_latest), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Row(Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(latest.ifBlank { stringResource(R.string.search_result_error) }, Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                        if (latest.isNotBlank()) {
                            IconButton({ onCopy(latest) }, Modifier.size(24.dp)) { Icon(painterResource(R.drawable.ic_copy), stringResource(android.R.string.copy), tint = CheckFirmTheme.colors.textSecondary) }
                            Spacer(Modifier.width(8.dp))
                            if (isOfficial || latest.matches(Regex("[a-fA-F0-9]{32}"))) {
                                IconButton(if (isOfficial) onOpenOfficialDoc else onOpenSherlock, Modifier.size(24.dp)) {
                                    Icon(painterResource(R.drawable.ic_web), stringResource(if (isOfficial) R.string.official_latest else R.string.sherlock), tint = CheckFirmTheme.colors.textSecondary)
                                }
                            }
                        }
                    }
                }
            }
            if (body.length == 6) OneCard {
                Column(Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(stringResource(FeatureR.string.smart_search), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        IconButton(onOpenFirmwareManual, Modifier.padding(start = 4.dp).size(16.dp)) { Icon(painterResource(R.drawable.ic_smart_search_help), stringResource(R.string.help), Modifier.size(16.dp)) }
                    }
                    Row(Modifier.padding(top = 8.dp).fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                        SmartDetail(stringResource(R.string.smart_search_bootloader), body.take(2), if (isOfficial) "" else stringResource(
                            if (officialBody.take(2) == body.take(2)) FeatureR.string.smart_search_downgrade_possible else FeatureR.string.smart_search_downgrade_impossible))
                        SmartDetail(stringResource(R.string.smart_search_major_version), body[2].toString(), if (isOfficial) "Android ${official.androidVersion}" else stringResource(
                            if (officialBody.length < 3 || officialBody[2] == body[2]) FeatureR.string.smart_search_type_minor
                            else if (officialBody[2] < body[2]) FeatureR.string.smart_search_type_major else FeatureR.string.smart_search_type_rollback))
                        SmartDetail(stringResource(R.string.smart_search_build_date), body.substring(3,5), date.ifBlank { stringResource(FeatureR.string.unknown) })
                        SmartDetail(stringResource(R.string.smart_search_minor_version), body[5].toString(), "")
                    }
                }
            }
            OneCard {
                Column(Modifier.padding(14.dp)) {
                    if (isOfficial) Text(stringResource(FeatureR.string.official_previous), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    else OneTab(listOf(stringResource(R.string.search_result_tab_previous), stringResource(R.string.search_result_tab_beta)), tab, { tab = it })
                    val values = if (tab == 0) previous.values else beta.values
                    Column(Modifier.height(150.dp).padding(top = 8.dp).verticalScroll(rememberScrollState())) {
                    if (values.isEmpty()) Text(stringResource(R.string.search_no_history), Modifier.padding(vertical = 12.dp))
                    else values.forEach { value ->
                        Text(value, Modifier.fillMaxWidth().clickable { onCopy(value) }.padding(vertical = 4.dp),
                            style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    }
                }
            }
            TextButton(onDismiss, Modifier.fillMaxWidth()) { Text(stringResource(android.R.string.ok), color = Color.White) }
        }
    }
}

@Composable
private fun SmartDetail(label: String, value: String, description: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, Modifier.padding(top = 4.dp), style = MaterialTheme.typography.bodyMedium)
        if (description.isNotBlank()) Text(description, style = MaterialTheme.typography.labelLarge)
    }
}

private fun firmwareBody(value: String): String = value.substringBefore('/').substringBefore('_').substringBefore('.').let { if (it.length >= 6) it.takeLast(6) else "" }
