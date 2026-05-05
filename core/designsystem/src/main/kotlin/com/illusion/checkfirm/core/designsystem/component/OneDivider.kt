package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun OneDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color,
    )
}

@ComponentPreview
@Composable
private fun OneDividerPreview() {
    CheckFirmTheme {
        OneDivider()
    }
}
