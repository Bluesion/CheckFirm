package com.illusion.checkfirm.feature.sherlock

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.component.OneBottomSheetDialog
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.feature.sherlock.R as FeatureR

/**
 * Replaces the legacy SherlockInformationDialog. Shows a brief explanation of
 * Manual vs Script modes when the user taps the info icon in the toolbar.
 */
@Composable
internal fun SherlockInformationDialog(onDismiss: () -> Unit) {
    OneBottomSheetDialog(
        title = stringResource(FeatureR.string.sherlock),
        onDismiss = onDismiss,
    ) {
        Text(
            text = stringResource(FeatureR.string.sherlock_manual_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = stringResource(FeatureR.string.sherlock_script_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
        ) {
            Text(text = stringResource(android.R.string.ok))
        }
    }
}

@ComponentPreview
@Composable
private fun SherlockInformationDialogPreview() {
    CheckFirmTheme {
        Surface { SherlockInformationDialog(onDismiss = {}) }
    }
}
