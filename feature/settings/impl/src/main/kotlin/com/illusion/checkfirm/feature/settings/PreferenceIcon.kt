package com.illusion.checkfirm.feature.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

object PreferenceIcon {
    val Profile: ImageVector
        get() = ImageVector.Builder(
            name = "profile",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                name = "head",
                fill = SolidColor(Color.White),
                fillAlpha = 0.7f,
            ) {
                moveTo(12.0f, 10.362f)
                curveTo(13.86f, 10.362f, 15.369f, 8.854f, 15.369f, 6.994f)
                curveTo(15.369f, 5.133f, 13.86f, 3.625f, 12.0f, 3.625f)
                curveTo(10.14f, 3.625f, 8.631f, 5.133f, 8.631f, 6.994f)
                curveTo(8.631f, 8.854f, 10.14f, 10.362f, 12.0f, 10.362f)
                close()
            }

            path(
                name = "body",
                fill = SolidColor(Color.White),
                fillAlpha = 0.7f,
            ) {
                moveTo(12.0f, 12.359f)
                curveTo(8.767f, 12.359f, 6.145f, 14.795f, 6.145f, 17.42f)
                verticalLineTo(18.627f)
                curveTo(7.829f, 19.73f, 9.84f, 20.375f, 12.0f, 20.375f)
                curveTo(14.16f, 20.375f, 16.171f, 19.73f, 17.855f, 18.627f)
                verticalLineTo(17.42f)
                curveTo(17.855f, 14.795f, 15.233f, 12.359f, 12.0f, 12.359f)
                close()
            }
        }.build()
}

@Preview
@Composable
private fun PreferenceIconPreview() {
    CheckFirmTheme {
        Surface {
            Row {
                Icon(
                    imageVector = PreferenceIcon.Profile,
                    contentDescription = null
                )
            }
        }
    }
}