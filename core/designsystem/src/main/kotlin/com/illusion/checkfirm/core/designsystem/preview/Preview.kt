package com.illusion.checkfirm.core.designsystem.preview

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@ComponentPreview
@Composable
fun ComponentPreview(
    content: @Composable () -> Unit = {},
) {
    CheckFirmTheme {
        Surface {
            content()
        }
    }
}