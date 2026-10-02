package com.illusion.checkfirm.feature.home.component

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneCard
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.feature.home.ResultState
import com.illusion.checkfirm.feature.home.R as FeatureR

/**
 * Tip card shown on Home when the firmware fetch failed or returned nothing.
 *
 * NetworkError → suggest enabling Wi-Fi / cellular data via system settings.
 * Empty → simple message to verify the device is correct.
 */
@Composable
internal fun HomeErrorState(
    state: ResultState,
    modifier: Modifier = Modifier,
    onCheckDevice: () -> Unit = {},
) {
    val context = LocalContext.current

    val network = state == ResultState.NetworkError
    if (!network && state != ResultState.Empty) return
    Column(modifier.fillMaxWidth().padding(top = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        androidx.compose.material3.Icon(androidx.compose.ui.res.painterResource(R.drawable.ic_bell), null,
            Modifier.size(64.dp), tint = androidx.compose.ui.graphics.Color.Unspecified)
        Text(stringResource(if (network) FeatureR.string.main_network_error_title else FeatureR.string.main_search_error_title),
            Modifier.padding(top = 8.dp), style = MaterialTheme.typography.titleLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Text(stringResource(if (network) FeatureR.string.main_network_error_text else FeatureR.string.main_search_error_text),
            Modifier.padding(top = 8.dp), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium)
        androidx.compose.material3.Card(Modifier.fillMaxWidth().padding(top = 24.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = CheckFirmTheme.colors.tipCardBackground),
            shape = com.illusion.checkfirm.core.designsystem.component.OneCardShape) {
            Column(Modifier.padding(16.dp)) {
                Text(stringResource(FeatureR.string.suggestion_title), style = MaterialTheme.typography.bodyLarge)
                if (network) {
                    androidx.compose.material3.TextButton(onClick = { context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS)) }) {
                        Text(stringResource(FeatureR.string.suggestion_wifi), color = CheckFirmTheme.colors.tipText)
                    }
                    androidx.compose.material3.TextButton(onClick = { context.startActivity(Intent(Settings.ACTION_DATA_USAGE_SETTINGS)) }) {
                        Text(stringResource(FeatureR.string.suggestion_data), color = CheckFirmTheme.colors.tipText)
                    }
                } else androidx.compose.material3.TextButton(onClick = onCheckDevice) {
                    Text(stringResource(FeatureR.string.suggestion_check_my_device_info), color = CheckFirmTheme.colors.tipText)
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun HomeErrorStatePreview() {
    CheckFirmTheme {
        Surface { HomeErrorState(state = ResultState.NetworkError) }
    }
}
