package com.illusion.checkfirm.core.designsystem.component

import android.view.Gravity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun OneAlertDialog(
    title: String,
    onConfirmButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "",
    confirmButtonText: String = stringResource(android.R.string.ok),
    dismissButtonText: String = stringResource(android.R.string.cancel),
    onDismissButtonClick: () -> Unit = {},
    horizontalPadding: Dp = 24.dp,
) {
    AlertDialog(
        onDismissRequest = onDismissButtonClick,
        title = {
            val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
            SideEffect {
                dialogWindow?.setGravity(Gravity.BOTTOM)
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            if (text.isNotBlank()) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmButtonClick,
            ) {
                Text(
                    text = confirmButtonText,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissButtonClick,
            ) {
                Text(
                    text = dismissButtonText,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
        properties = DialogProperties(usePlatformDefaultWidth = false),
    )
}

@ComponentPreview
@Composable
private fun OneAlertDialogPreview() {
    CheckFirmTheme {
        OneAlertDialog(
            title = "Delete bookmark?",
            text = "This action cannot be undone.",
            onConfirmButtonClick = {},
        )
    }
}

