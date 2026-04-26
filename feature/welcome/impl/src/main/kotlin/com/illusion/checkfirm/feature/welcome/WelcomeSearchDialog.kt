package com.illusion.checkfirm.feature.welcome

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun WelcomeSearchDialog(
    uiState: WelcomeSearchUiState,
    onModelChange: (String) -> Unit,
    onCscChange: (String) -> Unit,
    onSelectedChipChange: (String?) -> Unit,
    onDismissRequest: () -> Unit,
    onAddDevice: (model: String, csc: String) -> Unit,
) {
    val chips = listOf("S24", "S23", "Z Fold5")
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.welcome_search),
                    style = MaterialTheme.typography.headlineSmall,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    chips.forEach { chip ->
                        FilterChip(
                            selected = chip == uiState.selectedChip,
                            onClick = { onSelectedChipChange(chip) },
                            label = { Text(chip) },
                        )
                    }
                }

                OutlinedTextField(
                    value = uiState.model,
                    onValueChange = { onModelChange(it.uppercase()) },
                    label = { Text(text = stringResource(R.string.model)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                )

                OutlinedTextField(
                    value = uiState.csc,
                    onValueChange = { if (it.length <= 3) onCscChange(it.uppercase()) },
                    label = { Text(text = stringResource(R.string.csc)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = stringResource(android.R.string.cancel))
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(
                        onClick = { onAddDevice(uiState.model, uiState.csc) },
                        enabled = uiState.model.isNotBlank() && uiState.csc.length == 3,
                    ) {
                        Text(text = stringResource(R.string.add_item))
                    }
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun WelcomeSearchDialogPreview() {
    CheckFirmTheme {
        WelcomeSearchDialog(
            uiState = WelcomeSearchUiState(model = "SM-S928", csc = "KOO"),
            onModelChange = {},
            onCscChange = {},
            onSelectedChipChange = {},
            onDismissRequest = {},
            onAddDevice = { _, _ -> },
        )
    }
}
