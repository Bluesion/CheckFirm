package com.illusion.checkfirm.feature.settings.catcher

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun InfoCatcherDialog(
    model: String,
    csc: String,
    onModelChange: (String) -> Unit,
    onCscChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onAdd: (String, String) -> Unit,
) {
    val chipScrollState = rememberScrollState()

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Info Catcher",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(chipScrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf("Galaxy S", "Galaxy Z", "Galaxy A", "Galaxy Tab").forEach { series ->
                    FilterChip(
                        selected = false,
                        onClick = {
                            when (series) {
                                "Galaxy S" -> onModelChange("SM-S9")
                                "Galaxy Z" -> onModelChange("SM-F")
                                "Galaxy A" -> onModelChange("SM-A")
                                "Galaxy Tab" -> onModelChange("SM-X")
                            }
                        },
                        label = { Text(series) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = model,
                onValueChange = { onModelChange(it.uppercase()) },
                label = { Text(text = stringResource(R.string.model)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = csc,
                onValueChange = { if (it.length <= 3) onCscChange(it.uppercase()) },
                label = { Text(text = stringResource(R.string.csc)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onDismissRequest) {
                    Text(text = stringResource(android.R.string.cancel))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onAdd(model, csc) }) {
                    Text(text = stringResource(R.string.add_item))
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun InfoCatcherDialogPreview() {
    CheckFirmTheme {
        Surface {
            InfoCatcherDialog(
                model = "SM-S928",
                csc = "KOO",
                onModelChange = {},
                onCscChange = {},
                onDismissRequest = {},
                onAdd = { _, _ -> },
            )
        }
    }
}
