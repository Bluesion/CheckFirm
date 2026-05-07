package com.illusion.checkfirm.feature.settings.catcher

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneBottomSheetDialog
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Device

@Composable
fun InfoCatcherDialog(
    model: String,
    csc: String,
    bookmarks: List<Bookmark>,
    onModelChange: (String) -> Unit,
    onCscChange: (String) -> Unit,
    onSelectBookmark: (Bookmark) -> Unit,
    onDismissRequest: () -> Unit,
    onAdd: (String, String) -> Unit,
) {
    OneBottomSheetDialog(
        title = stringResource(R.string.info_catcher),
        onDismiss = onDismissRequest,
    ) {
        if (bookmarks.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                bookmarks.forEach { bookmark ->
                    AssistChip(
                        onClick = { onSelectBookmark(bookmark) },
                        label = { Text(bookmark.name) },
                        colors = AssistChipDefaults.assistChipColors(
                            labelColor = MaterialTheme.colorScheme.onSurface,
                        ),
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = model,
            onValueChange = { onModelChange(it.uppercase()) },
            label = { Text(stringResource(R.string.model)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = csc,
            onValueChange = { if (it.length <= 3) onCscChange(it.uppercase()) },
            label = { Text(stringResource(R.string.csc)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = onDismissRequest,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(22.dp),
            ) {
                Text(stringResource(android.R.string.cancel))
            }
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = { onAdd(model, csc) },
                enabled = model.isNotBlank() && csc.length == 3,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(22.dp),
            ) {
                Text(stringResource(R.string.add_item))
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
                bookmarks = listOf(
                    Bookmark("S24", Device("SM-S928B", "KOO"), "Galaxy S"),
                    Bookmark("Z Fold5", Device("SM-F946B", "KOO"), "Galaxy Z"),
                ),
                onModelChange = {},
                onCscChange = {},
                onSelectBookmark = {},
                onDismissRequest = {},
                onAdd = { _, _ -> },
            )
        }
    }
}
