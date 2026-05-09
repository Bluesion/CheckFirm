package com.illusion.checkfirm.feature.settings.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneBottomSheetDialog
import com.illusion.checkfirm.core.designsystem.component.OneDialogTextButton
import com.illusion.checkfirm.core.designsystem.component.OneEditText
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.feature.settings.R as FeatureR

@Composable
fun ProfileDialog(
    initialName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var name by remember(initialName) { mutableStateOf(initialName) }

    OneBottomSheetDialog(
        title = stringResource(FeatureR.string.settings_profile_user_name),
        onDismiss = onDismiss,
    ) {
        OneEditText(
            value = name,
            onValueChange = { name = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            OneDialogTextButton(
                onClick = onDismiss,
                text = stringResource(android.R.string.cancel),
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(8.dp))
            OneDialogTextButton(
                onClick = {
                    onConfirm(name.ifBlank { "Unknown" })
                    onDismiss()
                },
                text = stringResource(R.string.bookmark_save),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@ComponentPreview
@Composable
private fun ProfileDialogPreview() {
    CheckFirmTheme {
        Surface {
            ProfileDialog(
                initialName = "Bluesion",
                onDismiss = {},
                onConfirm = {},
            )
        }
    }
}
