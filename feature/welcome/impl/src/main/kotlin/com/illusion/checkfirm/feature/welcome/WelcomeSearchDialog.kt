package com.illusion.checkfirm.feature.welcome

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Devices.PIXEL_2
import androidx.compose.ui.tooling.preview.Preview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun WelcomeSearchDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
}

@Preview(showBackground = true, showSystemUi = true, device = PIXEL_2)
@Preview(showBackground = true, showSystemUi = true, device = PIXEL_2, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun WelcomeSearchDialogPreview() {
    CheckFirmTheme {
        WelcomeSearchDialog(
            onDismiss = {},
            onConfirm = {},
        )
    }
}
