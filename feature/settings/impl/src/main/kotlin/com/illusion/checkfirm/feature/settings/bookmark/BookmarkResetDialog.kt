package com.illusion.checkfirm.feature.settings.bookmark

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun BookmarkResetDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.settings_bookmark_reset_dialog_title),
                color = MaterialTheme.colorScheme.error
            )
        },
        text = { Text(text = stringResource(R.string.settings_bookmark_reset_dialog_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.settings_bookmark_reset),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = stringResource(R.string.close)) }
        }
    )
}

@ComponentPreview
@Composable
private fun BookmarkResetDialogPreview() {
    CheckFirmTheme {
        Surface {
            BookmarkResetDialog(
                onDismiss = {},
                onConfirm = {},
            )
        }
    }
}
