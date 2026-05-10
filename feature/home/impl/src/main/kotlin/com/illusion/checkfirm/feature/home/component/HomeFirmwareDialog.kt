package com.illusion.checkfirm.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneBottomSheetDialog
import com.illusion.checkfirm.core.designsystem.component.OneCard
import com.illusion.checkfirm.core.designsystem.component.OneTab
import com.illusion.checkfirm.core.domain.model.SearchResult

/**
 * Bottom sheet showing firmware details for a single SearchResult — Home's
 * version of feature/search/impl/SearchDialog. Kept local to avoid an
 * impl→impl dependency; the contract (callbacks for sherlock/report/manual)
 * is identical.
 */
@Composable
internal fun HomeFirmwareDialog(
    result: SearchResult,
    onDismiss: () -> Unit,
    onCopy: (String) -> Unit,
    onOpenOfficialDoc: () -> Unit,
    onOpenSherlock: () -> Unit,
    onOpenReport: () -> Unit,
    onOpenFirmwareManual: () -> Unit,
) {
    OneBottomSheetDialog(
        title = "${result.device.model} (${result.device.csc})",
        onDismiss = onDismiss,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(onClick = onOpenReport, shape = RoundedCornerShape(50)) {
                Text(stringResource(R.string.report))
            }
        }

        Spacer(Modifier.height(8.dp))

        FirmwareSection(
            title = stringResource(R.string.official_latest),
            firmware = result.firmware.officialFirmware.latestFirmware,
            previousMap = result.firmware.officialFirmware.previousFirmware,
            betaMap = emptyMap(),
            isOfficial = true,
            onCopy = onCopy,
            onActionClick = onOpenOfficialDoc,
            onHelpClick = onOpenFirmwareManual,
        )

        Spacer(Modifier.height(12.dp))

        FirmwareSection(
            title = stringResource(R.string.test_latest),
            firmware = result.firmware.testFirmware.latestFirmware,
            previousMap = result.firmware.testFirmware.previousFirmware,
            betaMap = result.firmware.testFirmware.betaFirmware,
            isOfficial = false,
            onCopy = onCopy,
            onActionClick = {
                if (isEncryptedFirmware(result.firmware.testFirmware.latestFirmware)) {
                    onOpenSherlock()
                }
            },
            onHelpClick = onOpenFirmwareManual,
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
        ) {
            Text(stringResource(android.R.string.ok))
        }
    }
}

@Composable
private fun FirmwareSection(
    title: String,
    firmware: String,
    previousMap: Map<String, String>,
    betaMap: Map<String, String>,
    isOfficial: Boolean,
    onCopy: (String) -> Unit,
    onActionClick: () -> Unit,
    onHelpClick: () -> Unit,
) {
    OneCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = firmware.ifBlank { stringResource(R.string.search_result_error) },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                if (firmware.isNotBlank()) {
                    IconButton(onClick = { onCopy(firmware) }) {
                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = stringResource(android.R.string.copy),
                        )
                    }
                    val actionEnabled = isOfficial || isEncryptedFirmware(firmware)
                    if (actionEnabled) {
                        IconButton(onClick = onActionClick) {
                            Icon(
                                imageVector = if (isOfficial) Icons.Outlined.OpenInBrowser
                                else Icons.AutoMirrored.Outlined.HelpOutline,
                                contentDescription = null,
                            )
                        }
                    }
                }
            }

            if (firmware.isNotBlank()) {
                SmartSearchRow(firmware = firmware, onHelpClick = onHelpClick)
            }

            if (previousMap.isNotEmpty() || betaMap.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                PreviousBetaTabs(previous = previousMap, beta = betaMap, onCopy = onCopy)
            }
        }
    }
}

@Composable
private fun SmartSearchRow(firmware: String, onHelpClick: () -> Unit) {
    val parts = remember(firmware) { parseSmartSearch(firmware) }
    Spacer(Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        SmartSearchTile(
            iconRes = R.drawable.ic_smart_search_device,
            label = stringResource(R.string.smart_search_bootloader),
            value = parts.bootloader,
            modifier = Modifier.weight(1f),
        )
        SmartSearchTile(
            iconRes = R.drawable.ic_smart_search_android_version,
            label = stringResource(R.string.smart_search_major_version),
            value = parts.majorVersion,
            modifier = Modifier.weight(1f),
        )
        SmartSearchTile(
            iconRes = R.drawable.ic_smart_search_discovered_date,
            label = stringResource(R.string.smart_search_build_date),
            value = parts.buildDate,
            modifier = Modifier.weight(1f),
        )
        SmartSearchTile(
            iconRes = R.drawable.ic_smart_search_firmware_type,
            label = stringResource(R.string.smart_search_minor_version),
            value = parts.minorVersion,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onHelpClick, modifier = Modifier.size(32.dp)) {
            Icon(
                painter = painterResource(R.drawable.ic_smart_search_help),
                contentDescription = stringResource(R.string.help),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun SmartSearchTile(
    iconRes: Int,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value.ifBlank { "-" },
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
        )
    }
}

@Composable
private fun PreviousBetaTabs(
    previous: Map<String, String>,
    beta: Map<String, String>,
    onCopy: (String) -> Unit,
) {
    var tab by remember { mutableIntStateOf(0) }
    val showBeta = beta.isNotEmpty()

    val titles = if (showBeta) {
        listOf(
            stringResource(R.string.search_result_tab_previous),
            stringResource(R.string.search_result_tab_beta),
        )
    } else {
        listOf(stringResource(R.string.search_result_tab_previous))
    }

    OneTab(titles = titles, selectedTabIndex = tab, onTabSelected = { tab = it })

    Spacer(Modifier.height(8.dp))

    val items = if (tab == 0) previous else beta
    if (items.isEmpty()) {
        Text(
            text = stringResource(R.string.search_no_history),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    } else {
        items.values.take(5).forEach { value ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 2.dp),
                )
                IconButton(onClick = { onCopy(value) }, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.ContentCopy,
                        contentDescription = stringResource(android.R.string.copy),
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

private data class SmartSearchParts(
    val bootloader: String = "",
    val majorVersion: String = "",
    val buildDate: String = "",
    val minorVersion: String = "",
)

private fun parseSmartSearch(firmware: String): SmartSearchParts {
    val build = firmware.substringBefore('/')
    if (build.length < 6) return SmartSearchParts()
    val body = build.takeLast(6)
    return SmartSearchParts(
        bootloader = body[1].toString(),
        majorVersion = body[2].toString(),
        buildDate = "${body[3]}${body[4]}",
        minorVersion = body[5].toString(),
    )
}

private fun isEncryptedFirmware(firmware: String): Boolean {
    if (firmware.isBlank()) return false
    val build = firmware.substringBefore('/')
    return build.any { it.isLowerCase() || it.isDigit() }
}
