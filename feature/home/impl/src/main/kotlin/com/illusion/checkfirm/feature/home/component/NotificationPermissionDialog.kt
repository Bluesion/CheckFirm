package com.illusion.checkfirm.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.component.OneBottomSheetDialog
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.feature.home.R

@Composable
internal fun NotificationPermissionDialog(
    onGrant: () -> Unit,
    onDismiss: () -> Unit,
) {
    OneBottomSheetDialog(
        title = stringResource(R.string.notification_permission_required_title),
        onDismiss = onDismiss,
    ) {
        Text(
            text = stringResource(R.string.notification_permission_required_message),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        ) {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.notification_permission_required_button_deny))
            }
            TextButton(onClick = onGrant) {
                Text(stringResource(R.string.notification_permission_required_button_grant))
            }
        }
    }
}

@ComponentPreview
@Composable
private fun NotificationPermissionDialogPreview() {
    CheckFirmTheme {
        Surface(modifier = Modifier.padding(24.dp)) {
            NotificationPermissionDialog(onGrant = {}, onDismiss = {})
        }
    }
}
