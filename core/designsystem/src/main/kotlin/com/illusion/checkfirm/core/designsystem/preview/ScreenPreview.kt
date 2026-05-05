package com.illusion.checkfirm.core.designsystem.preview

import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_NO
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Light", showBackground = true, device = PIXEL_4, uiMode = UI_MODE_NIGHT_NO)
@Preview(name = "Dark", showBackground = true, device = PIXEL_4, uiMode = UI_MODE_NIGHT_YES)
annotation class ScreenPreview
