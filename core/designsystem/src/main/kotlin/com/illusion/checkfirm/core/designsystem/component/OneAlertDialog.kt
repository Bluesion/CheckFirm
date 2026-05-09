package com.illusion.checkfirm.core.designsystem.component

import android.view.Gravity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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
    horizontalPadding: Dp = 12.dp,
) {
    Dialog(
        onDismissRequest = onDismissButtonClick,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
        SideEffect {
            dialogWindow?.setGravity(Gravity.BOTTOM)
        }

        Surface(
            shape = RoundedCornerShape(size = 28.dp),
            color = CheckFirmTheme.colors.dialogBackground,
            contentColor = MaterialTheme.colorScheme.onSurface,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
                .padding(bottom = 12.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    if (text.isNotBlank()) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OneDialogTextButton(
                        onClick = onDismissButtonClick,
                        text = dismissButtonText,
                        modifier = Modifier.weight(1f),
                    )
                    VerticalDivider(
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    )
                    OneDialogTextButton(
                        onClick = onConfirmButtonClick,
                        text = confirmButtonText,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun OneAlertDialogPreview() {
    CheckFirmTheme {
        Surface {
            OneAlertDialog(
                title = "This is a title",
                text = "Add whatever you like in here, and it'll work nicely across all dialog components.",
                onConfirmButtonClick = {},
            )
        }
    }
}
