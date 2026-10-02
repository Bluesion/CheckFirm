package com.illusion.checkfirm.feature.home.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneCard
import com.illusion.checkfirm.core.designsystem.component.OneCardShape
import com.illusion.checkfirm.core.domain.model.SearchResult

@Composable
internal fun HomeResultCard(result: SearchResult, onCardClick: (Boolean) -> Unit, isFirebaseEnabled: Boolean = true) {
    val context = LocalContext.current
    val official = result.firmware.officialFirmware
    val test = result.firmware.testFirmware
    val testBuild = test.clue.ifBlank { test.decryptedFirmware.ifBlank { test.latestFirmware } }
    OneCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painterResource(R.drawable.checkfirm_icon), contentDescription = null,
                    modifier = Modifier.size(24.dp), tint = androidx.compose.ui.graphics.Color.Unspecified)
                Text("${result.device.model} (${result.device.csc})",
                    modifier = Modifier.padding(start = 4.dp),
                    style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FirmwareSummary(
                    label = stringResource(R.string.official_latest), firmware = official.latestFirmware,
                    details = listOf(
                        R.drawable.ic_smart_search_device to official.deviceName,
                        R.drawable.ic_smart_search_discovered_date to official.releaseDate,
                        R.drawable.ic_smart_search_android_version to official.androidVersion,
                    ),
                    modifier = Modifier.weight(1f), onClick = { onCardClick(true) },
                    onLongClick = { copyToClipboard(context, official.latestFirmware) },
                )
                FirmwareSummary(
                    label = stringResource(R.string.test_latest), firmware = testBuild,
                    details = listOf(
                        R.drawable.ic_smart_search_discoverer to if (isFirebaseEnabled) test.watson.ifBlank { test.discoverer } else stringResource(com.illusion.checkfirm.feature.home.R.string.unknown),
                        R.drawable.ic_smart_search_discovered_date to if (isFirebaseEnabled) test.discoveryDate else java.time.LocalDate.now().toString(),
                        R.drawable.ic_smart_search_android_version to test.androidVersion,
                    ),
                    modifier = Modifier.weight(1f), onClick = { onCardClick(false) },
                    onLongClick = { copyToClipboard(context, testBuild) },
                )
            }
        }
    }
}

@Composable
private fun FirmwareSummary(
    label: String, firmware: String, details: List<Pair<Int, String>>,
    modifier: Modifier, onClick: () -> Unit, onLongClick: () -> Unit,
) {
    Card(
        modifier = modifier.combinedClickable(onClick = onClick, onLongClick = onLongClick),
        shape = OneCardShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(label, style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(
                        text = shortBuild(firmware).ifBlank { stringResource(R.string.search_result_error) },
                        modifier = Modifier.padding(top = 4.dp),
                        style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold,
                        color = if (firmware.isBlank()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                    )
                }
                androidx.compose.material3.Surface(shape = CircleShape, color = MaterialTheme.colorScheme.outlineVariant) {
                    Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = null,
                        modifier = Modifier.size(24.dp))
                }
            }
            if (firmware.isNotBlank()) {
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                details.forEach { (icon, text) ->
                    Row(Modifier.padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(painterResource(icon), contentDescription = null, modifier = Modifier.size(16.dp))
                        Text(text.ifBlank { "-" }, modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

private fun shortBuild(firmware: String): String {
    val build = firmware.substringBefore('/').substringBefore('_').substringBefore('.')
    return if (build.length >= 6) build.takeLast(4) else build
}

private fun copyToClipboard(context: Context, text: String) {
    if (text.isBlank()) return
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("CheckFirm", text))
}
