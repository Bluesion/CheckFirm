package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun OneDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color? = null,
    darkTheme: Boolean = isSystemInDarkTheme()
) {
    val defaultColor = if (darkTheme) Color(0xFF262626) else Color(0xFFEBEBEB)
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color ?: defaultColor
    )
}
