package com.illusion.checkfirm.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
    val dmDarkBlue: Color,
    val dmLightBlue: Color,
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        dmDarkBlue = Color.Unspecified,
        dmLightBlue = Color.Unspecified,
    )
}
