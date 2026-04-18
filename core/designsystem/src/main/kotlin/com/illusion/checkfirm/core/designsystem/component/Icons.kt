package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

object Icons {
    val Sample: ImageVector
        get() =
            ImageVector.Builder(
                name = "sample_icon",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            ).apply {

            }.build()
}

@Preview(showBackground = true)
@Composable
private fun IconPreview() {
    CheckFirmTheme {
        Row {
            Image(
                imageVector = Icons.Sample,
                contentDescription = null,
            )
        }
    }
}
