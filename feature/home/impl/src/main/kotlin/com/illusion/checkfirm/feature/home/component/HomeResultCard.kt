package com.illusion.checkfirm.feature.home.component

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneCard
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.domain.model.Device
import com.illusion.checkfirm.domain.model.Firmware
import com.illusion.checkfirm.domain.model.OfficialFirmware
import com.illusion.checkfirm.domain.model.SearchResult
import com.illusion.checkfirm.domain.model.TestFirmware

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeResultCard(
    result: SearchResult,
    onCardClick: () -> Unit,
) {
    val context = LocalContext.current

    OneCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onCardClick,
                    onLongClick = {
                        val build = result.firmware.officialFirmware.latestFirmware
                        if (build.isNotBlank()) {
                            copyToClipboard(context, build)
                        }
                    },
                )
                .padding(12.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${result.device.model} (${result.device.csc})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            FirmwareRow(
                label = stringResource(R.string.official_latest),
                value = result.firmware.officialFirmware.latestFirmware,
            )
            FirmwareRow(
                label = stringResource(R.string.test_latest),
                value = result.firmware.testFirmware.latestFirmware,
            )
        }
    }
}

@Composable
private fun FirmwareRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = value.ifBlank { "-" },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.setPrimaryClip(ClipData.newPlainText("CheckFirm", text))
}

@ComponentPreview
@Composable
private fun HomeResultCardPreview() {
    CheckFirmTheme {
        Surface {
            HomeResultCard(
                result = SearchResult(
                    device = Device("SM-S928B", "KOO"),
                    firmware = Firmware(
                        officialFirmware = OfficialFirmware(
                            latestFirmware = "S928BKSU3AXL5/S928BOXM3AXL5/S928BKSU3AXL5",
                        ),
                        testFirmware = TestFirmware(
                            latestFirmware = "abc123def456abc123def456abc12345",
                        ),
                    ),
                ),
                onCardClick = {},
            )
        }
    }
}
