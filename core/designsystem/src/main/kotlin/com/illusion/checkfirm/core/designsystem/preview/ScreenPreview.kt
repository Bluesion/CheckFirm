package com.illusion.checkfirm.core.designsystem.preview

import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, device = PIXEL_4)
@Preview(showBackground = true, device = PIXEL_4, uiMode = UI_MODE_NIGHT_YES)
@Repeatable
annotation class ScreenPreview