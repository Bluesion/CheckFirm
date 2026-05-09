package com.illusion.checkfirm.feature.home.component

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
) {
    val context = LocalContext.current

    val (title, message) = when (state) {
        ResultState.NetworkError ->
            stringResource(R.string.check_network) to stringResource(R.string.check_network_description)

        ResultState.Empty ->
            stringResource(R.string.check_device) to stringResource(R.string.check_device_description)

        else -> return
    }

    OneCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            if (state == ResultState.NetworkError) {
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AssistChip(
                        onClick = {
                            context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                        },
                        label = { Text(stringResource(R.string.enable_wifi)) },
                        colors = AssistChipDefaults.assistChipColors(
                            labelColor = MaterialTheme.colorScheme.onSurface,
                        ),
                    )
                    AssistChip(
                        onClick = {
                            context.startActivity(Intent(Settings.ACTION_DATA_ROAMING_SETTINGS))
                        },
                        label = { Text(stringResource(R.string.data_settings)) },
                        colors = AssistChipDefaults.assistChipColors(
                            labelColor = MaterialTheme.colorScheme.onSurface,
                        ),
                    )
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
