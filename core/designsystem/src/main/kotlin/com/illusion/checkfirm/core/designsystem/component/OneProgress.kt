package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun OneProgress(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF387AFF),
    strokeWidth: Float = 4f
) {
    CircularProgressIndicator(
        modifier = modifier.padding(4.dp),
        color = color,
        strokeWidth = strokeWidth.dp
    )
}

@ComponentPreview
@Composable
private fun OneProgressPreview() {
    CheckFirmTheme {
        OneProgress()
    }
}
