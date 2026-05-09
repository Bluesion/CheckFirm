package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object OneIcons {
    val CheckfirmAppIcon: ImageVector
        get() = ImageVector.Builder(
            name = "CheckfirmAppIcon",
            defaultWidth = 128.dp,
            defaultHeight = 128.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            group(
                name = "",
                pivotX = 128f,
                pivotY = 128f,
                scaleX = 0.5f,
                scaleY = 0.5f,
                rotate = 0f,
                translationX = 0f,
                translationY = 0f
            ) {
                path(
                    fill = SolidColor(Color(0xFFFAB200)),
                    stroke = SolidColor(Color(0xFFFAB200)),
                    strokeLineWidth = 24f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(104f, 184f)
                    lineToRelative(-56f, -55.99f)
                }
                path(
                    fill = SolidColor(Color(0xFF005AFF)),
                    stroke = SolidColor(Color(0xFF005AFF)),
                    strokeLineWidth = 24f,
                    strokeLineCap = StrokeCap.Round,
                    strokeLineJoin = StrokeJoin.Round
                ) {
                    moveTo(216f, 72f)
                    lineToRelative(-112f, 112f)
                }
            }
        }.build()

    val CheckfirmDivider: ImageVector
        get() = ImageVector.Builder(
            name = "CheckfirmDivider",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFFC4C6CF))) {
                moveTo(0f, 0f)
                horizontalLineTo(24f)
                verticalLineTo(24f)
                horizontalLineTo(0f)
                close()
            }
        }.build()

    val CheckfirmIcon: ImageVector
        get() = ImageVector.Builder(
            name = "CheckfirmIcon",
            defaultWidth = 128.dp,
            defaultHeight = 128.dp,
            viewportWidth = 256f,
            viewportHeight = 256f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFAB200)),
                stroke = SolidColor(Color(0xFFFAB200)),
                strokeLineWidth = 24f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(104f, 184f)
                lineToRelative(-56f, -55.99f)
            }
            path(
                fill = SolidColor(Color(0xFF005AFF)),
                stroke = SolidColor(Color(0xFF005AFF)),
                strokeLineWidth = 24f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(216f, 72f)
                lineToRelative(-112f, 112f)
            }
        }.build()

    val IcArrange: ImageVector
        get() = ImageVector.Builder(
            name = "IcArrange",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(16f, 17.01f)
                verticalLineTo(10f)
                horizontalLineToRelative(-2f)
                verticalLineToRelative(7.01f)
                horizontalLineToRelative(-3f)
                lineTo(15f, 21f)
                lineToRelative(4f, -3.99f)
                horizontalLineToRelative(-3f)
                close()
                moveTo(9f, 3f)
                lineTo(5f, 6.99f)
                horizontalLineToRelative(3f)
                verticalLineTo(14f)
                horizontalLineToRelative(2f)
                verticalLineTo(6.99f)
                horizontalLineToRelative(3f)
                lineTo(9f, 3f)
                close()
            }
        }.build()

    val IcArrangeHandle: ImageVector
        get() = ImageVector.Builder(
            name = "IcArrangeHandle",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(16.326f, 14.7643f)
                curveTo(16.6177f, 14.4702f, 17.0926f, 14.4684f, 17.3866f, 14.7601f)
                curveTo(17.6807f, 15.0518f, 17.6825f, 15.5267f, 17.3908f, 15.8208f)
                lineTo(17.3908f, 15.8208f)
                lineTo(13.1992f, 20.0454f)
                curveTo(12.543f, 20.7052f, 11.4749f, 20.7063f, 10.8191f, 20.0468f)
                lineTo(10.8191f, 20.0468f)
                lineTo(6.6101f, 15.8218f)
                curveTo(6.3177f, 15.5284f, 6.3186f, 15.0535f, 6.6121f, 14.7612f)
                curveTo(6.9055f, 14.4688f, 7.3804f, 14.4697f, 7.6727f, 14.7632f)
                lineTo(7.6727f, 14.7632f)
                lineTo(11.8822f, 18.9886f)
                curveTo(11.9515f, 19.0584f, 12.0654f, 19.0582f, 12.135f, 18.9883f)
                lineTo(12.135f, 18.9883f)
                close()
                moveTo(10.8027f, 3.9517f)
                curveTo(11.4586f, 3.2957f, 12.5241f, 3.2946f, 13.1785f, 3.9508f)
                lineTo(13.1785f, 3.9508f)
                lineTo(17.4055f, 8.1798f)
                curveTo(17.6983f, 8.4728f, 17.6982f, 8.9476f, 17.4052f, 9.2405f)
                curveTo(17.1122f, 9.5333f, 16.6374f, 9.5332f, 16.3445f, 9.2402f)
                lineTo(16.3445f, 9.2402f)
                lineTo(12.1169f, 5.0106f)
                curveTo(12.0483f, 4.9418f, 11.9338f, 4.9419f, 11.8646f, 5.0111f)
                lineTo(11.8646f, 5.0111f)
                lineTo(7.6566f, 9.2391f)
                curveTo(7.3644f, 9.5327f, 6.8895f, 9.5338f, 6.5959f, 9.2416f)
                curveTo(6.3023f, 8.9494f, 6.3012f, 8.4745f, 6.5934f, 8.1809f)
                lineTo(6.5934f, 8.1809f)
                close()
            }
        }.build()

    val IcBack: ImageVector
        get() = ImageVector.Builder(
            name = "IcBack",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 72.0f,
            viewportHeight = 72.0f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(46.6923f, 12.4566f)
                curveTo(47.5773f, 11.5836f, 47.5893f, 10.1586f, 46.7163f, 9.2736f)
                curveTo(45.8433f, 8.3916f, 44.4183f, 8.3826f, 43.5333f, 9.2556f)
                lineTo(20.1993f, 32.2656f)
                curveTo(19.1913f, 33.2586f, 18.6363f, 34.5876f, 18.6363f, 36.0036f)
                curveTo(18.6363f, 37.4166f, 19.1913f, 38.7486f, 20.1993f, 39.7416f)
                lineTo(43.5333f, 62.7486f)
                curveTo(43.9713f, 63.1776f, 44.5413f, 63.3936f, 45.1143f, 63.3936f)
                curveTo(45.6933f, 63.3936f, 46.2753f, 63.1686f, 46.7163f, 62.7246f)
                curveTo(47.5893f, 61.8396f, 47.5773f, 60.4146f, 46.6923f, 59.5416f)
                lineTo(23.3613f, 36.5346f)
                curveTo(23.1633f, 36.3456f, 23.1363f, 36.1206f, 23.1363f, 36.0036f)
                curveTo(23.1363f, 35.8866f, 23.1633f, 35.6586f, 23.3613f, 35.4696f)
                lineTo(46.6923f, 12.4566f)
                close()
            }
        }.build()

    val IcBell: ImageVector
        get() = ImageVector.Builder(
            name = "IcBell",
            defaultWidth = 200.dp,
            defaultHeight = 200.dp,
            viewportWidth = 32f,
            viewportHeight = 32f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFF7700)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(15.647f, 32.0f)
                curveTo(18.171f, 32.0f, 20.218f, 29.953f, 20.218f, 27.428f)
                curveTo(20.218f, 24.904f, 18.171f, 22.857f, 15.647f, 22.857f)
                curveTo(13.122f, 22.857f, 11.075f, 24.904f, 11.075f, 27.428f)
                curveTo(11.075f, 29.953f, 13.122f, 32.0f, 15.647f, 32.0f)
                close()
            }
            path(
                fill = Brush.radialGradient(
                    0.0f to Color(0xFFFADF73),
                    0.457f to Color(0xFFFFD500),
                    1.0f to Color(0xFFFC9900),
                    center = Offset(11.13f, 7.542f),
                    radius = 21.268f
                ),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(23.189f, 6.172f)
                curveTo(21.865f, 4.848f, 20.367f, 3.945f, 18.694f, 3.462f)
                lineTo(18.694f, 3.048f)
                curveTo(18.694f, 2.206f, 18.397f, 1.488f, 17.802f, 0.893f)
                curveTo(17.207f, 0.298f, 16.488f, 0.0f, 15.647f, 0.0f)
                curveTo(14.805f, 0.0f, 14.087f, 0.298f, 13.492f, 0.893f)
                curveTo(12.897f, 1.488f, 12.599f, 2.206f, 12.599f, 3.048f)
                lineTo(12.599f, 3.462f)
                curveTo(10.927f, 3.945f, 9.428f, 4.848f, 8.104f, 6.172f)
                curveTo(6.021f, 8.255f, 4.98f, 10.769f, 4.98f, 13.714f)
                lineTo(4.98f, 21.943f)
                lineTo(2.491f, 24.929f)
                curveTo(2.302f, 25.156f, 2.184f, 25.417f, 2.138f, 25.71f)
                curveTo(2.109f, 26.003f, 2.157f, 26.283f, 2.282f, 26.551f)
                curveTo(2.407f, 26.819f, 2.595f, 27.032f, 2.844f, 27.191f)
                curveTo(3.094f, 27.349f, 3.366f, 27.429f, 3.662f, 27.429f)
                lineTo(27.631f, 27.429f)
                curveTo(27.927f, 27.429f, 28.2f, 27.349f, 28.449f, 27.191f)
                curveTo(28.699f, 27.032f, 28.886f, 26.819f, 29.011f, 26.551f)
                curveTo(29.137f, 26.283f, 29.181f, 26.003f, 29.143f, 25.71f)
                curveTo(29.105f, 25.417f, 28.991f, 25.156f, 28.802f, 24.929f)
                lineTo(26.313f, 21.943f)
                lineTo(26.313f, 13.714f)
                curveTo(26.313f, 10.769f, 25.272f, 8.255f, 23.189f, 6.172f)
                close()
            }
        }.build()

    val IcCategory: ImageVector
        get() = ImageVector.Builder(
            name = "IcCategory",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(15f, 7f)
                lineTo(15f, 19.97f)
                lineToRelative(-4.21f, -1.81f)
                lineToRelative(-0.79f, -0.34f)
                lineToRelative(-0.79f, 0.34f)
                lineTo(5f, 19.97f)
                lineTo(5f, 7f)
                horizontalLineToRelative(10f)
                moveToRelative(4f, -6f)
                lineTo(8.99f, 1f)
                curveTo(7.89f, 1f, 7f, 1.9f, 7f, 3f)
                horizontalLineToRelative(10f)
                curveToRelative(1.1f, 0f, 2f, 0.9f, 2f, 2f)
                verticalLineToRelative(13f)
                lineToRelative(2f, 1f)
                lineTo(21f, 3f)
                curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
                close()
                moveTo(15f, 5f)
                lineTo(5f, 5f)
                curveToRelative(-1.1f, 0f, -2f, 0.9f, -2f, 2f)
                verticalLineToRelative(16f)
                lineToRelative(7f, -3f)
                lineToRelative(7f, 3f)
                lineTo(17f, 7f)
                curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
                close()
            }
        }.build()

    val IcCheckChip: ImageVector
        get() = ImageVector.Builder(
            name = "IcCheckChip",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1f
            ) {
                moveTo(12f, 12f)
                moveToRelative(-12f, 0f)
                arcToRelative(12f, 12f, 0f, true, true, 24f, 0f)
                arcToRelative(12f, 12f, 0f, true, true, -24f, 0f)
            }
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1f
            ) {
                moveTo(9.189f, 15.939f)
                lineToRelative(-3.127f, -3.128f)
                lineToRelative(-1.061f, 1.061f)
                lineToRelative(4.189f, 4.189f)
                lineToRelative(9f, -9f)
                lineToRelative(-1.061f, -1.061f)
                close()
            }
        }.build()

    val IcChevronRight: ImageVector
        get() = ImageVector.Builder(
            name = "IcChevronRight",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(530f, 479f)
                lineTo(353f, 302f)
                quadTo(344f, 293f, 344.5f, 281f)
                quadTo(345f, 269f, 354f, 260f)
                quadTo(363f, 251f, 375.5f, 251f)
                quadTo(388f, 251f, 397f, 260f)
                lineTo(595f, 458f)
                quadTo(600f, 463f, 602f, 468f)
                quadTo(604f, 473f, 604f, 479f)
                quadTo(604f, 485f, 602f, 490f)
                quadTo(600f, 495f, 595f, 500f)
                lineTo(396f, 699f)
                quadTo(387f, 708f, 375f, 707.5f)
                quadTo(363f, 707f, 354f, 698f)
                quadTo(345f, 689f, 345f, 676.5f)
                quadTo(345f, 664f, 354f, 655f)
                lineTo(530f, 479f)
                close()
            }
        }.build()

    val IcChevronRightSmall: ImageVector
        get() = ImageVector.Builder(
            name = "IcChevronRightSmall",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(530f, 479f)
                lineTo(353f, 302f)
                quadTo(344f, 293f, 344.5f, 281f)
                quadTo(345f, 269f, 354f, 260f)
                quadTo(363f, 251f, 375.5f, 251f)
                quadTo(388f, 251f, 397f, 260f)
                lineTo(595f, 458f)
                quadTo(600f, 463f, 602f, 468f)
                quadTo(604f, 473f, 604f, 479f)
                quadTo(604f, 485f, 602f, 490f)
                quadTo(600f, 495f, 595f, 500f)
                lineTo(396f, 699f)
                quadTo(387f, 708f, 375f, 707.5f)
                quadTo(363f, 707f, 354f, 698f)
                quadTo(345f, 689f, 345f, 676.5f)
                quadTo(345f, 664f, 354f, 655f)
                lineTo(530f, 479f)
                close()
            }
        }.build()

    val IcClear: ImageVector
        get() = ImageVector.Builder(
            name = "IcClear",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(480f, 536f)
                lineToRelative(116f, 116f)
                quadToRelative(11f, 11f, 28f, 11f)
                quadToRelative(17f, 0f, 28f, -11f)
                quadToRelative(11f, -11f, 11f, -28f)
                quadToRelative(0f, -17f, -11f, -28f)
                lineToRelative(-116f, -116f)
                lineToRelative(116f, -116f)
                quadToRelative(11f, -11f, 11f, -28f)
                quadToRelative(0f, -17f, -11f, -28f)
                quadToRelative(-11f, -11f, -28f, -11f)
                quadToRelative(-17f, 0f, -28f, 11f)
                lineToRelative(-116f, 116f)
                lineToRelative(-116f, -116f)
                quadToRelative(-11f, -11f, -28f, -11f)
                quadToRelative(-17f, 0f, -28f, 11f)
                quadToRelative(-11f, 11f, -11f, 28f)
                quadToRelative(0f, 17f, 11f, 28f)
                lineToRelative(116f, 116f)
                lineToRelative(-116f, 116f)
                quadToRelative(-11f, 11f, -11f, 28f)
                quadToRelative(0f, 17f, 11f, 28f)
                quadToRelative(11f, 11f, 28f, 11f)
                quadToRelative(17f, 0f, 28f, -11f)
                lineToRelative(116f, -116f)
                close()
                moveTo(480f, 880f)
                quadToRelative(-83f, 0f, -156f, -31.5f)
                quadToRelative(-73f, -31.5f, -127f, -85.5f)
                quadToRelative(-54f, -54f, -85.5f, -127f)
                quadToRelative(-31.5f, -73f, -31.5f, -156f)
                quadToRelative(0f, -83f, 31.5f, -156f)
                quadToRelative(31.5f, -73f, 85.5f, -127f)
                quadToRelative(54f, -54f, 127f, -85.5f)
                quadToRelative(73f, -31.5f, 156f, -31.5f)
                quadToRelative(83f, 0f, 156f, 31.5f)
                quadToRelative(73f, 31.5f, 127f, 85.5f)
                quadToRelative(54f, 54f, 85.5f, 127f)
                quadToRelative(31.5f, 73f, 31.5f, 156f)
                quadToRelative(0f, 83f, -31.5f, 156f)
                quadToRelative(-31.5f, 73f, -85.5f, 127f)
                quadToRelative(-54f, 54f, -127f, 85.5f)
                quadToRelative(-73f, 31.5f, -156f, 85.5f)
                close()
            }
        }.build()

    val IcClose: ImageVector
        get() = ImageVector.Builder(
            name = "IcClose",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(13.06f, 11.9994f)
                lineTo(19.031f, 6.0304f)
                curveTo(19.324f, 5.7374f, 19.324f, 5.2634f, 19.031f, 4.9704f)
                curveTo(18.738f, 4.6774f, 18.262f, 4.6774f, 17.97f, 4.9704f)
                lineTo(11.999f, 10.9384f)
                lineTo(6.03f, 4.9704f)
                curveTo(5.737f, 4.6774f, 5.262f, 4.6774f, 4.969f, 4.9704f)
                curveTo(4.676f, 5.2634f, 4.676f, 5.7374f, 4.969f, 6.0304f)
                lineTo(10.939f, 11.9994f)
                lineTo(4.969f, 17.9704f)
                curveTo(4.676f, 18.2634f, 4.676f, 18.7374f, 4.969f, 19.0304f)
                curveTo(5.116f, 19.1774f, 5.307f, 19.2494f, 5.499f, 19.2494f)
                curveTo(5.692f, 19.2494f, 5.883f, 19.1774f, 6.03f, 19.0304f)
                lineTo(11.999f, 13.0614f)
                lineTo(17.97f, 19.0304f)
                curveTo(18.116f, 19.1774f, 18.308f, 19.2494f, 18.5f, 19.2494f)
                curveTo(18.692f, 19.2494f, 18.884f, 19.1774f, 19.031f, 19.0304f)
                curveTo(19.324f, 18.7374f, 19.324f, 18.2634f, 19.031f, 17.9704f)
                lineTo(13.06f, 11.9994f)
                close()
            }
        }.build()

    val IcCopy: ImageVector
        get() = ImageVector.Builder(
            name = "IcCopy",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(360f, 720f)
                quadTo(327f, 720f, 303.5f, 696.5f)
                quadTo(280f, 673f, 280f, 640f)
                lineTo(280f, 160f)
                quadTo(280f, 127f, 303.5f, 103.5f)
                quadTo(327f, 80f, 360f, 80f)
                lineTo(720f, 80f)
                quadTo(753f, 80f, 776.5f, 103.5f)
                quadTo(800f, 127f, 800f, 160f)
                lineTo(800f, 640f)
                quadTo(800f, 673f, 776.5f, 696.5f)
                quadTo(753f, 720f, 720f, 720f)
                lineTo(360f, 720f)
                close()
                moveTo(360f, 640f)
                lineTo(720f, 640f)
                quadTo(720f, 640f, 720f, 640f)
                quadTo(720f, 640f, 720f, 640f)
                lineTo(720f, 160f)
                quadTo(720f, 160f, 720f, 160f)
                quadTo(720f, 160f, 720f, 160f)
                lineTo(360f, 160f)
                quadTo(360f, 160f, 360f, 160f)
                quadTo(360f, 160f, 360f, 160f)
                lineTo(360f, 640f)
                quadTo(360f, 640f, 360f, 640f)
                quadTo(360f, 640f, 360f, 640f)
                close()
                moveTo(200f, 880f)
                quadTo(167f, 880f, 143.5f, 856.5f)
                quadTo(120f, 833f, 120f, 800f)
                lineTo(120f, 280f)
                quadTo(120f, 263f, 131.5f, 251.5f)
                quadTo(143f, 240f, 160f, 240f)
                quadTo(177f, 240f, 188.5f, 251.5f)
                quadTo(200f, 263f, 200f, 280f)
                lineTo(200f, 800f)
                quadTo(200f, 800f, 200f, 800f)
                quadTo(200f, 800f, 200f, 800f)
                lineTo(600f, 800f)
                quadTo(617f, 800f, 628.5f, 811.5f)
                quadTo(640f, 823f, 640f, 840f)
                quadTo(640f, 857f, 628.5f, 868.5f)
                quadTo(617f, 880f, 600f, 880f)
                lineTo(200f, 880f)
                close()
                moveTo(360f, 640f)
                quadTo(360f, 640f, 360f, 640f)
                quadTo(360f, 640f, 360f, 640f)
                lineTo(360f, 160f)
                quadTo(360f, 160f, 360f, 160f)
                quadTo(360f, 160f, 360f, 160f)
                lineTo(360f, 160f)
                quadTo(360f, 160f, 360f, 160f)
                quadTo(360f, 160f, 360f, 160f)
                lineTo(360f, 640f)
                quadTo(360f, 640f, 360f, 640f)
                quadTo(360f, 640f, 360f, 640f)
                close()
            }
        }.build()

    val IcHelp: ImageVector
        get() = ImageVector.Builder(
            name = "IcHelp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 26f,
            viewportHeight = 26f
        ).apply {
            group(
                name = "",
                pivotX = 0f,
                pivotY = 0f,
                scaleX = 1f,
                scaleY = 1f,
                rotate = 0f,
                translationX = 1f,
                translationY = 1f
            ) {
                path(
                    fill = SolidColor(Color.White),
                    stroke = SolidColor(Color(0x00000000)),
                    strokeLineWidth = 0f
                ) {
                    moveTo(12f, 2f)
                    curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
                    reflectiveCurveToRelative(4.48f, 10f, 10f, 10f)
                    reflectiveCurveToRelative(10f, -4.48f, 10f, -10f)
                    reflectiveCurveTo(17.52f, 2f, 12f, 2f)
                    close()
                    moveTo(13f, 19f)
                    horizontalLineToRelative(-2f)
                    verticalLineToRelative(-2f)
                    horizontalLineToRelative(2f)
                    verticalLineToRelative(2f)
                    close()
                    moveTo(15.07f, 11.25f)
                    lineToRelative(-0.9f, 0.92f)
                    curveTo(13.45f, 12.9f, 13f, 13.5f, 13f, 15f)
                    horizontalLineToRelative(-2f)
                    verticalLineToRelative(-0.5f)
                    curveToRelative(0f, -1.1f, 0.45f, -2.1f, 1.17f, -2.83f)
                    lineToRelative(1.24f, -1.26f)
                    curveToRelative(0.37f, -0.36f, 0.59f, -0.86f, 0.59f, -1.41f)
                    curveToRelative(0f, -1.1f, -0.9f, -2f, -2f, -2f)
                    reflectiveCurveToRelative(-2f, 0.9f, -2f, 2f)
                    lineTo(8f, 9f)
                    curveToRelative(0f, -2.21f, 1.79f, -4f, 4f, -4f)
                    reflectiveCurveToRelative(4f, 1.79f, 4f, 4f)
                    curveToRelative(0f, 0.88f, -0.36f, 1.68f, -0.93f, 2.25f)
                    close()
                }
            }
        }.build()

    val IcHelpDevice: ImageVector
        get() = ImageVector.Builder(
            name = "IcHelpDevice",
            defaultWidth = 28.dp,
            defaultHeight = 28.dp,
            viewportWidth = 28.0f,
            viewportHeight = 28.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF8B8B8B)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(24.856f, 3.144f)
                curveTo(23.13f, 1.5216f, 20.7739f, 0.6012f, 18.1738f, 0.2569f)
                curveTo(15.6907f, -0.0722f, 14.0f, 0.0088f, 14.0f, 0.0088f)
                curveTo(14.0f, 0.0088f, 12.309f, -0.0722f, 9.8258f, 0.2569f)
                curveTo(7.2258f, 0.6012f, 4.8698f, 1.5216f, 3.1443f, 3.144f)
                curveTo(1.5215f, 4.8698f, 0.6012f, 7.2259f, 0.2567f, 9.8259f)
                curveTo(-0.0724f, 12.3089f, 0.0089f, 13.9999f, 0.0089f, 13.9999f)
                curveTo(0.0089f, 13.9999f, -0.0724f, 15.6907f, 0.2567f, 18.1739f)
                curveTo(0.6012f, 20.7737f, 1.5215f, 23.13f, 3.1441f, 24.8558f)
                curveTo(4.8698f, 26.4784f, 7.2258f, 27.3986f, 9.8258f, 27.7433f)
                curveTo(12.309f, 28.0722f, 14.0f, 27.9912f, 14.0f, 27.9912f)
                curveTo(14.0f, 27.9912f, 15.6907f, 28.0722f, 18.1738f, 27.7433f)
                curveTo(20.7739f, 27.3986f, 23.13f, 26.4784f, 24.8556f, 24.8558f)
                curveTo(26.4786f, 23.13f, 27.3988f, 20.7737f, 27.7433f, 18.1739f)
                curveTo(28.0722f, 15.6907f, 27.9912f, 13.9999f, 27.9912f, 13.9999f)
                curveTo(27.9912f, 13.9999f, 28.0722f, 12.3089f, 27.7433f, 9.8259f)
                curveTo(27.3988f, 7.2259f, 26.4786f, 4.8698f, 24.856f, 3.144f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(15.0065f, 18.0801f)
                curveTo(15.0065f, 18.5045f, 14.6224f, 18.8391f, 14.1824f, 18.7547f)
                curveTo(13.8561f, 18.6915f, 13.6315f, 18.3872f, 13.6315f, 18.0544f)
                lineTo(13.6315f, 13.8735f)
                lineTo(12.7909f, 13.8735f)
                curveTo(12.4582f, 13.8735f, 12.1529f, 13.6489f, 12.0906f, 13.3217f)
                curveTo(12.0063f, 12.8826f, 12.3408f, 12.4985f, 12.7653f, 12.4985f)
                lineTo(14.5482f, 12.4985f)
                curveTo(14.8012f, 12.4985f, 15.0065f, 12.7029f, 15.0065f, 12.9568f)
                lineTo(15.0065f, 18.0801f)
                close()
                moveTo(14.0f, 9.1811f)
                curveTo(14.5069f, 9.1811f, 14.9167f, 9.5918f, 14.9167f, 10.0977f)
                curveTo(14.9167f, 10.6038f, 14.5069f, 11.0144f, 14.0f, 11.0144f)
                curveTo(13.494f, 11.0144f, 13.0833f, 10.6038f, 13.0833f, 10.0977f)
                curveTo(13.0833f, 9.5918f, 13.494f, 9.1811f, 14.0f, 9.1811f)
                lineTo(14.0f, 9.1811f)
                close()
                moveTo(14.0f, 6.3229f)
                curveTo(9.7668f, 6.3229f, 6.3229f, 9.7668f, 6.3229f, 14.0f)
                curveTo(6.3229f, 18.2332f, 9.7668f, 21.6771f, 14.0f, 21.6771f)
                curveTo(18.2341f, 21.6771f, 21.6771f, 18.2332f, 21.6771f, 14.0f)
                curveTo(21.6771f, 9.7668f, 18.2341f, 6.3229f, 14.0f, 6.3229f)
                lineTo(14.0f, 6.3229f)
                close()
            }
        }.build()

    val IcHelpManual: ImageVector
        get() = ImageVector.Builder(
            name = "IcHelpManual",
            defaultWidth = 28.dp,
            defaultHeight = 28.dp,
            viewportWidth = 28.0f,
            viewportHeight = 28.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFA64C)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(24.856f, 3.144f)
                curveTo(23.13f, 1.5216f, 20.7739f, 0.6012f, 18.1738f, 0.2569f)
                curveTo(15.6907f, -0.0722f, 14.0f, 0.0088f, 14.0f, 0.0088f)
                curveTo(14.0f, 0.0088f, 12.309f, -0.0722f, 9.8258f, 0.2569f)
                curveTo(7.2258f, 0.6012f, 4.8698f, 1.5216f, 3.1443f, 3.144f)
                curveTo(1.5215f, 4.8698f, 0.6012f, 7.2259f, 0.2567f, 9.8259f)
                curveTo(-0.0724f, 12.3089f, 0.0089f, 13.9999f, 0.0089f, 13.9999f)
                curveTo(0.0089f, 13.9999f, -0.0724f, 15.6907f, 0.2567f, 18.1739f)
                curveTo(0.6012f, 20.7737f, 1.5215f, 23.13f, 3.1441f, 24.8558f)
                curveTo(4.8698f, 26.4784f, 7.2258f, 27.3986f, 9.8258f, 27.7433f)
                curveTo(12.309f, 28.0722f, 14.0f, 27.9912f, 14.0f, 27.9912f)
                curveTo(14.0f, 27.9912f, 15.6907f, 28.0722f, 18.1738f, 27.7433f)
                curveTo(20.7739f, 27.3986f, 23.13f, 26.4784f, 24.8556f, 24.8558f)
                curveTo(26.4786f, 23.13f, 27.3988f, 20.7737f, 27.7433f, 18.1739f)
                curveTo(28.0722f, 15.6907f, 27.9912f, 13.9999f, 27.9912f, 13.9999f)
                curveTo(27.9912f, 13.9999f, 28.0722f, 12.3089f, 27.7433f, 9.8259f)
                curveTo(27.3988f, 7.2259f, 26.4786f, 4.8698f, 24.856f, 3.144f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(16.3669f, 19.5056f)
                lineTo(16.3669f, 20.093f)
                curveTo(16.3669f, 21.0203f, 15.7581f, 21.0575f, 15.3753f, 21.1779f)
                curveTo(15.169f, 21.7381f, 14.6328f, 22.1374f, 14.0008f, 22.1374f)
                curveTo(13.3689f, 22.1374f, 12.8326f, 21.7373f, 12.6264f, 21.1779f)
                curveTo(12.2428f, 21.0575f, 11.6339f, 21.0212f, 11.6339f, 20.093f)
                lineTo(11.6339f, 20.093f)
                lineTo(11.6347f, 20.093f)
                lineTo(11.6347f, 19.5056f)
                lineTo(16.3669f, 19.5056f)
                close()
                moveTo(14.0f, 5.8626f)
                curveTo(17.1136f, 5.8626f, 19.6389f, 8.3879f, 19.6389f, 11.5015f)
                curveTo(19.6389f, 13.7694f, 18.2983f, 15.7238f, 16.3661f, 16.6198f)
                lineTo(16.3661f, 16.6198f)
                lineTo(16.3661f, 18.1205f)
                lineTo(11.6339f, 18.1205f)
                lineTo(11.6339f, 16.6198f)
                curveTo(9.7026f, 15.7247f, 8.3611f, 13.7702f, 8.3611f, 11.5015f)
                curveTo(8.3611f, 8.3879f, 10.8864f, 5.8626f, 14.0f, 5.8626f)
                close()
            }
        }.build()

    val IcInfoCatcher: ImageVector
        get() = ImageVector.Builder(
            name = "IcInfoCatcher",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(190f, 760f)
                quadTo(177.25f, 760f, 168.63f, 751.32f)
                quadTo(160f, 742.65f, 160f, 729.82f)
                quadTo(160f, 717f, 168.63f, 708.5f)
                quadTo(177.25f, 700f, 190f, 700f)
                lineTo(240f, 700f)
                lineTo(240f, 396f)
                quadTo(240f, 312f, 289.5f, 245.5f)
                quadTo(339f, 179f, 420f, 162f)
                lineTo(420f, 140f)
                quadTo(420f, 115f, 437.5f, 97.5f)
                quadTo(455f, 80f, 480f, 80f)
                quadTo(505f, 80f, 522.5f, 97.5f)
                quadTo(540f, 115f, 540f, 140f)
                lineTo(540f, 162f)
                quadTo(621f, 179f, 670.5f, 245.5f)
                quadTo(720f, 312f, 720f, 396f)
                lineTo(720f, 700f)
                lineTo(770f, 700f)
                quadTo(782.75f, 700f, 791.38f, 708.68f)
                quadTo(800f, 717.35f, 800f, 730.18f)
                quadTo(800f, 743f, 791.38f, 751.5f)
                quadTo(782.75f, 760f, 770f, 760f)
                lineTo(190f, 760f)
                close()
                moveTo(480f, 880f)
                quadTo(447f, 880f, 423.5f, 856.5f)
                quadTo(400f, 833f, 400f, 800f)
                lineTo(560f, 800f)
                quadTo(560f, 833f, 536.5f, 856.5f)
                quadTo(513f, 880f, 480f, 880f)
                close()
                moveTo(300f, 700f)
                lineTo(660f, 700f)
                lineTo(660f, 396f)
                quadTo(660f, 321f, 607.5f, 268.5f)
                quadTo(555f, 216f, 480f, 216f)
                quadTo(405f, 216f, 352.5f, 268.5f)
                quadTo(300f, 321f, 300f, 396f)
                lineTo(300f, 700f)
                close()
            }
        }.build()

    val IcPlay: ImageVector
        get() = ImageVector.Builder(
            name = "IcPlay",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(320f, 687f)
                lineTo(320f, 273f)
                quadTo(320f, 256f, 332f, 244.5f)
                quadTo(344f, 233f, 360f, 233f)
                quadTo(365f, 233f, 370.5f, 234.5f)
                quadTo(376f, 236f, 381f, 239f)
                lineTo(707f, 446f)
                quadTo(716f, 452f, 720.5f, 461f)
                quadTo(725f, 470f, 725f, 480f)
                quadTo(725f, 490f, 720.5f, 499f)
                quadTo(716f, 508f, 707f, 514f)
                lineTo(381f, 721f)
                quadTo(376f, 724f, 370.5f, 725.5f)
                quadTo(365f, 727f, 360f, 727f)
                quadTo(344f, 727f, 332f, 715.5f)
                quadTo(320f, 704f, 320f, 687f)
                close()
                moveTo(400f, 614f)
                lineTo(610f, 480f)
                lineTo(400f, 346f)
                lineTo(400f, 614f)
                close()
            }
        }.build()

    val IcProfile: ImageVector
        get() = ImageVector.Builder(
            name = "IcProfile",
            defaultWidth = 56.dp,
            defaultHeight = 56.dp,
            viewportWidth = 56.0f,
            viewportHeight = 56.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xffffffff)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(27.9971f, 0.0f)
                curveTo(12.533f, 0.0f, 0.0f, 12.5343f, 0.0f, 28.0f)
                curveTo(0.0f, 43.4657f, 12.533f, 56.0f, 27.9971f, 56.0f)
                curveTo(43.4613f, 56.0f, 56.0f, 43.4629f, 56.0f, 28.0f)
                curveTo(56.0f, 12.5371f, 43.4613f, 0.0f, 27.9971f, 0.0f)
                close()
                moveTo(28.0f, 11.5294f)
                curveTo(31.6346f, 11.5294f, 34.5882f, 14.472f, 34.5882f, 18.1176f)
                curveTo(34.5882f, 21.755f, 31.6401f, 24.7059f, 28.0f, 24.7059f)
                curveTo(24.3599f, 24.7059f, 21.4118f, 21.7606f, 21.4118f, 18.1176f)
                curveTo(21.4145f, 14.472f, 24.3571f, 11.5294f, 28.0f, 11.5294f)
                close()
                moveTo(27.9956f, 47.7647f)
                curveTo(21.9948f, 47.7647f, 16.646f, 45.1114f, 13.1765f, 40.9667f)
                curveTo(14.6958f, 34.4867f, 20.7498f, 29.6471f, 27.9956f, 29.6471f)
                curveTo(35.2443f, 29.6471f, 41.3101f, 34.4895f, 42.8235f, 40.9667f)
                curveTo(39.3452f, 45.1085f, 33.9993f, 47.7647f, 27.9956f, 47.7647f)
                close()
            }
        }.build()

    val IcReport: ImageVector
        get() = ImageVector.Builder(
            name = "IcReport",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF341000)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(480f, 680f)
                quadTo(497f, 680f, 508.5f, 668.5f)
                quadTo(520f, 657f, 520f, 640f)
                quadTo(520f, 623f, 508.5f, 611.5f)
                quadTo(497f, 600f, 480f, 600f)
                quadTo(463f, 600f, 451.5f, 611.5f)
                quadTo(440f, 623f, 440f, 640f)
                quadTo(440f, 657f, 451.5f, 668.5f)
                quadTo(463f, 680f, 480f, 680f)
                close()
                moveTo(480f, 520f)
                quadTo(497f, 520f, 508.5f, 508.5f)
                quadTo(520f, 497f, 520f, 480f)
                lineTo(520f, 320f)
                quadTo(520f, 303f, 508.5f, 291.5f)
                quadTo(497f, 280f, 480f, 280f)
                quadTo(463f, 280f, 451.5f, 291.5f)
                quadTo(440f, 303f, 440f, 320f)
                lineTo(440f, 480f)
                quadTo(440f, 497f, 451.5f, 508.5f)
                quadTo(463f, 520f, 480f, 520f)
                close()
                moveTo(363f, 840f)
                quadTo(347f, 840f, 332.5f, 834f)
                quadTo(318f, 828f, 307f, 817f)
                lineTo(143f, 653f)
                quadTo(132f, 642f, 126f, 627.5f)
                quadTo(120f, 613f, 120f, 597f)
                lineTo(120f, 363f)
                quadTo(120f, 347f, 126f, 332.5f)
                quadTo(132f, 318f, 143f, 307f)
                lineTo(307f, 143f)
                quadTo(318f, 132f, 332.5f, 126f)
                quadTo(347f, 120f, 363f, 120f)
                lineTo(597f, 120f)
                quadTo(613f, 120f, 627.5f, 126f)
                quadTo(642f, 132f, 653f, 143f)
                lineTo(817f, 307f)
                quadTo(828f, 318f, 834f, 332.5f)
                quadTo(840f, 347f, 840f, 363f)
                lineTo(840f, 597f)
                quadTo(840f, 613f, 834f, 627.5f)
                quadTo(828f, 642f, 817f, 653f)
                lineTo(653f, 817f)
                quadTo(642f, 828f, 627.5f, 834f)
                quadTo(613f, 840f, 597f, 840f)
                lineTo(363f, 840f)
                close()
                moveTo(364f, 760f)
                lineTo(596f, 760f)
                lineTo(760f, 596f)
                lineTo(760f, 364f)
                lineTo(596f, 200f)
                lineTo(364f, 200f)
                lineTo(200f, 364f)
                lineTo(200f, 596f)
                lineTo(364f, 760f)
                close()
            }
        }.build()

    val IcSend: ImageVector
        get() = ImageVector.Builder(
            name = "IcSend",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(15.0523f, 20.2754f)
                lineTo(21.4783f, 4.9594f)
                curveTo(21.7573f, 4.2694f, 21.7163f, 3.5784f, 21.3713f, 3.0624f)
                curveTo(20.9053f, 2.3744f, 19.9753f, 2.1394f, 19.0343f, 2.5204f)
                lineTo(3.7253f, 8.9524f)
                curveTo(2.8393f, 9.3144f, 2.3223f, 9.9034f, 2.3383f, 10.5264f)
                curveTo(2.3473f, 10.8644f, 2.5243f, 11.4844f, 3.6093f, 11.8634f)
                lineTo(8.9683f, 13.6474f)
                lineTo(13.7253f, 8.8904f)
                curveTo(14.1083f, 8.5084f, 14.7283f, 8.5084f, 15.1123f, 8.8904f)
                curveTo(15.4963f, 9.2734f, 15.4963f, 9.8934f, 15.1123f, 10.2764f)
                lineTo(10.3523f, 15.0374f)
                lineTo(12.1463f, 20.3894f)
                curveTo(12.5423f, 21.5374f, 13.2283f, 21.6614f, 13.5063f, 21.6614f)
                curveTo(14.1273f, 21.6574f, 14.6893f, 21.1534f, 15.0523f, 20.2754f)
                close()
            }
        }.build()

    val IcSherlock: ImageVector
        get() = ImageVector.Builder(
            name = "IcSherlock",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 48f,
            viewportHeight = 48f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color.White),
                strokeLineWidth = 4f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(24f, 6f)
                curveTo(14.059f, 6f, 6f, 14.059f, 6f, 24f)
                curveTo(6f, 33.941f, 14.059f, 42f, 24f, 42f)
                verticalLineTo(42f)
                curveTo(33.941f, 42f, 42f, 33.941f, 42f, 24f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.White),
                strokeLineWidth = 4f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(24f, 15f)
                curveTo(19.029f, 15f, 15f, 19.029f, 15f, 24f)
                curveTo(15f, 28.971f, 19.029f, 33f, 24f, 33f)
                curveTo(28.971f, 33f, 33f, 28.971f, 33f, 24f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.White),
                strokeLineWidth = 4f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(24f, 24f)
                lineTo(30.3f, 17.694f)
            }
            path(
                fill = null,
                stroke = SolidColor(Color.White),
                strokeLineWidth = 4f,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(30.3f, 11.426f)
                verticalLineTo(17.7f)
                horizontalLineTo(36.625f)
                lineTo(42f, 12.3f)
                horizontalLineTo(35.703f)
                verticalLineTo(6f)
                lineTo(30.3f, 11.426f)
                close()
            }
        }.build()

    val IcSherlockFailFaceIcon: ImageVector
        get() = ImageVector.Builder(
            name = "IcSherlockFailFaceIcon",
            defaultWidth = 200.dp,
            defaultHeight = 200.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(12f, 22f)
                curveTo(17.523f, 22f, 22f, 17.523f, 22f, 12f)
                curveTo(22f, 6.477f, 17.523f, 2f, 12f, 2f)
                curveTo(6.477f, 2f, 2f, 6.477f, 2f, 12f)
                curveTo(2f, 17.523f, 6.477f, 22f, 12f, 22f)
                close()
            }
            group(
                name = "animated",
                pivotX = 0f,
                pivotY = 0f,
                scaleX = 1f,
                scaleY = 1f,
                rotate = 0f,
                translationX = 0f,
                translationY = 0f
            ) {
                path(
                    fill = SolidColor(Color.White),
                    stroke = SolidColor(Color(0x00000000)),
                    strokeLineWidth = 0f
                ) {
                    moveTo(7.55f, 9.6f)
                    curveTo(7.219f, 9.352f, 7.152f, 8.881f, 7.4f, 8.55f)
                    curveTo(7.649f, 8.219f, 8.119f, 8.152f, 8.45f, 8.4f)
                    lineTo(10.45f, 9.9f)
                    curveTo(10.639f, 10.042f, 10.75f, 10.264f, 10.75f, 10.5f)
                    curveTo(10.75f, 10.736f, 10.639f, 10.958f, 10.45f, 11.1f)
                    lineTo(8.45f, 12.6f)
                    curveTo(8.119f, 12.849f, 7.649f, 12.781f, 7.4f, 12.45f)
                    curveTo(7.152f, 12.119f, 7.219f, 11.649f, 7.55f, 11.4f)
                    lineTo(8.75f, 10.5f)
                    lineTo(7.55f, 9.6f)
                    close()
                }
                path(
                    fill = SolidColor(Color.White),
                    stroke = SolidColor(Color(0x00000000)),
                    strokeLineWidth = 0f
                ) {
                    moveTo(16.6f, 8.55f)
                    curveTo(16.849f, 8.881f, 16.781f, 9.352f, 16.45f, 9.6f)
                    lineTo(15.25f, 10.5f)
                    lineTo(16.45f, 11.4f)
                    curveTo(16.781f, 11.649f, 16.849f, 12.119f, 16.6f, 12.45f)
                    curveTo(16.351f, 12.781f, 15.881f, 12.849f, 15.55f, 12.6f)
                    lineTo(13.55f, 11.1f)
                    curveTo(13.361f, 10.958f, 13.25f, 10.736f, 13.25f, 10.5f)
                    curveTo(13.25f, 10.264f, 13.361f, 10.042f, 13.55f, 9.9f)
                    lineTo(15.55f, 8.4f)
                    curveTo(15.881f, 8.152f, 16.351f, 8.219f, 16.6f, 8.55f)
                    close()
                }
                path(
                    fill = SolidColor(Color.White),
                    stroke = SolidColor(Color(0x00000000)),
                    strokeLineWidth = 0f
                ) {
                    moveTo(15.53f, 16.53f)
                    curveTo(15.238f, 16.823f, 14.763f, 16.823f, 14.47f, 16.53f)
                    lineTo(14.0f, 16.06f)
                    curveTo(13.439f, 16.59f, 12.561f, 16.59f, 12.0f, 16.06f)
                    curveTo(11.439f, 16.59f, 10.561f, 16.59f, 10.0f, 16.06f)
                    lineTo(9.53f, 16.53f)
                    curveTo(9.237f, 16.823f, 8.763f, 16.823f, 8.47f, 16.53f)
                    curveTo(8.177f, 16.237f, 8.177f, 15.763f, 8.47f, 15.47f)
                    lineTo(8.97f, 14.97f)
                    curveTo(9.529f, 14.411f, 10.429f, 14.401f, 11.0f, 14.94f)
                    curveTo(11.561f, 14.41f, 12.439f, 14.41f, 13.0f, 14.94f)
                    curveTo(13.571f, 14.401f, 14.471f, 14.411f, 15.03f, 14.97f)
                    lineTo(15.53f, 15.47f)
                    curveTo(15.823f, 15.763f, 15.823f, 16.237f, 15.53f, 16.53f)
                    close()
                }
            }
        }.build()

    val IcSherlockLoadingFaceIcon: ImageVector
        get() = ImageVector.Builder(
            name = "IcSherlockLoadingFaceIcon",
            defaultWidth = 200.dp,
            defaultHeight = 200.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            group(
                name = "rotating_border_group",
                pivotX = 12f,
                pivotY = 12f,
                scaleX = 1f,
                scaleY = 1f,
                rotate = 0f,
                translationX = 0f,
                translationY = 0f
            ) {
                path(
                    fill = null,
                    stroke = SolidColor(Color(0xFF5DAAF2)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round
                ) {
                    moveTo(12f, 2f)
                    arcTo(10f, 10f, 0f, false, true, 22f, 12f)
                }
                path(
                    fill = null,
                    stroke = SolidColor(Color(0xFF5DAAF2)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round
                ) {
                    moveTo(22f, 12f)
                    arcTo(10f, 10f, 0f, false, true, 12f, 22f)
                }
                path(
                    fill = null,
                    stroke = SolidColor(Color(0xFF5DAAF2)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round
                ) {
                    moveTo(12f, 22f)
                    arcTo(10f, 10f, 0f, false, true, 2f, 12f)
                }
                path(
                    fill = null,
                    stroke = SolidColor(Color(0xFF0072D2)),
                    strokeLineWidth = 2f,
                    strokeLineCap = StrokeCap.Round
                ) {
                    moveTo(2f, 12f)
                    arcTo(10f, 10f, 0f, false, true, 12f, 2f)
                }
            }
            path(
                fill = SolidColor(Color(0xFFC1C1C1)),
                stroke = null,
                strokeLineWidth = 0f,
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(12f, 19.583f)
                curveTo(16.188f, 19.583f, 19.583f, 16.188f, 19.583f, 12f)
                curveTo(19.583f, 7.812f, 16.188f, 4.417f, 12f, 4.417f)
                curveTo(7.812f, 4.417f, 4.417f, 7.812f, 4.417f, 12f)
                curveTo(4.417f, 16.188f, 7.812f, 19.583f, 12f, 19.583f)
                close()
            }
            path(fill = SolidColor(Color.White), stroke = null, strokeLineWidth = 0f) {
                moveTo(9.512f, 11.645f)
                curveTo(9.97f, 11.645f, 10.341f, 11.088f, 10.341f, 10.401f)
                curveTo(10.341f, 9.713f, 9.97f, 9.156f, 9.512f, 9.156f)
                curveTo(9.054f, 9.156f, 8.682f, 9.713f, 8.682f, 10.401f)
                curveTo(8.682f, 11.088f, 9.054f, 11.645f, 9.512f, 11.645f)
                close()
            }
            path(fill = SolidColor(Color.White), stroke = null, strokeLineWidth = 0f) {
                moveTo(15.317f, 10.401f)
                curveTo(15.317f, 11.088f, 14.946f, 11.645f, 14.488f, 11.645f)
                curveTo(14.03f, 11.645f, 13.659f, 11.088f, 13.659f, 10.401f)
                curveTo(13.659f, 9.713f, 14.03f, 9.156f, 14.488f, 9.156f)
                curveTo(14.946f, 9.156f, 15.317f, 9.713f, 15.317f, 10.401f)
                close()
            }
            path(fill = SolidColor(Color.White), stroke = null, strokeLineWidth = 0f) {
                moveTo(8.941f, 14.539f)
                curveTo(9.175f, 14.223f, 9.62f, 14.157f, 9.935f, 14.391f)
                curveTo(10.525f, 14.828f, 11.236f, 15.08f, 12f, 15.08f)
                curveTo(12.764f, 15.08f, 13.475f, 14.828f, 14.065f, 14.391f)
                curveTo(14.38f, 14.157f, 14.825f, 14.223f, 15.059f, 14.539f)
                curveTo(15.293f, 14.854f, 15.227f, 15.299f, 14.911f, 15.533f)
                curveTo(14.09f, 16.142f, 13.086f, 16.502f, 12f, 16.502f)
                curveTo(10.914f, 16.502f, 9.909f, 16.142f, 9.089f, 15.533f)
                curveTo(8.773f, 15.299f, 8.707f, 14.854f, 8.941f, 14.539f)
                close()
            }
        }.build()

    val IcSherlockNormalFaceIcon: ImageVector
        get() = ImageVector.Builder(
            name = "IcSherlockNormalFaceIcon",
            defaultWidth = 200.dp,
            defaultHeight = 200.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF0381FE)), stroke = null, strokeLineWidth = 0f) {
                moveTo(12f, 22f)
                curveTo(17.523f, 22f, 22f, 17.523f, 22f, 12f)
                curveTo(22f, 6.477f, 17.523f, 2f, 12f, 2f)
                curveTo(6.477f, 2f, 2f, 6.477f, 2f, 12f)
                curveTo(2f, 17.523f, 6.477f, 22f, 12f, 22f)
                close()
            }
            path(fill = SolidColor(Color.White), stroke = null, strokeLineWidth = 0f) {
                moveTo(9f, 12f)
                curveTo(9.552f, 12f, 10f, 11.328f, 10f, 10.5f)
                curveTo(10f, 9.672f, 9.552f, 9f, 9f, 9f)
                curveTo(8.448f, 9f, 8f, 9.672f, 8f, 10.5f)
                curveTo(8f, 11.328f, 8.448f, 12f, 9f, 12f)
                close()
            }
            path(fill = SolidColor(Color.White), stroke = null, strokeLineWidth = 0f) {
                moveTo(16f, 10.5f)
                curveTo(16f, 11.328f, 15.552f, 12f, 15f, 12f)
                curveTo(14.448f, 12f, 14f, 11.328f, 14f, 10.5f)
                curveTo(14f, 9.672f, 14.448f, 9f, 15f, 9f)
                curveTo(15.552f, 9f, 16f, 9.672f, 16f, 10.5f)
                close()
            }
            path(
                fill = null,
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(8.5f, 16.5f)
                quadTo(12f, 18.5f, 15.5f, 16.5f)
            }
        }.build()

    val IcSherlockSuccessFaceIcon: ImageVector
        get() = ImageVector.Builder(
            name = "IcSherlockSuccessFaceIcon",
            defaultWidth = 200.dp,
            defaultHeight = 200.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF8DD483)), stroke = null, strokeLineWidth = 0f) {
                moveTo(12f, 22f)
                curveTo(17.523f, 22f, 22f, 17.523f, 22f, 12f)
                curveTo(22f, 6.477f, 17.523f, 2f, 12f, 2f)
                curveTo(6.477f, 2f, 2f, 6.477f, 2f, 12f)
                curveTo(2f, 17.523f, 6.477f, 22f, 12f, 22f)
                close()
            }
            group(
                name = "face",
                pivotX = 0f,
                pivotY = 0f,
                scaleX = 1f,
                scaleY = 1f,
                rotate = 0f,
                translationX = 0f,
                translationY = 0f
            ) {
                path(fill = SolidColor(Color.White), stroke = null, strokeLineWidth = 0f) {
                    moveTo(9f, 12f)
                    curveTo(9.552f, 12f, 10f, 11.328f, 10f, 10.5f)
                    curveTo(10f, 9.672f, 9.552f, 9f, 9f, 9f)
                    curveTo(8.448f, 9f, 8f, 9.672f, 8f, 10.5f)
                    curveTo(8f, 11.328f, 8.448f, 12f, 9f, 12f)
                    close()
                }
                path(fill = SolidColor(Color.White), stroke = null, strokeLineWidth = 0f) {
                    moveTo(16.6f, 8.55f)
                    curveTo(16.849f, 8.881f, 16.781f, 9.352f, 16.45f, 9.6f)
                    lineTo(15.25f, 10.5f)
                    lineTo(16.45f, 11.4f)
                    curveTo(16.781f, 11.649f, 16.849f, 12.119f, 16.6f, 12.45f)
                    curveTo(16.351f, 12.781f, 15.881f, 12.849f, 15.55f, 12.6f)
                    lineTo(13.55f, 11.1f)
                    curveTo(13.361f, 10.958f, 13.25f, 10.736f, 13.25f, 10.5f)
                    curveTo(13.25f, 10.264f, 13.361f, 10.042f, 13.55f, 9.9f)
                    lineTo(15.55f, 8.4f)
                    curveTo(15.881f, 8.152f, 16.351f, 8.219f, 16.6f, 8.55f)
                    close()
                }
                path(
                    fill = null,
                    stroke = SolidColor(Color.White),
                    strokeLineWidth = 1.5f,
                    strokeLineCap = StrokeCap.Round
                ) {
                    moveTo(8.5f, 16.5f)
                    quadTo(12f, 18.5f, 15.5f, 16.5f)
                }
            }
        }.build()

    val IcSherlockWarningFaceIcon: ImageVector
        get() = ImageVector.Builder(
            name = "IcSherlockWarningFaceIcon",
            defaultWidth = 200.dp,
            defaultHeight = 200.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFFFF9B17)), stroke = null, strokeLineWidth = 0f) {
                moveTo(12f, 22f)
                curveTo(17.523f, 22f, 22f, 17.523f, 22f, 12f)
                curveTo(22f, 6.477f, 17.523f, 2f, 12f, 2f)
                curveTo(6.477f, 2f, 2f, 6.477f, 2f, 12f)
                curveTo(2f, 17.523f, 6.477f, 22f, 12f, 22f)
                close()
            }
            group(
                name = "left_eye",
                pivotX = 0f,
                pivotY = 10.5f,
                scaleX = 1f,
                scaleY = 1f,
                rotate = 0f,
                translationX = 0f,
                translationY = 0f
            ) {
                path(fill = SolidColor(Color.White), stroke = null, strokeLineWidth = 0f) {
                    moveTo(10f, 10.5f)
                    curveTo(10f, 11.328f, 9.552f, 12f, 9f, 12f)
                    curveTo(8.448f, 12f, 8f, 11.328f, 8f, 10.5f)
                    curveTo(8f, 9.672f, 8.448f, 9f, 9f, 9f)
                    curveTo(9.552f, 9f, 10f, 9.672f, 10f, 10.5f)
                    close()
                }
            }
            group(
                name = "right_eye",
                pivotX = 0f,
                pivotY = 10.5f,
                scaleX = 1f,
                scaleY = 1f,
                rotate = 0f,
                translationX = 0f,
                translationY = 0f
            ) {
                path(fill = SolidColor(Color.White), stroke = null, strokeLineWidth = 0f) {
                    moveTo(15f, 12f)
                    curveTo(15.552f, 12f, 16f, 11.328f, 16f, 10.5f)
                    curveTo(16f, 9.672f, 15.552f, 9f, 15f, 9f)
                    curveTo(14.448f, 9f, 14f, 9.672f, 14f, 10.5f)
                    curveTo(14f, 11.328f, 14.448f, 12f, 15f, 12f)
                    close()
                }
            }
            path(fill = SolidColor(Color.White), stroke = null, strokeLineWidth = 0f) {
                moveTo(8.25f, 16f)
                curveTo(8.25f, 15.586f, 8.586f, 15.25f, 9f, 15.25f)
                horizontalLineTo(15f)
                curveTo(15.414f, 15.25f, 15.75f, 15.586f, 15.75f, 16f)
                curveTo(15.75f, 16.414f, 15.414f, 16.75f, 15f, 16.75f)
                horizontalLineTo(9f)
                curveTo(8.586f, 16.75f, 8.25f, 16.414f, 8.25f, 16f)
                close()
            }
        }.build()

    val IcSmartSearchAndroidVersion: ImageVector
        get() = ImageVector.Builder(
            name = "IcSmartSearchAndroidVersion",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(40f, 721f)
                quadTo(48f, 615f, 105f, 524.5f)
                quadTo(162f, 434f, 256f, 381f)
                lineTo(181f, 252f)
                quadTo(178f, 243f, 180.5f, 234f)
                quadTo(183f, 225f, 191f, 220f)
                quadTo(200f, 215f, 210.5f, 218f)
                quadTo(221f, 221f, 226f, 230f)
                lineTo(300f, 357f)
                quadTo(386f, 320f, 480f, 320f)
                quadTo(574f, 320f, 660f, 357f)
                lineTo(735f, 230f)
                quadTo(740f, 221f, 750.5f, 218f)
                quadTo(761f, 215f, 770f, 220f)
                quadTo(778f, 225f, 781.5f, 234.5f)
                quadTo(785f, 244f, 780f, 252f)
                lineTo(704f, 381f)
                quadTo(798f, 434f, 855f, 524.5f)
                quadTo(912f, 615f, 920f, 721f)
                lineTo(40f, 721f)
                close()
                moveTo(280f, 611f)
                quadTo(300f, 611f, 315f, 596f)
                quadTo(330f, 581f, 330f, 561f)
                quadTo(330f, 541f, 315f, 526f)
                quadTo(300f, 511f, 280f, 511f)
                quadTo(260f, 511f, 245f, 526f)
                quadTo(230f, 541f, 230f, 561f)
                quadTo(230f, 581f, 245f, 596f)
                quadTo(260f, 611f, 280f, 611f)
                close()
                moveTo(680f, 611f)
                quadTo(700f, 611f, 715f, 596f)
                quadTo(730f, 581f, 730f, 561f)
                quadTo(730f, 541f, 715f, 526f)
                quadTo(700f, 511f, 680f, 511f)
                quadTo(660f, 511f, 645f, 526f)
                quadTo(630f, 541f, 630f, 561f)
                quadTo(630f, 581f, 645f, 596f)
                quadTo(660f, 611f, 680f, 611f)
                close()
            }
        }.build()

    val IcSmartSearchDevice: ImageVector
        get() = ImageVector.Builder(
            name = "IcSmartSearchDevice",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(160f, 880f)
                quadTo(127f, 880f, 103.5f, 856.5f)
                quadTo(80f, 833f, 80f, 800f)
                lineTo(80f, 360f)
                quadTo(80f, 327f, 103.5f, 303.5f)
                quadTo(127f, 280f, 160f, 280f)
                lineTo(360f, 280f)
                lineTo(360f, 160f)
                quadTo(360f, 127f, 383.5f, 103.5f)
                quadTo(407f, 80f, 440f, 80f)
                lineTo(520f, 80f)
                quadTo(553f, 80f, 576.5f, 103.5f)
                quadTo(600f, 127f, 600f, 160f)
                lineTo(600f, 280f)
                lineTo(800f, 280f)
                quadTo(833f, 280f, 856.5f, 303.5f)
                quadTo(880f, 327f, 880f, 360f)
                lineTo(880f, 800f)
                quadTo(880f, 833f, 856.5f, 856.5f)
                quadTo(833f, 880f, 800f, 880f)
                lineTo(160f, 880f)
                close()
                moveTo(160f, 800f)
                lineTo(800f, 800f)
                quadTo(800f, 800f, 800f, 800f)
                quadTo(800f, 800f, 800f, 800f)
                lineTo(800f, 360f)
                quadTo(800f, 360f, 800f, 360f)
                quadTo(800f, 360f, 800f, 360f)
                lineTo(600f, 360f)
                lineTo(600f, 360f)
                quadTo(600f, 393f, 576.5f, 416.5f)
                quadTo(553f, 440f, 520f, 440f)
                lineTo(440f, 440f)
                quadTo(407f, 440f, 383.5f, 416.5f)
                quadTo(360f, 393f, 360f, 360f)
                lineTo(360f, 360f)
                lineTo(160f, 360f)
                quadTo(160f, 360f, 160f, 360f)
                quadTo(160f, 360f, 160f, 360f)
                lineTo(160f, 800f)
                quadTo(160f, 800f, 160f, 800f)
                quadTo(160f, 800f, 160f, 800f)
                close()
                moveTo(240f, 720f)
                lineTo(480f, 720f)
                lineTo(480f, 702f)
                quadTo(480f, 685f, 470.5f, 670.5f)
                quadTo(461f, 656f, 444f, 648f)
                quadTo(424f, 639f, 403.5f, 634.5f)
                quadTo(383f, 630f, 360f, 630f)
                quadTo(337f, 630f, 316.5f, 634.5f)
                quadTo(296f, 639f, 276f, 648f)
                quadTo(259f, 656f, 249.5f, 670.5f)
                quadTo(240f, 685f, 240f, 702f)
                lineTo(240f, 720f)
                close()
                moveTo(560f, 660f)
                lineTo(720f, 660f)
                lineTo(720f, 600f)
                lineTo(560f, 600f)
                lineTo(560f, 660f)
                close()
                moveTo(360f, 600f)
                quadTo(385f, 600f, 402.5f, 582.5f)
                quadTo(420f, 565f, 420f, 540f)
                quadTo(420f, 515f, 402.5f, 497.5f)
                quadTo(385f, 480f, 360f, 480f)
                quadTo(335f, 480f, 317.5f, 497.5f)
                quadTo(300f, 515f, 300f, 540f)
                quadTo(300f, 565f, 317.5f, 582.5f)
                quadTo(335f, 600f, 360f, 600f)
                close()
                moveTo(560f, 540f)
                lineTo(720f, 540f)
                lineTo(720f, 480f)
                lineTo(560f, 480f)
                lineTo(560f, 540f)
                close()
                moveTo(440f, 360f)
                lineTo(520f, 360f)
                lineTo(520f, 160f)
                lineTo(440f, 160f)
                lineTo(440f, 360f)
                close()
            }
        }.build()

    val IcSmartSearchDiscoveredDate: ImageVector
        get() = ImageVector.Builder(
            name = "IcSmartSearchDiscoveredDate",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(180f, 880f)
                quadTo(156f, 880f, 138f, 862f)
                quadTo(120f, 844f, 120f, 820f)
                lineTo(120f, 200f)
                quadTo(120f, 176f, 138f, 158f)
                quadTo(156f, 140f, 180f, 140f)
                lineTo(245f, 140f)
                lineTo(245f, 112f)
                quadTo(245f, 98.4f, 254.2f, 89.2f)
                quadTo(263.4f, 80f, 277f, 80f)
                quadTo(291.02f, 80f, 300.51f, 89.2f)
                quadTo(310f, 98.4f, 310f, 112f)
                lineTo(310f, 140f)
                lineTo(650f, 140f)
                lineTo(650f, 112f)
                quadTo(650f, 98.4f, 659.2f, 89.2f)
                quadTo(668.4f, 80f, 682f, 80f)
                quadTo(696.03f, 80f, 705.51f, 89.2f)
                quadTo(715f, 98.4f, 715f, 112f)
                lineTo(715f, 140f)
                lineTo(780f, 140f)
                quadTo(804f, 140f, 822f, 158f)
                quadTo(840f, 176f, 840f, 200f)
                lineTo(840f, 820f)
                quadTo(840f, 844f, 822f, 862f)
                quadTo(804f, 880f, 780f, 880f)
                lineTo(180f, 880f)
                close()
                moveTo(180f, 820f)
                lineTo(780f, 820f)
                quadTo(780f, 820f, 780f, 820f)
                quadTo(780f, 820f, 780f, 820f)
                lineTo(780f, 390f)
                lineTo(180f, 390f)
                lineTo(180f, 820f)
                quadTo(180f, 820f, 180f, 820f)
                quadTo(180f, 820f, 180f, 820f)
                close()
                moveTo(180f, 330f)
                lineTo(780f, 330f)
                lineTo(780f, 200f)
                quadTo(780f, 200f, 780f, 200f)
                quadTo(780f, 200f, 780f, 200f)
                lineTo(180f, 200f)
                quadTo(180f, 200f, 180f, 200f)
                quadTo(180f, 200f, 180f, 200f)
                lineTo(180f, 330f)
                close()
                moveTo(480f, 560f)
                quadTo(463f, 560f, 451.5f, 548.5f)
                quadTo(440f, 537f, 440f, 520f)
                quadTo(440f, 503f, 451.5f, 491.5f)
                quadTo(463f, 480f, 480f, 480f)
                quadTo(497f, 480f, 508.5f, 491.5f)
                quadTo(520f, 503f, 520f, 520f)
                quadTo(520f, 537f, 508.5f, 548.5f)
                quadTo(497f, 560f, 480f, 560f)
                close()
                moveTo(320f, 560f)
                quadTo(303f, 560f, 291.5f, 548.5f)
                quadTo(280f, 537f, 280f, 520f)
                quadTo(280f, 503f, 291.5f, 491.5f)
                quadTo(303f, 480f, 320f, 480f)
                quadTo(337f, 480f, 348.5f, 491.5f)
                quadTo(360f, 503f, 360f, 520f)
                quadTo(360f, 537f, 348.5f, 548.5f)
                quadTo(337f, 560f, 320f, 560f)
                close()
                moveTo(640f, 560f)
                quadTo(623f, 560f, 611.5f, 548.5f)
                quadTo(600f, 537f, 600f, 520f)
                quadTo(600f, 503f, 611.5f, 491.5f)
                quadTo(623f, 480f, 640f, 480f)
                quadTo(657f, 480f, 668.5f, 491.5f)
                quadTo(680f, 503f, 680f, 520f)
                quadTo(680f, 537f, 668.5f, 548.5f)
                quadTo(657f, 560f, 640f, 560f)
                close()
                moveTo(480f, 720f)
                quadTo(463f, 720f, 451.5f, 708.5f)
                quadTo(440f, 697f, 440f, 680f)
                quadTo(440f, 663f, 451.5f, 651.5f)
                quadTo(463f, 640f, 480f, 640f)
                quadTo(497f, 640f, 508.5f, 651.5f)
                quadTo(520f, 663f, 520f, 680f)
                quadTo(520f, 697f, 508.5f, 708.5f)
                quadTo(497f, 720f, 480f, 720f)
                close()
                moveTo(320f, 720f)
                quadTo(303f, 720f, 291.5f, 708.5f)
                quadTo(280f, 697f, 280f, 680f)
                quadTo(280f, 663f, 291.5f, 651.5f)
                quadTo(303f, 640f, 320f, 640f)
                quadTo(337f, 640f, 348.5f, 651.5f)
                quadTo(360f, 663f, 360f, 680f)
                quadTo(360f, 697f, 348.5f, 708.5f)
                quadTo(337f, 720f, 320f, 720f)
                close()
                moveTo(640f, 720f)
                quadTo(623f, 720f, 611.5f, 708.5f)
                quadTo(600f, 697f, 600f, 680f)
                quadTo(600f, 663f, 611.5f, 651.5f)
                quadTo(623f, 640f, 640f, 640f)
                quadTo(657f, 640f, 668.5f, 651.5f)
                quadTo(680f, 663f, 680f, 680f)
                quadTo(680f, 697f, 668.5f, 708.5f)
                quadTo(657f, 720f, 640f, 720f)
                close()
            }
        }.build()

    val IcSmartSearchDiscoverer: ImageVector
        get() = ImageVector.Builder(
            name = "IcSmartSearchDiscoverer",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(702.0f, 381.0f)
                lineTo(851.0f, 232.0f)
                quadTo(859.8f, 223.0f, 871.9f, 223.0f)
                quadTo(884.0f, 223.0f, 893.0f, 232.05f)
                quadTo(902.0f, 241.11f, 902.0f, 253.55f)
                quadTo(902.0f, 266.0f, 893.0f, 275.0f)
                lineTo(723.0f, 445.0f)
                quadTo(714.0f, 454.0f, 702.0f, 454.0f)
                quadTo(690.0f, 454.0f, 681.0f, 445.0f)
                lineTo(596.0f, 360.0f)
                quadTo(587.0f, 350.93f, 587.0f, 338.47f)
                quadTo(587.0f, 326.0f, 596.0f, 317.0f)
                quadTo(605.0f, 308.0f, 617.0f, 308.0f)
                quadTo(629.0f, 308.0f, 638.0f, 317.0f)
                lineTo(702.0f, 381.0f)
                close()
                moveTo(360.0f, 479.0f)
                quadTo(294.0f, 479.0f, 252.0f, 437.0f)
                quadTo(210.0f, 395.0f, 210.0f, 329.0f)
                quadTo(210.0f, 263.0f, 252.0f, 221.0f)
                quadTo(294.0f, 179.0f, 360.0f, 179.0f)
                quadTo(426.0f, 179.0f, 468.0f, 221.0f)
                quadTo(510.0f, 263.0f, 510.0f, 329.0f)
                quadTo(510.0f, 395.0f, 468.0f, 437.0f)
                quadTo(426.0f, 479.0f, 360.0f, 479.0f)
                close()
                moveTo(40.0f, 740.0f)
                lineTo(40.0f, 706.0f)
                quadTo(40.0f, 671.0f, 57.5f, 642.5f)
                quadTo(75.0f, 614.0f, 108.0f, 600.0f)
                quadTo(183.0f, 567.0f, 241.34f, 553.5f)
                quadTo(299.68f, 540.0f, 359.84f, 540.0f)
                quadTo(420.0f, 540.0f, 478.0f, 553.5f)
                quadTo(536.0f, 567.0f, 611.0f, 600.0f)
                quadTo(644.0f, 615.0f, 662.0f, 643.0f)
                quadTo(680.0f, 671.0f, 680.0f, 706.0f)
                lineTo(680.0f, 740.0f)
                quadTo(680.0f, 764.75f, 662.38f, 782.37f)
                quadTo(644.75f, 800.0f, 620.0f, 800.0f)
                lineTo(100.0f, 800.0f)
                quadTo(75.25f, 800.0f, 57.63f, 782.37f)
                quadTo(40.0f, 764.75f, 40.0f, 740.0f)
                close()
                moveTo(100.0f, 740.0f)
                lineTo(620.0f, 740.0f)
                lineTo(620.0f, 706.0f)
                quadTo(620.0f, 690.0f, 611.0f, 675.5f)
                quadTo(602.0f, 661.0f, 587.0f, 654.0f)
                quadTo(516.0f, 621.0f, 467.0f, 610.5f)
                quadTo(418.0f, 600.0f, 360.0f, 600.0f)
                quadTo(302.0f, 600.0f, 252.5f, 610.5f)
                quadTo(203.0f, 621.0f, 132.0f, 654.0f)
                quadTo(117.0f, 661.0f, 108.5f, 675.5f)
                quadTo(100.0f, 690.0f, 100.0f, 706.0f)
                lineTo(100.0f, 740.0f)
                close()
                moveTo(360.0f, 419.0f)
                quadTo(399.0f, 419.0f, 424.5f, 393.5f)
                quadTo(450.0f, 368.0f, 450.0f, 329.0f)
                quadTo(450.0f, 290.0f, 424.5f, 264.5f)
                quadTo(399.0f, 239.0f, 360.0f, 239.0f)
                quadTo(321.0f, 239.0f, 295.5f, 264.5f)
                quadTo(270.0f, 290.0f, 270.0f, 329.0f)
                quadTo(270.0f, 368.0f, 295.5f, 393.5f)
                quadTo(321.0f, 419.0f, 360.0f, 419.0f)
                close()
                moveTo(360.0f, 670.0f)
                lineTo(360.0f, 670.0f)
                quadTo(360.0f, 670.0f, 360.0f, 670.0f)
                quadTo(360.0f, 670.0f, 360.0f, 670.0f)
                quadTo(360.0f, 670.0f, 360.0f, 670.0f)
                quadTo(360.0f, 670.0f, 360.0f, 670.0f)
                quadTo(360.0f, 670.0f, 360.0f, 670.0f)
                quadTo(360.0f, 670.0f, 360.0f, 670.0f)
                quadTo(360.0f, 670.0f, 360.0f, 670.0f)
                quadTo(360.0f, 670.0f, 360.0f, 670.0f)
                lineTo(360.0f, 670.0f)
                close()
                moveTo(360.0f, 329.0f)
                quadTo(360.0f, 329.0f, 360.0f, 329.0f)
                quadTo(360.0f, 329.0f, 360.0f, 329.0f)
                quadTo(360.0f, 329.0f, 360.0f, 329.0f)
                quadTo(360.0f, 329.0f, 360.0f, 329.0f)
                quadTo(360.0f, 329.0f, 360.0f, 329.0f)
                quadTo(360.0f, 329.0f, 360.0f, 329.0f)
                quadTo(360.0f, 329.0f, 360.0f, 329.0f)
                quadTo(360.0f, 329.0f, 360.0f, 329.0f)
                close()
            }
        }.build()

    val IcSmartSearchFirmwareType: ImageVector
        get() = ImageVector.Builder(
            name = "IcSmartSearchFirmwareType",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(289.0f, 388.0f)
                lineTo(456.0f, 121.0f)
                quadTo(461.0f, 114.0f, 467.0f, 110.5f)
                quadTo(473.0f, 107.0f, 481.0f, 107.0f)
                quadTo(489.0f, 107.0f, 495.0f, 110.5f)
                quadTo(501.0f, 114.0f, 506.0f, 121.0f)
                lineTo(673.0f, 388.0f)
                quadTo(678.0f, 396.0f, 677.5f, 404.0f)
                quadTo(677.0f, 412.0f, 673.0f, 419.0f)
                quadTo(669.0f, 426.0f, 662.4f, 430.0f)
                quadTo(655.8f, 434.0f, 647.0f, 434.0f)
                lineTo(315.0f, 434.0f)
                quadTo(306.09f, 434.0f, 299.4f, 429.87f)
                quadTo(292.71f, 425.75f, 289.0f, 419.0f)
                quadTo(285.0f, 412.0f, 284.5f, 404.0f)
                quadTo(284.0f, 396.0f, 289.0f, 388.0f)
                close()
                moveTo(706.0f, 880.0f)
                quadTo(632.0f, 880.0f, 582.0f, 830.0f)
                quadTo(532.0f, 780.0f, 532.0f, 706.0f)
                quadTo(532.0f, 632.0f, 582.0f, 582.0f)
                quadTo(632.0f, 532.0f, 706.0f, 532.0f)
                quadTo(780.0f, 532.0f, 830.0f, 582.0f)
                quadTo(880.0f, 632.0f, 880.0f, 706.0f)
                quadTo(880.0f, 780.0f, 830.0f, 830.0f)
                quadTo(780.0f, 880.0f, 706.0f, 880.0f)
                close()
                moveTo(120.0f, 825.0f)
                lineTo(120.0f, 581.0f)
                quadTo(120.0f, 568.25f, 128.63f, 559.62f)
                quadTo(137.25f, 551.0f, 150.0f, 551.0f)
                lineTo(394.0f, 551.0f)
                quadTo(406.75f, 551.0f, 415.38f, 559.62f)
                quadTo(424.0f, 568.25f, 424.0f, 581.0f)
                lineTo(424.0f, 825.0f)
                quadTo(424.0f, 837.75f, 415.38f, 846.37f)
                quadTo(406.75f, 855.0f, 394.0f, 855.0f)
                lineTo(150.0f, 855.0f)
                quadTo(137.25f, 855.0f, 128.63f, 846.37f)
                quadTo(120.0f, 837.75f, 120.0f, 825.0f)
                close()
                moveTo(706.08f, 820.0f)
                quadTo(754.0f, 820.0f, 787.0f, 786.92f)
                quadTo(820.0f, 753.83f, 820.0f, 705.92f)
                quadTo(820.0f, 658.0f, 786.92f, 625.0f)
                quadTo(753.83f, 592.0f, 705.92f, 592.0f)
                quadTo(658.0f, 592.0f, 625.0f, 625.08f)
                quadTo(592.0f, 658.17f, 592.0f, 706.08f)
                quadTo(592.0f, 754.0f, 625.08f, 787.0f)
                quadTo(658.17f, 820.0f, 706.08f, 820.0f)
                close()
                moveTo(180.0f, 795.0f)
                lineTo(364.0f, 795.0f)
                lineTo(364.0f, 611.0f)
                lineTo(180.0f, 611.0f)
                lineTo(180.0f, 795.0f)
                close()
                moveTo(369.0f, 374.0f)
                lineTo(593.0f, 374.0f)
                lineTo(481.0f, 193.0f)
                lineTo(369.0f, 374.0f)
                close()
                moveTo(481.0f, 374.0f)
                lineTo(481.0f, 374.0f)
                lineTo(481.0f, 374.0f)
                close()
                moveTo(364.0f, 611.0f)
                lineTo(364.0f, 611.0f)
                lineTo(364.0f, 611.0f)
                lineTo(364.0f, 611.0f)
                close()
                moveTo(706.0f, 706.0f)
                quadTo(706.0f, 706.0f, 706.0f, 706.0f)
                quadTo(706.0f, 706.0f, 706.0f, 706.0f)
                quadTo(706.0f, 706.0f, 706.0f, 706.0f)
                quadTo(706.0f, 706.0f, 706.0f, 706.0f)
                quadTo(706.0f, 706.0f, 706.0f, 706.0f)
                quadTo(706.0f, 706.0f, 706.0f, 706.0f)
                quadTo(706.0f, 706.0f, 706.0f, 706.0f)
                quadTo(706.0f, 706.0f, 706.0f, 706.0f)
                close()
            }
        }.build()

    val IcSmartSearchHelp: ImageVector
        get() = ImageVector.Builder(
            name = "IcSmartSearchHelp",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(478.0f, 720.0f)
                quadTo(499.0f, 720.0f, 513.5f, 705.5f)
                quadTo(528.0f, 691.0f, 528.0f, 670.0f)
                quadTo(528.0f, 649.0f, 513.5f, 634.5f)
                quadTo(499.0f, 620.0f, 478.0f, 620.0f)
                quadTo(457.0f, 620.0f, 442.5f, 634.5f)
                quadTo(428.0f, 649.0f, 428.0f, 670.0f)
                quadTo(428.0f, 691.0f, 442.5f, 705.5f)
                quadTo(457.0f, 720.0f, 478.0f, 720.0f)
                close()
                moveTo(480.0f, 880.0f)
                quadTo(397.0f, 880.0f, 324.0f, 848.5f)
                quadTo(251.0f, 817.0f, 197.0f, 763.0f)
                quadTo(143.0f, 709.0f, 111.5f, 636.0f)
                quadTo(80.0f, 563.0f, 80.0f, 480.0f)
                quadTo(80.0f, 397.0f, 111.5f, 324.0f)
                quadTo(143.0f, 251.0f, 197.0f, 197.0f)
                quadTo(251.0f, 143.0f, 324.0f, 111.5f)
                quadTo(397.0f, 80.0f, 480.0f, 80.0f)
                quadTo(563.0f, 80.0f, 636.0f, 111.5f)
                quadTo(709.0f, 143.0f, 763.0f, 197.0f)
                quadTo(817.0f, 251.0f, 848.5f, 324.0f)
                quadTo(880.0f, 397.0f, 880.0f, 480.0f)
                quadTo(880.0f, 563.0f, 848.5f, 636.0f)
                quadTo(817.0f, 709.0f, 763.0f, 763.0f)
                quadTo(709.0f, 817.0f, 636.0f, 848.5f)
                quadTo(563.0f, 880.0f, 480.0f, 880.0f)
                close()
                moveTo(480.0f, 800.0f)
                quadTo(614.0f, 800.0f, 707.0f, 707.0f)
                quadTo(800.0f, 614.0f, 800.0f, 480.0f)
                quadTo(800.0f, 346.0f, 707.0f, 253.0f)
                quadTo(614.0f, 160.0f, 480.0f, 160.0f)
                quadTo(346.0f, 160.0f, 253.0f, 253.0f)
                quadTo(160.0f, 346.0f, 160.0f, 480.0f)
                quadTo(160.0f, 614.0f, 253.0f, 707.0f)
                quadTo(346.0f, 800.0f, 480.0f, 800.0f)
                close()
                moveTo(480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                quadTo(480.0f, 480.0f, 480.0f, 480.0f)
                close()
                moveTo(484.0f, 308.0f)
                quadTo(509.0f, 308.0f, 527.5f, 324.0f)
                quadTo(546.0f, 340.0f, 546.0f, 364.0f)
                quadTo(546.0f, 386.0f, 532.5f, 403.0f)
                quadTo(519.0f, 420.0f, 502.0f, 435.0f)
                quadTo(479.0f, 455.0f, 461.5f, 479.0f)
                quadTo(444.0f, 503.0f, 444.0f, 533.0f)
                quadTo(444.0f, 547.0f, 454.5f, 556.5f)
                quadTo(465.0f, 566.0f, 479.0f, 566.0f)
                quadTo(494.0f, 566.0f, 504.5f, 556.0f)
                quadTo(515.0f, 546.0f, 518.0f, 531.0f)
                quadTo(522.0f, 510.0f, 536.0f, 493.5f)
                quadTo(550.0f, 477.0f, 566.0f, 462.0f)
                quadTo(589.0f, 440.0f, 605.5f, 414.0f)
                quadTo(622.0f, 388.0f, 622.0f, 356.0f)
                quadTo(622.0f, 305.0f, 580.5f, 272.5f)
                quadTo(539.0f, 240.0f, 484.0f, 240.0f)
                quadTo(446.0f, 240.0f, 411.5f, 256.0f)
                quadTo(377.0f, 272.0f, 359.0f, 305.0f)
                quadTo(352.0f, 317.0f, 354.5f, 330.5f)
                quadTo(357.0f, 344.0f, 368.0f, 351.0f)
                quadTo(382.0f, 359.0f, 397.0f, 356.0f)
                quadTo(412.0f, 353.0f, 422.0f, 339.0f)
                quadTo(433.0f, 324.0f, 449.5f, 316.0f)
                quadTo(466.0f, 308.0f, 484.0f, 308.0f)
                close()
            }
        }.build()

    val IcTips: ImageVector
        get() = ImageVector.Builder(
            name = "IcTips",
            defaultWidth = 28.dp,
            defaultHeight = 28.dp,
            viewportWidth = 28f,
            viewportHeight = 28f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFAC51)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(16.3669f, 19.5056f)
                lineTo(16.3669f, 20.093f)
                curveTo(16.3669f, 21.0203f, 15.7581f, 21.0575f, 15.3753f, 21.1779f)
                curveTo(15.169f, 21.7381f, 14.6328f, 22.1374f, 14.0008f, 22.1374f)
                curveTo(13.3689f, 22.1374f, 12.8326f, 21.7373f, 12.6264f, 21.1779f)
                curveTo(12.2428f, 21.0575f, 11.6339f, 21.0212f, 11.6339f, 20.093f)
                lineTo(11.6339f, 20.093f)
                lineTo(11.6347f, 20.093f)
                lineTo(11.6347f, 19.5056f)
                lineTo(16.3669f, 19.5056f)
                close()
                moveTo(14.0f, 5.8626f)
                curveTo(17.1136f, 5.8626f, 19.6389f, 8.3879f, 19.6389f, 11.5015f)
                curveTo(19.6389f, 13.7694f, 18.2983f, 15.7238f, 16.3661f, 16.6198f)
                lineTo(16.3661f, 16.6198f)
                lineTo(16.3661f, 18.1205f)
                lineTo(11.6339f, 18.1205f)
                lineTo(11.6339f, 16.6198f)
                curveTo(9.7026f, 15.7247f, 8.3611f, 13.7702f, 8.3611f, 11.5015f)
                curveTo(8.3611f, 8.3879f, 10.8864f, 5.8626f, 14.0f, 5.8626f)
                close()
            }
        }.build()

    val IcWeb: ImageVector
        get() = ImageVector.Builder(
            name = "IcWeb",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(200.0f, 840.0f)
                quadTo(167.0f, 840.0f, 143.5f, 816.5f)
                quadTo(120.0f, 793.0f, 120.0f, 760.0f)
                lineTo(120.0f, 200.0f)
                quadTo(120.0f, 167.0f, 143.5f, 143.5f)
                quadTo(167.0f, 120.0f, 200.0f, 120.0f)
                lineTo(440.0f, 120.0f)
                quadTo(457.0f, 120.0f, 468.5f, 131.5f)
                quadTo(480.0f, 143.0f, 480.0f, 160.0f)
                quadTo(480.0f, 177.0f, 468.5f, 188.5f)
                quadTo(457.0f, 200.0f, 440.0f, 200.0f)
                lineTo(200.0f, 200.0f)
                quadTo(200.0f, 200.0f, 200.0f, 200.0f)
                quadTo(200.0f, 200.0f, 200.0f, 200.0f)
                lineTo(200.0f, 760.0f)
                quadTo(200.0f, 760.0f, 200.0f, 760.0f)
                quadTo(200.0f, 760.0f, 200.0f, 760.0f)
                lineTo(760.0f, 760.0f)
                quadTo(760.0f, 760.0f, 760.0f, 760.0f)
                quadTo(760.0f, 760.0f, 760.0f, 760.0f)
                lineTo(760.0f, 520.0f)
                quadTo(760.0f, 503.0f, 771.5f, 491.5f)
                quadTo(783.0f, 480.0f, 800.0f, 480.0f)
                quadTo(817.0f, 480.0f, 828.5f, 491.5f)
                quadTo(840.0f, 503.0f, 840.0f, 520.0f)
                lineTo(840.0f, 760.0f)
                quadTo(840.0f, 793.0f, 816.5f, 816.5f)
                quadTo(793.0f, 840.0f, 760.0f, 840.0f)
                lineTo(200.0f, 840.0f)
                close()
                moveTo(760.0f, 256.0f)
                lineTo(416.0f, 600.0f)
                quadTo(405.0f, 611.0f, 388.0f, 611.0f)
                quadTo(371.0f, 611.0f, 360.0f, 600.0f)
                quadTo(349.0f, 589.0f, 349.0f, 572.0f)
                quadTo(349.0f, 555.0f, 360.0f, 544.0f)
                lineTo(704.0f, 200.0f)
                lineTo(600.0f, 200.0f)
                quadTo(583.0f, 200.0f, 571.5f, 188.5f)
                quadTo(560.0f, 177.0f, 560.0f, 160.0f)
                quadTo(560.0f, 143.0f, 571.5f, 131.5f)
                quadTo(583.0f, 120.0f, 600.0f, 120.0f)
                lineTo(800.0f, 120.0f)
                quadTo(817.0f, 120.0f, 828.5f, 131.5f)
                quadTo(840.0f, 143.0f, 840.0f, 160.0f)
                lineTo(840.0f, 360.0f)
                quadTo(840.0f, 377.0f, 828.5f, 388.5f)
                quadTo(817.0f, 400.0f, 800.0f, 400.0f)
                quadTo(783.0f, 400.0f, 771.5f, 388.5f)
                quadTo(760.0f, 377.0f, 760.0f, 360.0f)
                lineTo(760.0f, 256.0f)
                close()
            }
        }.build()

    val IcWelcomeSearch: ImageVector
        get() = ImageVector.Builder(
            name = "IcWelcomeSearch",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(634.0f, 180.0f)
                lineTo(634.0f, 512.0f)
                lineTo(711.0f, 580.0f)
                quadTo(716.0f, 585.0f, 718.0f, 590.6f)
                quadTo(720.0f, 596.19f, 720.0f, 602.16f)
                lineTo(720.0f, 619.32f)
                quadTo(720.0f, 632.0f, 711.38f, 640.5f)
                quadTo(702.75f, 649.0f, 690.0f, 649.0f)
                lineTo(510.0f, 649.0f)
                lineTo(510.0f, 885.0f)
                quadTo(510.0f, 897.75f, 501.32f, 906.37f)
                quadTo(492.65f, 915.0f, 479.82f, 915.0f)
                quadTo(467.0f, 915.0f, 458.5f, 906.37f)
                quadTo(450.0f, 897.75f, 450.0f, 885.0f)
                lineTo(450.0f, 649.0f)
                lineTo(270.0f, 649.0f)
                quadTo(257.25f, 649.0f, 248.63f, 640.42f)
                quadTo(240.0f, 631.83f, 240.0f, 619.14f)
                lineTo(240.0f, 601.97f)
                quadTo(240.0f, 596.0f, 242.0f, 590.5f)
                quadTo(244.0f, 585.0f, 249.0f, 580.0f)
                lineTo(320.0f, 512.0f)
                lineTo(320.0f, 180.0f)
                lineTo(300.0f, 180.0f)
                quadTo(287.25f, 180.0f, 278.63f, 171.32f)
                quadTo(270.0f, 162.65f, 270.0f, 149.82f)
                quadTo(270.0f, 137.0f, 278.63f, 128.5f)
                quadTo(287.25f, 120.0f, 300.0f, 120.0f)
                lineTo(654.0f, 120.0f)
                quadTo(666.75f, 120.0f, 675.38f, 128.68f)
                quadTo(684.0f, 137.35f, 684.0f, 150.18f)
                quadTo(684.0f, 163.0f, 675.38f, 171.5f)
                quadTo(666.75f, 180.0f, 654.0f, 180.0f)
                lineTo(634.0f, 180.0f)
                close()
                moveTo(321.0f, 589.0f)
                lineTo(633.0f, 589.0f)
                lineTo(574.0f, 534.0f)
                lineTo(574.0f, 180.0f)
                lineTo(380.0f, 180.0f)
                lineTo(380.0f, 534.0f)
                lineTo(321.0f, 589.0f)
                close()
                moveTo(477.0f, 589.0f)
                lineTo(477.0f, 589.0f)
                lineTo(477.0f, 589.0f)
                lineTo(477.0f, 589.0f)
                lineTo(477.0f, 589.0f)
                lineTo(477.0f, 589.0f)
                close()
            }
        }.build()

    val OneuiAppInfoIcon: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiAppInfoIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 72.0f,
            viewportHeight = 72.0f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(36.0f, 8.25f)
                curveToRelative(15.327f, 0.0f, 27.75f, 12.425f, 27.75f, 27.753f)
                curveTo(63.75f, 51.328f, 51.327f, 63.75f, 36.0f, 63.75f)
                curveToRelative(-15.325f, 0.0f, -27.75f, -12.423f, -27.75f, -27.747f)
                curveTo(8.25f, 20.676f, 20.675f, 8.25f, 36.0f, 8.25f)
                close()
                moveTo(36.0f, 12.75f)
                curveToRelative(-12.84f, 0.0f, -23.25f, 10.411f, -23.25f, 23.253f)
                curveTo(12.75f, 48.842f, 23.16f, 59.25f, 36.0f, 59.25f)
                curveToRelative(12.842f, 0.0f, 23.25f, -10.407f, 23.25f, -23.247f)
                curveTo(59.25f, 23.16f, 48.842f, 12.75f, 36.0f, 12.75f)
                close()
                moveTo(39.0f, 30.41f)
                verticalLineToRelative(20.616f)
                curveToRelative(0.0f, 1.243f, -1.008f, 2.25f, -2.25f, 2.25f)
                curveToRelative(-1.139f, 0.0f, -2.081f, -0.847f, -2.229f, -1.944f)
                lineTo(34.5f, 51.026f)
                verticalLineTo(34.908f)
                lineToRelative(-3.504f, 0.002f)
                curveToRelative(-1.139f, 0.0f, -2.081f, -0.846f, -2.229f, -1.944f)
                lineToRelative(-0.021f, -0.306f)
                curveToRelative(0.0f, -1.139f, 0.846f, -2.08f, 1.945f, -2.229f)
                lineToRelative(0.305f, -0.021f)
                horizontalLineTo(39.0f)
                close()
                moveTo(36.166f, 20.896f)
                curveToRelative(1.657f, 0.0f, 3.0f, 1.344f, 3.0f, 3.0f)
                curveToRelative(0.0f, 1.656f, -1.344f, 3.0f, -3.0f, 3.0f)
                curveToRelative(-1.656f, 0.0f, -3.0f, -1.344f, -3.0f, -3.0f)
                curveTo(33.166f, 22.24f, 34.51f, 20.896f, 36.166f, 20.896f)
                close()
            }
        }.build()

    val OneuiAdd: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiAdd",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(20.375f, 11.25f)
                lineTo(12.75f, 11.25f)
                lineTo(12.75f, 3.625f)
                curveTo(12.75f, 3.211f, 12.414f, 2.875f, 12.0f, 2.875f)
                curveTo(11.586f, 2.875f, 11.25f, 3.211f, 11.25f, 3.625f)
                lineTo(11.25f, 11.25f)
                lineTo(3.625f, 11.25f)
                curveTo(3.211f, 11.25f, 2.875f, 11.586f, 2.875f, 12.0f)
                curveTo(2.875f, 12.414f, 3.211f, 12.75f, 3.625f, 12.75f)
                lineTo(11.25f, 12.75f)
                lineTo(11.25f, 20.375f)
                curveTo(11.25f, 20.789f, 11.586f, 21.125f, 12.0f, 21.125f)
                curveTo(12.414f, 21.125f, 12.75f, 20.789f, 12.75f, 20.375f)
                lineTo(12.75f, 12.75f)
                lineTo(20.375f, 12.75f)
                curveTo(20.789f, 12.75f, 21.125f, 12.414f, 21.125f, 12.0f)
                curveTo(21.125f, 11.586f, 20.789f, 11.25f, 20.375f, 11.25f)
            }
        }.build()

    val OneuiBookmark: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiBookmark",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0f
            ) {
                moveTo(18.2589f, 5.0872f)
                lineTo(18.2589f, 18.7852f)
                curveTo(18.2589f, 18.8912f, 18.1979f, 18.9452f, 18.1479f, 18.9732f)
                curveTo(18.0959f, 19.0002f, 18.0169f, 19.0212f, 17.9139f, 18.9542f)
                lineTo(13.0529f, 15.9422f)
                curveTo(12.6439f, 15.6762f, 12.1149f, 15.6762f, 11.7209f, 15.9332f)
                lineTo(6.8289f, 18.9642f)
                curveTo(6.7409f, 19.0212f, 6.6619f, 19.0002f, 6.6119f, 18.9732f)
                curveTo(6.5609f, 18.9452f, 6.4999f, 18.8902f, 6.4999f, 18.7852f)
                lineTo(6.4999f, 5.0872f)
                curveTo(6.4999f, 4.2122f, 7.2619f, 3.5002f, 8.1979f, 3.5002f)
                lineTo(16.5609f, 3.5002f)
                curveTo(17.4969f, 3.5002f, 18.2589f, 4.2122f, 18.2589f, 5.0872f)
                moveTo(4.9999f, 5.0872f)
                lineTo(4.9999f, 18.7852f)
                curveTo(4.9999f, 19.4152f, 5.3439f, 19.9922f, 5.8979f, 20.2922f)
                curveTo(6.1549f, 20.4312f, 6.4379f, 20.5002f, 6.7179f, 20.5002f)
                curveTo(7.0399f, 20.5002f, 7.3609f, 20.4082f, 7.6349f, 20.2302f)
                lineTo(12.3789f, 17.2892f)
                lineTo(17.1099f, 20.2202f)
                curveTo(17.6369f, 20.5652f, 18.3079f, 20.5922f, 18.8619f, 20.2922f)
                curveTo(19.4149f, 19.9922f, 19.7589f, 19.4152f, 19.7589f, 18.7852f)
                lineTo(19.7589f, 5.0872f)
                curveTo(19.7589f, 3.3852f, 18.3239f, 2.0002f, 16.5609f, 2.0002f)
                lineTo(8.1979f, 2.0002f)
                curveTo(6.4349f, 2.0002f, 4.9999f, 3.3852f, 4.9999f, 5.0872f)
            }
        }.build()

    val OneuiBudsFilled: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiBudsFilled",
            defaultWidth = 19.dp,
            defaultHeight = 19.dp,
            viewportWidth = 19.0f,
            viewportHeight = 19.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(6.681f, 11.017f)
                lineTo(5.329f, 11.614f)
                curveTo(5.25f, 11.649f, 5.168f, 11.666f, 5.087f, 11.666f)
                curveTo(4.856f, 11.666f, 4.636f, 11.532f, 4.536f, 11.306f)
                curveTo(4.402f, 11.003f, 4.54f, 10.648f, 4.843f, 10.514f)
                lineTo(6.195f, 9.918f)
                curveTo(6.5f, 9.781f, 6.853f, 9.921f, 6.987f, 10.224f)
                curveTo(7.121f, 10.527f, 6.984f, 10.883f, 6.681f, 11.017f)
                moveTo(2.908f, 13.035f)
                curveTo(1.857f, 13.035f, 1.001f, 12.134f, 1.001f, 11.029f)
                curveTo(1.001f, 10.4f, 1.284f, 9.844f, 1.715f, 9.476f)
                curveTo(1.705f, 10.868f, 2.278f, 12.241f, 3.646f, 12.877f)
                curveTo(3.419f, 12.978f, 3.17f, 13.035f, 2.908f, 13.035f)
                moveTo(6.335f, 5.097f)
                curveTo(4.333f, 4.514f, 2.479f, 5.898f, 1.875f, 8.226f)
                curveTo(0.782f, 8.658f, -0.0f, 9.747f, -0.0f, 11.029f)
                curveTo(-0.0f, 12.687f, 1.304f, 14.036f, 2.908f, 14.036f)
                curveTo(3.681f, 14.036f, 4.381f, 13.718f, 4.902f, 13.206f)
                curveTo(6.672f, 13.334f, 8.391f, 12.15f, 8.914f, 10.156f)
                curveTo(9.518f, 7.854f, 8.296f, 5.667f, 6.335f, 5.097f)
                moveTo(16.092f, 13.034f)
                curveTo(15.83f, 13.034f, 15.581f, 12.978f, 15.354f, 12.877f)
                curveTo(16.722f, 12.241f, 17.295f, 10.867f, 17.284f, 9.476f)
                curveTo(17.716f, 9.844f, 17.998f, 10.4f, 17.998f, 11.028f)
                curveTo(17.998f, 12.134f, 17.143f, 13.034f, 16.092f, 13.034f)
                moveTo(14.463f, 11.306f)
                curveTo(14.364f, 11.531f, 14.144f, 11.665f, 13.913f, 11.665f)
                curveTo(13.833f, 11.665f, 13.749f, 11.649f, 13.671f, 11.614f)
                lineTo(12.319f, 11.017f)
                curveTo(12.016f, 10.883f, 11.878f, 10.528f, 12.012f, 10.224f)
                curveTo(12.146f, 9.921f, 12.5f, 9.782f, 12.805f, 9.917f)
                lineTo(14.157f, 10.515f)
                curveTo(14.46f, 10.648f, 14.597f, 11.003f, 14.463f, 11.306f)
                moveTo(17.124f, 8.225f)
                curveTo(16.521f, 5.898f, 14.668f, 4.514f, 12.665f, 5.097f)
                curveTo(10.704f, 5.667f, 9.483f, 7.854f, 10.086f, 10.156f)
                curveTo(10.609f, 12.149f, 12.327f, 13.334f, 14.098f, 13.206f)
                curveTo(14.619f, 13.717f, 15.318f, 14.036f, 16.092f, 14.036f)
                curveTo(17.695f, 14.036f, 19.0f, 12.687f, 19.0f, 11.028f)
                curveTo(19.0f, 9.747f, 18.218f, 8.658f, 17.124f, 8.225f)
            }
        }.build()

    val OneuiBudsTone: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiBudsTone",
            defaultWidth = 28.dp,
            defaultHeight = 24.dp,
            viewportWidth = 28.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFCCCCCC)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(7.402f, 5.511f)
                lineTo(7.46f, 5.516f)
                curveTo(8.509f, 5.625f, 9.299f, 6.062f, 9.995f, 6.687f)
                curveTo(10.95f, 7.544f, 11.6f, 8.745f, 11.777f, 9.973f)
                curveTo(12.061f, 11.95f, 11.208f, 14.098f, 9.559f, 15.106f)
                lineTo(9.378f, 15.211f)
                curveTo(9.074f, 15.378f, 8.623f, 15.56f, 8.062f, 15.675f)
                curveTo(7.191f, 15.855f, 6.335f, 15.796f, 5.537f, 15.423f)
                curveTo(4.508f, 14.941f, 3.96f, 14.223f, 3.427f, 13.083f)
                curveTo(2.361f, 10.802f, 2.577f, 7.881f, 4.244f, 6.473f)
                curveTo(4.79f, 6.012f, 5.44f, 5.729f, 6.149f, 5.592f)
                curveTo(6.629f, 5.499f, 7.064f, 5.485f, 7.402f, 5.511f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF3B3B3F)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(3.447f, 5.529f)
                curveTo(4.167f, 4.921f, 5.01f, 4.554f, 5.914f, 4.379f)
                curveTo(6.514f, 4.263f, 7.057f, 4.245f, 7.498f, 4.28f)
                lineTo(7.578f, 4.287f)
                curveTo(8.941f, 4.428f, 9.948f, 4.985f, 10.82f, 5.768f)
                curveTo(11.989f, 6.816f, 12.782f, 8.28f, 13.0f, 9.797f)
                curveTo(13.35f, 12.236f, 12.301f, 14.877f, 10.191f, 16.167f)
                lineTo(9.985f, 16.287f)
                curveTo(9.872f, 16.348f, 9.749f, 16.41f, 9.615f, 16.47f)
                curveTo(9.242f, 17.391f, 8.576f, 18.167f, 7.696f, 18.675f)
                curveTo(5.541f, 19.919f, 2.783f, 19.177f, 1.537f, 17.017f)
                curveTo(0.636f, 15.457f, 0.757f, 13.54f, 1.782f, 12.118f)
                curveTo(1.177f, 9.696f, 1.664f, 7.035f, 3.447f, 5.529f)
                close()
                moveTo(2.607f, 16.4f)
                curveTo(3.512f, 17.969f, 5.515f, 18.508f, 7.079f, 17.605f)
                curveTo(7.382f, 17.43f, 7.649f, 17.212f, 7.876f, 16.959f)
                curveTo(6.905f, 17.088f, 5.936f, 16.973f, 5.013f, 16.541f)
                curveTo(3.655f, 15.905f, 2.954f, 14.986f, 2.308f, 13.606f)
                lineTo(2.346f, 13.687f)
                curveTo(2.043f, 14.564f, 2.118f, 15.554f, 2.607f, 16.4f)
                close()
                moveTo(6.149f, 5.592f)
                curveTo(5.44f, 5.729f, 4.79f, 6.012f, 4.244f, 6.473f)
                curveTo(2.831f, 7.666f, 2.46f, 9.945f, 3.029f, 12.001f)
                curveTo(3.051f, 12.048f, 3.065f, 12.097f, 3.073f, 12.147f)
                curveTo(3.167f, 12.467f, 3.286f, 12.781f, 3.427f, 13.083f)
                curveTo(3.96f, 14.223f, 4.508f, 14.941f, 5.537f, 15.423f)
                curveTo(6.335f, 15.796f, 7.191f, 15.855f, 8.062f, 15.675f)
                curveTo(8.623f, 15.56f, 9.074f, 15.378f, 9.378f, 15.211f)
                lineTo(9.559f, 15.106f)
                curveTo(11.208f, 14.098f, 12.061f, 11.95f, 11.777f, 9.973f)
                curveTo(11.6f, 8.745f, 10.95f, 7.544f, 9.995f, 6.687f)
                curveTo(9.299f, 6.062f, 8.509f, 5.625f, 7.46f, 5.516f)
                lineTo(7.402f, 5.511f)
                curveTo(7.064f, 5.485f, 6.629f, 5.499f, 6.149f, 5.592f)
                close()
                moveTo(9.86f, 11.987f)
                curveTo(10.098f, 12.231f, 10.094f, 12.622f, 9.85f, 12.86f)
                lineTo(8.683f, 14.0f)
                curveTo(8.439f, 14.238f, 8.048f, 14.234f, 7.809f, 13.99f)
                curveTo(7.571f, 13.746f, 7.576f, 13.355f, 7.82f, 13.116f)
                lineTo(8.986f, 11.977f)
                curveTo(9.231f, 11.738f, 9.622f, 11.743f, 9.86f, 11.987f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFCCCCCC)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(20.617f, 5.511f)
                lineTo(20.559f, 5.516f)
                curveTo(19.51f, 5.625f, 18.72f, 6.062f, 18.024f, 6.687f)
                curveTo(17.069f, 7.544f, 16.418f, 8.745f, 16.242f, 9.973f)
                curveTo(15.958f, 11.95f, 16.811f, 14.098f, 18.46f, 15.106f)
                lineTo(18.641f, 15.211f)
                curveTo(18.945f, 15.378f, 19.396f, 15.56f, 19.957f, 15.675f)
                curveTo(20.828f, 15.855f, 21.684f, 15.796f, 22.482f, 15.423f)
                curveTo(23.511f, 14.941f, 24.058f, 14.223f, 24.591f, 13.083f)
                curveTo(25.658f, 10.802f, 25.442f, 7.881f, 23.775f, 6.473f)
                curveTo(23.229f, 6.012f, 22.579f, 5.729f, 21.87f, 5.592f)
                curveTo(21.39f, 5.499f, 20.955f, 5.485f, 20.617f, 5.511f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF3B3B3F)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(24.572f, 5.529f)
                curveTo(23.852f, 4.921f, 23.009f, 4.554f, 22.104f, 4.379f)
                curveTo(21.505f, 4.263f, 20.962f, 4.245f, 20.521f, 4.28f)
                lineTo(20.441f, 4.287f)
                curveTo(19.078f, 4.428f, 18.071f, 4.985f, 17.198f, 5.768f)
                curveTo(16.03f, 6.816f, 15.237f, 8.28f, 15.019f, 9.797f)
                curveTo(14.669f, 12.236f, 15.718f, 14.877f, 17.827f, 16.167f)
                lineTo(18.034f, 16.287f)
                curveTo(18.147f, 16.348f, 18.27f, 16.41f, 18.404f, 16.47f)
                curveTo(18.777f, 17.391f, 19.443f, 18.167f, 20.322f, 18.675f)
                curveTo(22.478f, 19.919f, 25.235f, 19.177f, 26.482f, 17.017f)
                curveTo(27.383f, 15.457f, 27.262f, 13.54f, 26.237f, 12.118f)
                curveTo(26.842f, 9.696f, 26.354f, 7.035f, 24.572f, 5.529f)
                close()
                moveTo(25.412f, 16.4f)
                curveTo(24.506f, 17.969f, 22.504f, 18.508f, 20.94f, 17.605f)
                curveTo(20.637f, 17.43f, 20.37f, 17.212f, 20.143f, 16.959f)
                curveTo(21.114f, 17.088f, 22.083f, 16.973f, 23.006f, 16.541f)
                curveTo(24.364f, 15.905f, 25.065f, 14.986f, 25.71f, 13.606f)
                lineTo(25.673f, 13.687f)
                curveTo(25.976f, 14.564f, 25.9f, 15.554f, 25.412f, 16.4f)
                close()
                moveTo(21.87f, 5.592f)
                curveTo(22.579f, 5.729f, 23.229f, 6.012f, 23.775f, 6.473f)
                curveTo(25.188f, 7.666f, 25.558f, 9.945f, 24.99f, 12.001f)
                curveTo(24.968f, 12.048f, 24.954f, 12.097f, 24.945f, 12.147f)
                curveTo(24.852f, 12.467f, 24.733f, 12.781f, 24.591f, 13.083f)
                curveTo(24.058f, 14.223f, 23.511f, 14.941f, 22.482f, 15.423f)
                curveTo(21.684f, 15.796f, 20.828f, 15.855f, 19.957f, 15.675f)
                curveTo(19.396f, 15.56f, 18.945f, 15.378f, 18.641f, 15.211f)
                lineTo(18.46f, 15.106f)
                curveTo(16.811f, 14.098f, 15.958f, 11.95f, 16.242f, 9.973f)
                curveTo(16.418f, 8.745f, 17.069f, 7.544f, 18.024f, 6.687f)
                curveTo(18.72f, 6.062f, 19.51f, 5.625f, 20.559f, 5.516f)
                lineTo(20.617f, 5.511f)
                curveTo(20.955f, 5.485f, 21.39f, 5.499f, 21.87f, 5.592f)
                close()
                moveTo(18.159f, 11.987f)
                curveTo(17.921f, 12.231f, 17.925f, 12.622f, 18.169f, 12.86f)
                lineTo(19.336f, 14.0f)
                curveTo(19.58f, 14.238f, 19.971f, 14.234f, 20.21f, 13.99f)
                curveTo(20.448f, 13.746f, 20.443f, 13.355f, 20.199f, 13.116f)
                lineTo(19.032f, 11.977f)
                curveTo(18.788f, 11.738f, 18.397f, 11.743f, 18.159f, 11.987f)
                close()
            }
        }.build()

    val OneuiDelete: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiDelete",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color.Transparent),
                strokeLineWidth = 1.5f
            ) {
                moveTo(18.2369f, 6.8956f)
                lineTo(17.2849f, 19.1546f)
                curveTo(17.2049f, 20.1966f, 16.3359f, 20.9996f, 15.2919f, 20.9996f)
                lineTo(8.7089f, 20.9996f)
                curveTo(7.6639f, 20.9996f, 6.7959f, 20.1966f, 6.7149f, 19.1546f)
                lineTo(5.7639f, 6.8956f)
                moveTo(4.314f, 6.5001f)
                lineTo(19.686f, 6.5001f)
                moveTo(13.8702f, 11.6642f)
                lineTo(13.8702f, 16.1142f)
                moveTo(10.1304f, 11.6642f)
                lineTo(10.1304f, 16.1142f)
                moveTo(9.2496f, 6.0001f)
                lineTo(9.2496f, 3.7501f)
                curveTo(9.2496f, 3.4751f, 9.4746f, 3.2501f, 9.7496f, 3.2501f)
                lineTo(14.2496f, 3.2501f)
                curveTo(14.5246f, 3.2501f, 14.7496f, 3.4751f, 14.7496f, 3.7501f)
                lineTo(14.7496f, 6.0001f)
            }
        }.build()

    val OneuiDevice: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiDevice",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color.Transparent),
                strokeLineWidth = 1.5f
            ) {
                moveTo(15.069f, 20.25f)
                lineTo(8.931f, 20.25f)
                curveTo(7.45f, 20.25f, 6.25f, 19.05f, 6.25f, 17.569f)
                lineTo(6.25f, 6.431f)
                curveTo(6.25f, 4.95f, 7.45f, 3.75f, 8.931f, 3.75f)
                lineTo(15.069f, 3.75f)
                curveTo(16.549f, 3.75f, 17.75f, 4.95f, 17.75f, 6.431f)
                lineTo(17.75f, 17.569f)
                curveTo(17.75f, 19.05f, 16.549f, 20.25f, 15.069f, 20.25f)
                close()
                moveTo(13.5973f, 16.8281f)
                lineTo(10.4023f, 16.8281f)
                lineTo(13.5973f, 16.8281f)
                close()
            }
        }.build()

    val OneuiEdit: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiEdit",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color.Transparent),
                strokeLineWidth = 1.5f
            ) {
                moveTo(11.0217f, 18.4762f)
                curveTo(10.8297f, 18.6682f, 10.0967f, 19.2232f, 9.8247f, 19.2772f)
                lineTo(4.6567f, 20.2902f)
                curveTo(4.3847f, 20.3442f, 4.2067f, 20.1652f, 4.2597f, 19.8932f)
                lineTo(5.2737f, 14.7282f)
                curveTo(5.3277f, 14.4562f, 5.8817f, 13.7222f, 6.0737f, 13.5302f)
                moveTo(11.2102f, 18.2926f)
                curveTo(10.9182f, 18.5846f, 10.4412f, 18.5846f, 10.1492f, 18.2926f)
                lineTo(6.2602f, 14.4036f)
                curveTo(5.9692f, 14.1116f, 5.9692f, 13.6346f, 6.2602f, 13.3426f)
                lineTo(14.7672f, 4.8356f)
                curveTo(15.5482f, 4.0546f, 16.8142f, 4.0546f, 17.5962f, 4.8356f)
                lineTo(19.7172f, 6.9576f)
                curveTo(20.4982f, 7.7376f, 20.4982f, 9.0036f, 19.7172f, 9.7856f)
                lineTo(11.2102f, 18.2926f)
                close()
            }
        }.build()

    val OneuiFitFilled: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiFitFilled",
            defaultWidth = 19.dp,
            defaultHeight = 19.dp,
            viewportWidth = 19.0f,
            viewportHeight = 19.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(12.356f, 13.225f)
                curveTo(12.356f, 13.503f, 12.179f, 13.738f, 11.936f, 13.82f)
                lineTo(11.912f, 13.82f)
                curveTo(11.873f, 13.826f, 11.835f, 13.835f, 11.8f, 13.849f)
                curveTo(11.782f, 13.85f, 11.765f, 13.851f, 11.747f, 13.851f)
                lineTo(7.254f, 13.851f)
                lineTo(7.201f, 13.845f)
                curveTo(7.137f, 13.826f, 7.067f, 13.814f, 6.996f, 13.814f)
                lineTo(7.064f, 13.82f)
                curveTo(6.82f, 13.738f, 6.645f, 13.503f, 6.645f, 13.225f)
                lineTo(6.645f, 5.828f)
                curveTo(6.645f, 5.581f, 6.784f, 5.368f, 6.987f, 5.267f)
                curveTo(7.054f, 5.257f, 7.118f, 5.238f, 7.176f, 5.208f)
                curveTo(7.201f, 5.204f, 7.227f, 5.202f, 7.254f, 5.202f)
                lineTo(11.747f, 5.202f)
                lineTo(11.823f, 5.21f)
                curveTo(11.882f, 5.238f, 11.947f, 5.257f, 12.014f, 5.266f)
                curveTo(12.216f, 5.368f, 12.356f, 5.581f, 12.356f, 5.828f)
                lineTo(12.356f, 13.225f)
                close()
                moveTo(12.793f, 4.289f)
                lineTo(12.793f, 2.04f)
                curveTo(12.793f, 1.474f, 12.319f, 1.039f, 11.762f, 1.039f)
                lineTo(7.238f, 1.039f)
                curveTo(6.681f, 1.039f, 6.208f, 1.474f, 6.208f, 2.04f)
                lineTo(6.207f, 4.289f)
                curveTo(5.735f, 4.63f, 5.426f, 5.192f, 5.426f, 5.828f)
                lineTo(5.426f, 13.225f)
                curveTo(5.426f, 13.909f, 5.783f, 14.508f, 6.316f, 14.836f)
                lineTo(6.317f, 16.96f)
                curveTo(6.317f, 17.519f, 6.778f, 17.961f, 7.332f, 17.961f)
                lineTo(11.668f, 17.961f)
                curveTo(12.223f, 17.961f, 12.683f, 17.519f, 12.683f, 16.96f)
                lineTo(12.684f, 14.836f)
                curveTo(13.217f, 14.508f, 13.573f, 13.909f, 13.573f, 13.225f)
                lineTo(13.573f, 5.828f)
                curveTo(13.573f, 5.192f, 13.265f, 4.63f, 12.793f, 4.289f)
                lineTo(12.793f, 4.289f)
                close()
            }
        }.build()

    val OneuiFitTone: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiFitTone",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFCCCCCC)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(15.0f, 3.0f)
                lineTo(15.001f, 7.102f)
                curveTo(15.097f, 7.222f, 15.154f, 7.375f, 15.154f, 7.542f)
                lineTo(15.154f, 16.517f)
                curveTo(15.154f, 16.684f, 15.097f, 16.837f, 15.001f, 16.957f)
                lineTo(15.0f, 21.0f)
                lineTo(9.0f, 21.0f)
                lineTo(8.999f, 16.957f)
                curveTo(8.903f, 16.837f, 8.846f, 16.684f, 8.846f, 16.517f)
                lineTo(8.846f, 7.542f)
                curveTo(8.846f, 7.375f, 8.903f, 7.222f, 8.999f, 7.102f)
                lineTo(9.0f, 3.0f)
                lineTo(15.0f, 3.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF3B3B3F)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(14.499f, 2.25f)
                curveTo(15.114f, 2.25f, 15.637f, 2.731f, 15.637f, 3.357f)
                lineTo(15.637f, 5.841f)
                curveTo(16.159f, 6.217f, 16.5f, 6.839f, 16.5f, 7.542f)
                lineTo(16.5f, 16.517f)
                curveTo(16.5f, 17.273f, 16.106f, 17.935f, 15.517f, 18.298f)
                lineTo(15.516f, 20.643f)
                curveTo(15.516f, 21.261f, 15.007f, 21.75f, 14.395f, 21.75f)
                lineTo(9.605f, 21.75f)
                curveTo(8.993f, 21.75f, 8.484f, 21.261f, 8.484f, 20.643f)
                lineTo(8.483f, 18.298f)
                curveTo(7.894f, 17.935f, 7.5f, 17.273f, 7.5f, 16.517f)
                lineTo(7.5f, 7.542f)
                curveTo(7.5f, 6.839f, 7.841f, 6.217f, 8.363f, 5.841f)
                lineTo(8.363f, 3.357f)
                curveTo(8.363f, 2.731f, 8.886f, 2.25f, 9.501f, 2.25f)
                lineTo(14.499f, 2.25f)
                close()
                moveTo(14.016f, 18.592f)
                lineTo(9.983f, 18.592f)
                lineTo(9.984f, 20.249f)
                lineTo(14.016f, 20.249f)
                lineTo(14.016f, 18.592f)
                close()
                moveTo(14.482f, 6.85f)
                lineTo(9.518f, 6.85f)
                curveTo(9.489f, 6.85f, 9.461f, 6.852f, 9.433f, 6.855f)
                curveTo(9.369f, 6.889f, 9.298f, 6.91f, 9.223f, 6.921f)
                curveTo(9.0f, 7.033f, 8.846f, 7.269f, 8.846f, 7.542f)
                lineTo(8.846f, 16.517f)
                curveTo(8.846f, 16.824f, 9.04f, 17.084f, 9.309f, 17.175f)
                lineTo(9.234f, 17.168f)
                curveTo(9.313f, 17.168f, 9.389f, 17.181f, 9.461f, 17.203f)
                lineTo(9.518f, 17.209f)
                lineTo(14.482f, 17.209f)
                curveTo(14.501f, 17.209f, 14.521f, 17.208f, 14.54f, 17.206f)
                curveTo(14.58f, 17.191f, 14.621f, 17.181f, 14.664f, 17.175f)
                lineTo(14.691f, 17.175f)
                curveTo(14.96f, 17.084f, 15.154f, 16.824f, 15.154f, 16.517f)
                lineTo(15.154f, 7.542f)
                curveTo(15.154f, 7.269f, 15.0f, 7.033f, 14.777f, 6.92f)
                curveTo(14.702f, 6.91f, 14.632f, 6.889f, 14.566f, 6.857f)
                lineTo(14.482f, 6.85f)
                close()
                moveTo(14.136f, 3.75f)
                lineTo(9.863f, 3.75f)
                lineTo(9.863f, 5.466f)
                lineTo(14.136f, 5.466f)
                lineTo(14.136f, 3.75f)
                close()
            }
        }.build()

    val OneuiOtherDeviceFilled: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiOtherDeviceFilled",
            defaultWidth = 19.dp,
            defaultHeight = 19.dp,
            viewportWidth = 19.0f,
            viewportHeight = 19.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(6.982f, 10.38f)
                lineTo(4.916f, 10.38f)
                curveTo(3.891f, 10.38f, 3.055f, 11.215f, 3.055f, 12.238f)
                lineTo(3.055f, 14.302f)
                curveTo(3.055f, 15.325f, 3.891f, 16.16f, 4.916f, 16.16f)
                lineTo(6.982f, 16.16f)
                curveTo(8.006f, 16.16f, 8.842f, 15.325f, 8.842f, 14.302f)
                lineTo(8.842f, 12.238f)
                curveTo(8.842f, 11.215f, 8.006f, 10.38f, 6.982f, 10.38f)
                moveTo(12.239f, 3.069f)
                curveTo(11.215f, 3.069f, 10.379f, 3.905f, 10.379f, 4.928f)
                lineTo(10.379f, 6.992f)
                curveTo(10.379f, 8.016f, 11.215f, 8.85f, 12.239f, 8.85f)
                lineTo(14.306f, 8.85f)
                curveTo(15.33f, 8.85f, 16.166f, 8.016f, 16.166f, 6.992f)
                lineTo(16.166f, 4.928f)
                curveTo(16.166f, 3.905f, 15.33f, 3.069f, 14.306f, 3.069f)
                lineTo(12.239f, 3.069f)
                close()
                moveTo(6.092f, 3.0f)
                curveTo(4.387f, 3.0f, 3.0f, 4.386f, 3.0f, 6.088f)
                lineTo(3.0f, 8.357f)
                curveTo(3.0f, 8.916f, 3.455f, 9.371f, 4.016f, 9.371f)
                lineTo(4.099f, 9.371f)
                curveTo(4.658f, 9.371f, 5.116f, 8.916f, 5.116f, 8.357f)
                lineTo(5.116f, 6.939f)
                curveTo(5.116f, 6.383f, 4.664f, 5.93f, 4.107f, 5.925f)
                curveTo(4.192f, 4.905f, 5.047f, 4.098f, 6.092f, 4.098f)
                curveTo(7.135f, 4.098f, 7.992f, 4.905f, 8.075f, 5.925f)
                curveTo(7.517f, 5.93f, 7.067f, 6.383f, 7.067f, 6.939f)
                lineTo(7.067f, 8.357f)
                curveTo(7.067f, 8.916f, 7.523f, 9.371f, 8.083f, 9.371f)
                lineTo(8.167f, 9.371f)
                curveTo(8.726f, 9.371f, 9.182f, 8.916f, 9.182f, 8.357f)
                lineTo(9.182f, 6.088f)
                curveTo(9.182f, 4.386f, 7.795f, 3.0f, 6.092f, 3.0f)
                moveTo(13.273f, 10.38f)
                curveTo(11.68f, 10.38f, 10.379f, 11.679f, 10.379f, 13.27f)
                curveTo(10.379f, 14.861f, 11.68f, 16.16f, 13.273f, 16.16f)
                lineTo(13.429f, 16.156f)
                curveTo(14.966f, 16.063f, 16.166f, 14.796f, 16.166f, 13.27f)
                curveTo(16.166f, 11.679f, 14.865f, 10.38f, 13.273f, 10.38f)
            }
        }.build()

    val OneuiOtherDeviceTone: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiOtherDeviceTone",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF3B3B3F)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(8.394f, 13.173f)
                curveTo(9.783f, 13.173f, 10.915f, 14.306f, 10.915f, 15.695f)
                lineTo(10.915f, 18.496f)
                curveTo(10.915f, 19.885f, 9.783f, 21.017f, 8.394f, 21.017f)
                lineTo(5.593f, 21.017f)
                curveTo(4.204f, 21.017f, 3.071f, 19.885f, 3.071f, 18.496f)
                lineTo(3.071f, 15.695f)
                curveTo(3.071f, 14.306f, 4.204f, 13.173f, 5.593f, 13.173f)
                close()
                moveTo(17.078f, 13.173f)
                curveTo(19.237f, 13.173f, 21.0f, 14.937f, 21.0f, 17.095f)
                curveTo(21.0f, 19.165f, 19.375f, 20.885f, 17.291f, 21.011f)
                lineTo(17.078f, 21.017f)
                curveTo(14.919f, 21.017f, 13.156f, 19.254f, 13.156f, 17.095f)
                curveTo(13.156f, 14.937f, 14.919f, 13.173f, 17.078f, 13.173f)
                close()
                moveTo(8.394f, 14.854f)
                lineTo(5.593f, 14.854f)
                curveTo(5.132f, 14.854f, 4.752f, 15.234f, 4.752f, 15.695f)
                lineTo(4.752f, 18.496f)
                curveTo(4.752f, 18.956f, 5.132f, 19.336f, 5.593f, 19.336f)
                lineTo(8.394f, 19.336f)
                curveTo(8.854f, 19.336f, 9.234f, 18.956f, 9.234f, 18.496f)
                lineTo(9.234f, 15.695f)
                curveTo(9.234f, 15.234f, 8.854f, 14.854f, 8.394f, 14.854f)
                close()
                moveTo(17.078f, 14.854f)
                curveTo(15.847f, 14.854f, 14.837f, 15.865f, 14.837f, 17.095f)
                curveTo(14.837f, 18.326f, 15.847f, 19.336f, 17.053f, 19.337f)
                lineTo(17.214f, 19.332f)
                curveTo(18.387f, 19.261f, 19.319f, 18.275f, 19.319f, 17.095f)
                curveTo(19.319f, 15.865f, 18.309f, 14.854f, 17.078f, 14.854f)
                close()
                moveTo(6.939f, 3.0f)
                curveTo(9.109f, 3.0f, 10.876f, 4.767f, 10.876f, 6.938f)
                lineTo(10.876f, 9.831f)
                curveTo(10.876f, 10.543f, 10.295f, 11.124f, 9.582f, 11.124f)
                lineTo(9.476f, 11.124f)
                curveTo(8.762f, 11.124f, 8.181f, 10.543f, 8.181f, 9.831f)
                lineTo(8.181f, 8.023f)
                curveTo(8.181f, 7.313f, 8.755f, 6.736f, 9.465f, 6.73f)
                curveTo(9.359f, 5.428f, 8.268f, 4.401f, 6.939f, 4.401f)
                curveTo(5.609f, 4.401f, 4.518f, 5.428f, 4.411f, 6.73f)
                curveTo(5.12f, 6.736f, 5.696f, 7.313f, 5.696f, 8.023f)
                lineTo(5.696f, 9.831f)
                curveTo(5.696f, 10.543f, 5.113f, 11.124f, 4.401f, 11.124f)
                lineTo(4.294f, 11.124f)
                curveTo(3.58f, 11.124f, 3.0f, 10.543f, 3.0f, 9.831f)
                lineTo(3.0f, 6.938f)
                curveTo(3.0f, 4.767f, 4.767f, 3.0f, 6.939f, 3.0f)
                close()
                moveTo(18.479f, 3.089f)
                curveTo(19.867f, 3.089f, 21.0f, 4.221f, 21.0f, 5.61f)
                lineTo(21.0f, 8.411f)
                curveTo(21.0f, 9.8f, 19.867f, 10.932f, 18.479f, 10.932f)
                lineTo(15.677f, 10.932f)
                curveTo(14.289f, 10.932f, 13.156f, 9.8f, 13.156f, 8.411f)
                lineTo(13.156f, 5.61f)
                curveTo(13.156f, 4.221f, 14.289f, 3.089f, 15.677f, 3.089f)
                close()
                moveTo(18.479f, 4.769f)
                lineTo(15.677f, 4.769f)
                curveTo(15.217f, 4.769f, 14.837f, 5.15f, 14.837f, 5.61f)
                lineTo(14.837f, 8.411f)
                curveTo(14.837f, 8.871f, 15.217f, 9.252f, 15.677f, 9.252f)
                lineTo(18.479f, 9.252f)
                curveTo(18.939f, 9.252f, 19.319f, 8.871f, 19.319f, 8.411f)
                lineTo(19.319f, 5.61f)
                curveTo(19.319f, 5.15f, 18.939f, 4.769f, 18.479f, 4.769f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFCCCCCC)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(5.593f, 14.854f)
                lineTo(8.394f, 14.854f)
                curveTo(8.854f, 14.854f, 9.234f, 15.234f, 9.234f, 15.695f)
                lineTo(9.234f, 18.496f)
                curveTo(9.234f, 18.956f, 8.854f, 19.336f, 8.394f, 19.336f)
                lineTo(5.593f, 19.336f)
                curveTo(5.132f, 19.336f, 4.752f, 18.956f, 4.752f, 18.496f)
                lineTo(4.752f, 15.695f)
                curveTo(4.752f, 15.234f, 5.132f, 14.854f, 5.593f, 14.854f)
                close()
                moveTo(17.078f, 14.854f)
                curveTo(18.309f, 14.854f, 19.319f, 15.865f, 19.319f, 17.095f)
                curveTo(19.319f, 18.275f, 18.387f, 19.261f, 17.214f, 19.332f)
                lineTo(17.053f, 19.337f)
                curveTo(15.847f, 19.336f, 14.837f, 18.326f, 14.837f, 17.095f)
                curveTo(14.837f, 15.865f, 15.847f, 14.854f, 17.078f, 14.854f)
                close()
                moveTo(15.677f, 4.769f)
                lineTo(18.479f, 4.769f)
                curveTo(18.939f, 4.769f, 19.319f, 5.15f, 19.319f, 5.61f)
                lineTo(19.319f, 8.411f)
                curveTo(19.319f, 8.871f, 18.939f, 9.252f, 18.479f, 9.252f)
                lineTo(15.677f, 9.252f)
                curveTo(15.217f, 9.252f, 14.837f, 8.871f, 14.837f, 8.411f)
                lineTo(14.837f, 5.61f)
                curveTo(14.837f, 5.15f, 15.217f, 4.769f, 15.677f, 4.769f)
                close()
            }
        }.build()

    val OneuiPhoneFilled: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiPhoneFilled",
            defaultWidth = 19.dp,
            defaultHeight = 19.dp,
            viewportWidth = 19.0f,
            viewportHeight = 19.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(10.74f, 14.46f)
                lineTo(8.259f, 14.46f)
                curveTo(7.931f, 14.46f, 7.665f, 14.194f, 7.665f, 13.866f)
                curveTo(7.665f, 13.538f, 7.931f, 13.272f, 8.259f, 13.272f)
                lineTo(10.74f, 13.272f)
                curveTo(11.068f, 13.272f, 11.333f, 13.538f, 11.333f, 13.866f)
                curveTo(11.333f, 14.194f, 11.068f, 14.46f, 10.74f, 14.46f)
                moveTo(12.37f, 2.474f)
                lineTo(6.63f, 2.474f)
                curveTo(5.537f, 2.474f, 4.651f, 3.36f, 4.651f, 4.453f)
                lineTo(4.651f, 14.547f)
                curveTo(4.651f, 15.64f, 5.537f, 16.526f, 6.63f, 16.526f)
                lineTo(12.37f, 16.526f)
                curveTo(13.462f, 16.526f, 14.349f, 15.64f, 14.349f, 14.547f)
                lineTo(14.349f, 4.453f)
                curveTo(14.349f, 3.36f, 13.462f, 2.474f, 12.37f, 2.474f)
            }
        }.build()

    val OneuiPhoneTone: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiPhoneTone",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF3B3B3F)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(15.069f, 3.0f)
                curveTo(16.964f, 3.0f, 18.5f, 4.536f, 18.5f, 6.431f)
                lineTo(18.5f, 17.569f)
                curveTo(18.5f, 19.464f, 16.964f, 21.0f, 15.069f, 21.0f)
                lineTo(8.931f, 21.0f)
                curveTo(7.036f, 21.0f, 5.5f, 19.464f, 5.5f, 17.569f)
                lineTo(5.5f, 6.431f)
                curveTo(5.5f, 4.536f, 7.036f, 3.0f, 8.931f, 3.0f)
                close()
                moveTo(15.069f, 4.5f)
                lineTo(8.931f, 4.5f)
                curveTo(7.864f, 4.5f, 7.0f, 5.364f, 7.0f, 6.431f)
                lineTo(7.0f, 17.569f)
                curveTo(7.0f, 18.636f, 7.864f, 19.5f, 8.931f, 19.5f)
                lineTo(15.069f, 19.5f)
                curveTo(16.135f, 19.5f, 17.0f, 18.635f, 17.0f, 17.569f)
                lineTo(17.0f, 6.431f)
                curveTo(17.0f, 5.365f, 16.135f, 4.5f, 15.069f, 4.5f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFCCCCCC)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(8.931f, 4.5f)
                lineTo(15.069f, 4.5f)
                curveTo(16.135f, 4.5f, 17.0f, 5.365f, 17.0f, 6.431f)
                lineTo(17.0f, 17.569f)
                curveTo(17.0f, 18.635f, 16.135f, 19.5f, 15.069f, 19.5f)
                lineTo(8.931f, 19.5f)
                curveTo(7.864f, 19.5f, 7.0f, 18.636f, 7.0f, 17.569f)
                lineTo(7.0f, 6.431f)
                curveTo(7.0f, 5.364f, 7.864f, 4.5f, 8.931f, 4.5f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF3B3B3F)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(10.4f, 16.08f)
                lineTo(13.6f, 16.08f)
                arcTo(0.75f, 0.75f, 0.0f, false, true, 14.35f, 16.83f)
                lineTo(14.35f, 16.83f)
                arcTo(0.75f, 0.75f, 0.0f, false, true, 13.6f, 17.58f)
                lineTo(10.4f, 17.58f)
                arcTo(0.75f, 0.75f, 0.0f, false, true, 9.65f, 16.83f)
                lineTo(9.65f, 16.83f)
                arcTo(0.75f, 0.75f, 0.0f, false, true, 10.4f, 16.08f)
                close()
            }
        }.build()

    val OneuiRingFilled: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiRingFilled",
            defaultWidth = 19.dp,
            defaultHeight = 19.dp,
            viewportWidth = 19.0f,
            viewportHeight = 19.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(12.813f, 12.813f)
                curveTo(10.541f, 15.086f, 7.879f, 15.991f, 6.993f, 15.106f)
                curveTo(6.711f, 14.823f, 6.61f, 14.359f, 6.679f, 13.785f)
                curveTo(8.258f, 13.524f, 9.91f, 12.63f, 11.269f, 11.271f)
                curveTo(12.628f, 9.913f, 13.523f, 8.261f, 13.782f, 6.679f)
                lineTo(13.902f, 6.667f)
                curveTo(14.422f, 6.625f, 14.843f, 6.73f, 15.106f, 6.993f)
                curveTo(15.991f, 7.879f, 15.086f, 10.541f, 12.813f, 12.813f)
                moveTo(12.277f, 7.139f)
                lineTo(12.35f, 7.107f)
                curveTo(12.025f, 8.218f, 11.33f, 9.366f, 10.347f, 10.349f)
                curveTo(9.363f, 11.333f, 8.215f, 12.027f, 7.105f, 12.351f)
                curveTo(7.538f, 11.378f, 8.277f, 10.294f, 9.286f, 9.286f)
                curveTo(10.268f, 8.303f, 11.323f, 7.576f, 12.277f, 7.139f)
                moveTo(16.027f, 6.072f)
                lineTo(16.016f, 6.061f)
                lineTo(12.834f, 3.026f)
                lineTo(12.733f, 2.932f)
                curveTo(11.082f, 1.488f, 7.444f, 2.47f, 4.957f, 4.957f)
                curveTo(2.412f, 7.502f, 1.442f, 11.252f, 3.036f, 12.845f)
                lineTo(3.026f, 12.834f)
                lineTo(6.032f, 15.985f)
                lineTo(6.071f, 16.027f)
                curveTo(7.644f, 17.6f, 11.018f, 16.452f, 13.735f, 13.735f)
                curveTo(16.451f, 11.019f, 17.6f, 7.644f, 16.027f, 6.072f)
            }
        }.build()

    val OneuiRingTone: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiRingTone",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFCCCCCC)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(17.069f, 8.743f)
                curveTo(17.668f, 8.694f, 18.152f, 8.815f, 18.455f, 9.118f)
                curveTo(19.474f, 10.137f, 18.432f, 13.202f, 15.817f, 15.816f)
                curveTo(13.202f, 18.432f, 10.138f, 19.473f, 9.118f, 18.454f)
                curveTo(8.793f, 18.129f, 8.678f, 17.595f, 8.757f, 16.934f)
                curveTo(10.574f, 16.634f, 12.475f, 15.605f, 14.039f, 14.041f)
                curveTo(15.603f, 12.478f, 16.633f, 10.576f, 16.932f, 8.757f)
                close()
                moveTo(7.836f, 7.836f)
                curveTo(10.256f, 5.416f, 13.694f, 4.527f, 14.793f, 5.626f)
                lineTo(14.805f, 5.638f)
                lineTo(16.55f, 7.301f)
                curveTo(14.736f, 7.585f, 12.578f, 8.813f, 10.696f, 10.696f)
                curveTo(8.813f, 12.579f, 7.585f, 14.736f, 7.301f, 16.55f)
                lineTo(5.638f, 14.806f)
                lineTo(5.625f, 14.793f)
                curveTo(4.527f, 13.694f, 5.416f, 10.256f, 7.836f, 7.836f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF3B3B3F)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(15.721f, 4.441f)
                lineTo(15.837f, 4.549f)
                lineTo(19.499f, 8.042f)
                lineTo(19.512f, 8.054f)
                curveTo(21.321f, 9.863f, 20.0f, 13.748f, 16.874f, 16.874f)
                curveTo(13.747f, 20.0f, 9.864f, 21.321f, 8.054f, 19.512f)
                lineTo(8.008f, 19.463f)
                lineTo(4.549f, 15.837f)
                lineTo(4.561f, 15.85f)
                curveTo(2.727f, 14.015f, 3.843f, 9.7f, 6.772f, 6.772f)
                curveTo(9.634f, 3.909f, 13.82f, 2.779f, 15.721f, 4.441f)
                close()
                moveTo(17.065f, 8.74f)
                lineTo(16.929f, 8.753f)
                curveTo(16.629f, 10.573f, 15.599f, 12.475f, 14.035f, 14.038f)
                curveTo(12.472f, 15.602f, 10.57f, 16.631f, 8.753f, 16.931f)
                curveTo(8.674f, 17.592f, 8.789f, 18.125f, 9.115f, 18.451f)
                curveTo(10.134f, 19.47f, 13.198f, 18.428f, 15.813f, 15.813f)
                curveTo(18.428f, 13.198f, 19.47f, 10.134f, 18.451f, 9.115f)
                curveTo(18.148f, 8.812f, 17.664f, 8.691f, 17.065f, 8.74f)
                close()
                moveTo(7.832f, 7.832f)
                curveTo(5.412f, 10.252f, 4.523f, 13.69f, 5.622f, 14.789f)
                curveTo(5.629f, 14.797f, 5.632f, 14.799f, 5.635f, 14.802f)
                lineTo(7.297f, 16.546f)
                curveTo(7.581f, 14.732f, 8.809f, 12.575f, 10.692f, 10.692f)
                curveTo(12.575f, 8.809f, 14.732f, 7.582f, 16.546f, 7.298f)
                lineTo(14.802f, 5.635f)
                lineTo(14.789f, 5.622f)
                curveTo(13.69f, 4.523f, 10.253f, 5.412f, 7.832f, 7.832f)
                close()
                moveTo(15.28f, 9.246f)
                lineTo(15.195f, 9.282f)
                curveTo(14.098f, 9.786f, 12.884f, 10.622f, 11.753f, 11.753f)
                curveTo(10.592f, 12.913f, 9.742f, 14.162f, 9.243f, 15.281f)
                curveTo(10.521f, 14.908f, 11.843f, 14.109f, 12.974f, 12.977f)
                curveTo(14.106f, 11.846f, 14.906f, 10.524f, 15.28f, 9.246f)
                close()
            }
        }.build()

    val OneuiSearch: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiSearch",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color.Transparent),
                strokeLineWidth = 0f
            ) {
                moveTo(4.7876f, 10.4115f)
                curveTo(4.7876f, 7.3095f, 7.3106f, 4.7875f, 10.4126f, 4.7875f)
                curveTo(13.5146f, 4.7875f, 16.0376f, 7.3095f, 16.0376f, 10.4115f)
                curveTo(16.0376f, 13.5135f, 13.5146f, 16.0375f, 10.4126f, 16.0375f)
                curveTo(7.3106f, 16.0375f, 4.7876f, 13.5135f, 4.7876f, 10.4115f)
                moveTo(20.4926f, 19.4325f)
                lineTo(15.9506f, 14.8895f)
                curveTo(16.9416f, 13.6645f, 17.5376f, 12.1065f, 17.5376f, 10.4115f)
                curveTo(17.5376f, 6.4825f, 14.3416f, 3.2875f, 10.4126f, 3.2875f)
                curveTo(6.4836f, 3.2875f, 3.2876f, 6.4825f, 3.2876f, 10.4115f)
                curveTo(3.2876f, 14.3405f, 6.4836f, 17.5375f, 10.4126f, 17.5375f)
                curveTo(12.1066f, 17.5375f, 13.6656f, 16.9405f, 14.8896f, 15.9495f)
                lineTo(19.4326f, 20.4925f)
                curveTo(19.5786f, 20.6395f, 19.7706f, 20.7125f, 19.9626f, 20.7125f)
                curveTo(20.1546f, 20.7125f, 20.3466f, 20.6395f, 20.4926f, 20.4925f)
                curveTo(20.7856f, 20.1995f, 20.7856f, 19.7245f, 20.4926f, 19.4325f)
            }
        }.build()

    val OneuiSettings: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiSettings",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                stroke = SolidColor(Color.Black),
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 4.0f,
            ) {
                moveTo(13.3856f, 3.13989f)
                curveTo(13.8582f, 3.13989f, 14.3088f, 3.41759f, 14.5588f, 3.86619f)
                lineTo(14.6219f, 3.99269f)
                lineTo(14.6691f, 4.13469f)
                lineTo(15.1121f, 5.86049f)
                lineTo(15.1526f, 5.89389f)
                curveTo(15.2432f, 5.97669f, 15.4617f, 6.18669f, 15.5527f, 6.24479f)
                curveTo(15.5427f, 6.23829f, 15.5523f, 6.23649f, 15.5701f, 6.23649f)
                lineTo(15.6656f, 6.23699f)
                lineTo(15.7338f, 6.22409f)
                lineTo(17.4439f, 5.74739f)
                curveTo(17.9001f, 5.61999f, 18.4098f, 5.76679f, 18.7707f, 6.13419f)
                lineTo(18.8653f, 6.23979f)
                lineTo(18.9471f, 6.36229f)
                lineTo(20.3352f, 8.77539f)
                curveTo(20.5639f, 9.17449f, 20.5547f, 9.68679f, 20.3069f, 10.1266f)
                lineTo(20.2254f, 10.2561f)
                lineTo(20.1156f, 10.3824f)
                lineTo(18.8475f, 11.6325f)
                curveTo(18.8112f, 11.6682f, 18.77f, 11.7416f, 18.7555f, 11.753f)
                lineTo(18.7502f, 11.7483f)
                lineTo(18.7476f, 11.8144f)
                lineTo(18.7457f, 12.227f)
                curveTo(18.7467f, 12.213f, 18.7528f, 12.2184f, 18.7609f, 12.2317f)
                lineTo(18.8019f, 12.3051f)
                lineTo(18.8476f, 12.3583f)
                lineTo(20.118f, 13.6096f)
                curveTo(20.4459f, 13.9359f, 20.5739f, 14.4371f, 20.4472f, 14.9309f)
                lineTo(20.4063f, 15.0652f)
                lineTo(20.3346f, 15.2152f)
                lineTo(18.947f, 17.6276f)
                curveTo(18.7111f, 18.0375f, 18.2447f, 18.2897f, 17.7304f, 18.2812f)
                lineTo(17.5891f, 18.2724f)
                lineTo(17.4442f, 18.2424f)
                lineTo(15.7335f, 17.7655f)
                curveTo(15.7515f, 17.7706f, 15.3099f, 17.8915f, 15.1922f, 17.9529f)
                curveTo(15.2101f, 17.9434f, 15.174f, 17.9931f, 15.1508f, 18.0324f)
                lineTo(15.137f, 18.0588f)
                lineTo(15.1134f, 18.1279f)
                lineTo(14.6686f, 19.8567f)
                curveTo(14.55f, 20.3138f, 14.1686f, 20.6808f, 13.6716f, 20.8104f)
                lineTo(13.5333f, 20.8398f)
                lineTo(13.3856f, 20.8498f)
                horizontalLineTo(10.6144f)
                curveTo(10.1414f, 20.8498f, 9.6902f, 20.5716f, 9.4409f, 20.1218f)
                lineTo(9.3781f, 19.995f)
                lineTo(9.3321f, 19.8557f)
                lineTo(8.8868f, 18.1286f)
                curveTo(8.8909f, 18.1447f, 8.564f, 17.8197f, 8.4487f, 17.7455f)
                curveTo(8.4583f, 17.7516f, 8.4482f, 17.7531f, 8.4302f, 17.753f)
                lineTo(8.3352f, 17.7521f)
                lineTo(8.2653f, 17.7656f)
                lineTo(6.5561f, 18.2423f)
                curveTo(6.1001f, 18.3696f, 5.5908f, 18.2224f, 5.2299f, 17.8559f)
                lineTo(5.1353f, 17.7506f)
                lineTo(5.0528f, 17.6273f)
                lineTo(3.6675f, 15.2174f)
                curveTo(3.4362f, 14.8184f, 3.4443f, 14.3048f, 3.6927f, 13.8638f)
                lineTo(3.7744f, 13.7338f)
                lineTo(3.8844f, 13.6073f)
                lineTo(5.1515f, 12.3582f)
                curveTo(5.1878f, 12.3225f, 5.2294f, 12.2484f, 5.244f, 12.2354f)
                lineTo(5.2524f, 12.1753f)
                lineTo(5.2541f, 11.7661f)
                curveTo(5.2522f, 11.7928f, 5.2297f, 11.742f, 5.2105f, 11.7067f)
                lineTo(5.1971f, 11.6846f)
                lineTo(5.1515f, 11.6314f)
                lineTo(3.8824f, 10.3814f)
                curveTo(3.5531f, 10.0555f, 3.4258f, 9.55389f, 3.5525f, 9.05979f)
                lineTo(3.5933f, 8.92539f)
                lineTo(3.6654f, 8.77449f)
                lineTo(5.0523f, 6.36319f)
                curveTo(5.2882f, 5.95179f, 5.7549f, 5.69959f, 6.2697f, 5.70839f)
                lineTo(6.4112f, 5.71739f)
                lineTo(6.5559f, 5.74739f)
                lineTo(8.2631f, 6.22349f)
                curveTo(8.2462f, 6.21879f, 8.6888f, 6.09739f, 8.811f, 6.03369f)
                curveTo(8.8009f, 6.03889f, 8.8047f, 6.02929f, 8.8139f, 6.01339f)
                lineTo(8.8635f, 5.92829f)
                lineTo(8.8635f, 5.92829f)
                lineTo(8.8866f, 5.86179f)
                lineTo(9.3308f, 4.13539f)
                curveTo(9.448f, 3.67689f, 9.8304f, 3.30919f, 10.3281f, 3.17929f)
                lineTo(10.4666f, 3.14989f)
                lineTo(10.6144f, 3.13989f)
                horizontalLineTo(13.3856f)
                close()
                moveTo(10.6912f, 4.56159f)
                lineTo(10.2537f, 6.25969f)
                lineTo(10.2047f, 6.40659f)
                curveTo(10.0605f, 6.77809f, 9.7899f, 7.12969f, 9.4699f, 7.29669f)
                curveTo(9.0923f, 7.49369f, 8.4617f, 7.67599f, 8.0217f, 7.62299f)
                lineTo(7.8826f, 7.59629f)
                lineTo(6.2494f, 7.13719f)
                lineTo(4.9341f, 9.41739f)
                lineTo(6.1821f, 10.6491f)
                lineTo(6.2855f, 10.7664f)
                curveTo(6.5042f, 11.0415f, 6.6576f, 11.3953f, 6.677f, 11.6914f)
                lineTo(6.6764f, 12.2022f)
                lineTo(6.6679f, 12.3554f)
                curveTo(6.6281f, 12.6679f, 6.4677f, 13.0105f, 6.2486f, 13.2681f)
                lineTo(6.1513f, 13.373f)
                lineTo(4.9351f, 14.5657f)
                lineTo(6.2456f, 16.8497f)
                lineTo(7.9252f, 16.3829f)
                lineTo(8.0776f, 16.3516f)
                curveTo(8.4708f, 16.2908f, 8.9111f, 16.3508f, 9.2173f, 16.5461f)
                curveTo(9.5802f, 16.7797f, 10.057f, 17.2421f, 10.2253f, 17.6516f)
                lineTo(10.2661f, 17.7726f)
                lineTo(10.6893f, 19.4243f)
                lineTo(13.3076f, 19.4272f)
                lineTo(13.7456f, 17.7322f)
                lineTo(13.7944f, 17.5843f)
                curveTo(13.9371f, 17.2142f, 14.2078f, 16.8624f, 14.5291f, 16.6921f)
                curveTo(14.9079f, 16.4945f, 15.5387f, 16.3124f, 15.9777f, 16.3664f)
                lineTo(16.1163f, 16.3934f)
                lineTo(17.7502f, 16.8516f)
                lineTo(19.0646f, 14.5714f)
                lineTo(17.8169f, 13.3406f)
                lineTo(17.7136f, 13.2233f)
                curveTo(17.4942f, 12.9473f, 17.3407f, 12.5925f, 17.3224f, 12.2976f)
                lineTo(17.3234f, 11.7903f)
                lineTo(17.3315f, 11.6365f)
                curveTo(17.3704f, 11.3234f, 17.5308f, 10.9807f, 17.7502f, 10.7227f)
                lineTo(17.8477f, 10.6177f)
                lineTo(19.0646f, 9.42209f)
                lineTo(17.7531f, 7.13909f)
                lineTo(16.072f, 7.60709f)
                lineTo(15.9202f, 7.63799f)
                curveTo(15.5258f, 7.69829f, 15.0858f, 7.63829f, 14.7842f, 7.44419f)
                curveTo(14.4236f, 7.21419f, 13.9432f, 6.74839f, 13.7738f, 6.33769f)
                lineTo(13.7328f, 6.21639f)
                lineTo(13.3104f, 4.56439f)
                lineTo(10.6912f, 4.56159f)
                close()
                moveTo(11.9995f, 8.68609f)
                curveTo(13.8269f, 8.68609f, 15.3083f, 10.1674f, 15.3083f, 11.9949f)
                curveTo(15.3083f, 13.8216f, 13.8266f, 15.3036f, 11.9995f, 15.3036f)
                curveTo(10.1732f, 15.3036f, 8.6917f, 13.8214f, 8.6917f, 11.9949f)
                curveTo(8.6917f, 10.1676f, 10.1729f, 8.68609f, 11.9995f, 8.68609f)
                close()
                moveTo(11.9995f, 10.1107f)
                curveTo(10.9597f, 10.1107f, 10.1163f, 10.9543f, 10.1163f, 11.9949f)
                curveTo(10.1163f, 13.0348f, 10.9601f, 13.879f, 11.9995f, 13.879f)
                curveTo(13.0398f, 13.879f, 13.8837f, 13.0349f, 13.8837f, 11.9949f)
                curveTo(13.8837f, 10.9542f, 13.0402f, 10.1107f, 11.9995f, 10.1107f)
                close()
            }
        }.build()

    val OneuiTabletFilled: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiTabletFilled",
            defaultWidth = 19.dp,
            defaultHeight = 19.dp,
            viewportWidth = 19.0f,
            viewportHeight = 19.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(10.741f, 12.982f)
                lineTo(8.259f, 12.982f)
                curveTo(7.932f, 12.982f, 7.666f, 12.716f, 7.666f, 12.388f)
                curveTo(7.666f, 12.06f, 7.932f, 11.794f, 8.259f, 11.794f)
                lineTo(10.741f, 11.794f)
                curveTo(11.068f, 11.794f, 11.334f, 12.06f, 11.334f, 12.388f)
                curveTo(11.334f, 12.716f, 11.068f, 12.982f, 10.741f, 12.982f)
                moveTo(14.448f, 3.661f)
                lineTo(4.552f, 3.661f)
                curveTo(3.464f, 3.661f, 2.573f, 4.552f, 2.573f, 5.641f)
                lineTo(2.573f, 13.359f)
                curveTo(2.573f, 14.448f, 3.464f, 15.339f, 4.552f, 15.339f)
                lineTo(14.448f, 15.339f)
                curveTo(15.536f, 15.339f, 16.427f, 14.448f, 16.427f, 13.359f)
                lineTo(16.427f, 5.641f)
                curveTo(16.427f, 4.552f, 15.536f, 3.661f, 14.448f, 3.661f)
            }
        }.build()

    val OneuiTabletTone: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiTabletTone",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF3B3B3F)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(17.75f, 4.75f)
                curveTo(19.545f, 4.75f, 21.0f, 6.205f, 21.0f, 8.0f)
                lineTo(21.0f, 16.0f)
                curveTo(21.0f, 17.795f, 19.545f, 19.25f, 17.75f, 19.25f)
                lineTo(6.25f, 19.25f)
                curveTo(4.455f, 19.25f, 3.0f, 17.795f, 3.0f, 16.0f)
                lineTo(3.0f, 8.0f)
                curveTo(3.0f, 6.205f, 4.455f, 4.75f, 6.25f, 4.75f)
                close()
                moveTo(17.75f, 6.25f)
                lineTo(6.25f, 6.25f)
                curveTo(5.283f, 6.25f, 4.5f, 7.033f, 4.5f, 8.0f)
                lineTo(4.5f, 16.0f)
                curveTo(4.5f, 16.967f, 5.283f, 17.75f, 6.25f, 17.75f)
                lineTo(17.75f, 17.75f)
                curveTo(18.717f, 17.75f, 19.5f, 16.967f, 19.5f, 16.0f)
                lineTo(19.5f, 8.0f)
                curveTo(19.5f, 7.033f, 18.717f, 6.25f, 17.75f, 6.25f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFCCCCCC)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(6.25f, 6.25f)
                lineTo(17.75f, 6.25f)
                curveTo(18.717f, 6.25f, 19.5f, 7.033f, 19.5f, 8.0f)
                lineTo(19.5f, 16.0f)
                curveTo(19.5f, 16.967f, 18.717f, 17.75f, 17.75f, 17.75f)
                lineTo(6.25f, 17.75f)
                curveTo(5.283f, 17.75f, 4.5f, 16.967f, 4.5f, 16.0f)
                lineTo(4.5f, 8.0f)
                curveTo(4.5f, 7.033f, 5.283f, 6.25f, 6.25f, 6.25f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF3B3B3F)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(10.4f, 14.32f)
                lineTo(13.6f, 14.32f)
                arcTo(0.75f, 0.75f, 0.0f, false, true, 14.35f, 15.07f)
                lineTo(14.35f, 15.07f)
                arcTo(0.75f, 0.75f, 0.0f, false, true, 13.6f, 15.82f)
                lineTo(10.4f, 15.82f)
                arcTo(0.75f, 0.75f, 0.0f, false, true, 9.65f, 15.07f)
                lineTo(9.65f, 15.07f)
                arcTo(0.75f, 0.75f, 0.0f, false, true, 10.4f, 14.32f)
                close()
            }
        }.build()

    val OneuiWatchFilled: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiWatchFilled",
            defaultWidth = 19.dp,
            defaultHeight = 19.dp,
            viewportWidth = 19.0f,
            viewportHeight = 19.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(13.234f, 11.539f)
                curveTo(12.51f, 12.858f, 11.107f, 13.755f, 9.5f, 13.755f)
                curveTo(7.892f, 13.755f, 6.49f, 12.858f, 5.766f, 11.539f)
                curveTo(5.434f, 10.933f, 5.244f, 10.238f, 5.244f, 9.5f)
                curveTo(5.244f, 8.761f, 5.434f, 8.066f, 5.766f, 7.461f)
                curveTo(6.49f, 6.142f, 7.892f, 5.245f, 9.5f, 5.245f)
                curveTo(11.107f, 5.245f, 12.51f, 6.142f, 13.234f, 7.461f)
                curveTo(13.566f, 8.066f, 13.755f, 8.761f, 13.755f, 9.5f)
                curveTo(13.755f, 10.238f, 13.566f, 10.933f, 13.234f, 11.539f)
                moveTo(13.695f, 5.697f)
                curveTo(13.533f, 5.518f, 13.442f, 5.284f, 13.442f, 5.042f)
                lineTo(13.442f, 3.64f)
                curveTo(13.442f, 2.778f, 12.742f, 2.078f, 11.879f, 2.078f)
                lineTo(7.121f, 2.078f)
                curveTo(6.258f, 2.078f, 5.559f, 2.778f, 5.559f, 3.64f)
                lineTo(5.559f, 5.042f)
                curveTo(5.559f, 5.284f, 5.467f, 5.518f, 5.305f, 5.697f)
                curveTo(4.393f, 6.702f, 3.836f, 8.035f, 3.836f, 9.5f)
                curveTo(3.836f, 10.964f, 4.393f, 12.298f, 5.305f, 13.303f)
                curveTo(5.467f, 13.483f, 5.559f, 13.716f, 5.559f, 13.958f)
                lineTo(5.559f, 15.359f)
                curveTo(5.559f, 16.222f, 6.258f, 16.922f, 7.121f, 16.922f)
                lineTo(11.879f, 16.922f)
                curveTo(12.742f, 16.922f, 13.442f, 16.222f, 13.442f, 15.359f)
                lineTo(13.442f, 13.958f)
                curveTo(13.442f, 13.716f, 13.533f, 13.483f, 13.695f, 13.303f)
                curveTo(14.607f, 12.298f, 15.164f, 10.964f, 15.164f, 9.5f)
                curveTo(15.164f, 8.035f, 14.607f, 6.702f, 13.695f, 5.697f)
            }
        }.build()

    val OneuiWatchTone: ImageVector
        get() = ImageVector.Builder(
            name = "OneuiWatchTone",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFCCCCCC)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(8.473f, 17.169f)
                lineTo(8.48f, 17.22f)
                lineTo(8.48f, 19.673f)
                curveTo(8.48f, 19.865f, 8.762f, 20.024f, 9.128f, 20.05f)
                lineTo(9.23f, 20.053f)
                lineTo(14.73f, 20.053f)
                curveTo(15.109f, 20.053f, 15.423f, 19.91f, 15.473f, 19.725f)
                lineTo(15.48f, 19.673f)
                lineTo(15.48f, 17.24f)
                curveTo(15.48f, 17.133f, 13.676f, 17.951f, 11.875f, 17.938f)
                curveTo(10.163f, 17.926f, 8.449f, 17.078f, 8.473f, 17.169f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFCCCCCC)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(15.487f, 5.798f)
                lineTo(15.48f, 5.743f)
                lineTo(15.48f, 4.346f)
                curveTo(15.48f, 4.141f, 15.197f, 3.971f, 14.832f, 3.944f)
                lineTo(14.73f, 3.94f)
                lineTo(9.23f, 3.94f)
                curveTo(8.85f, 3.94f, 8.536f, 4.093f, 8.487f, 4.291f)
                lineTo(8.48f, 4.346f)
                lineTo(8.48f, 5.764f)
                curveTo(8.48f, 5.989f, 15.537f, 5.996f, 15.487f, 5.798f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF3B3B3F)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(14.73f, 2.44f)
                curveTo(15.92f, 2.44f, 16.896f, 3.366f, 16.975f, 4.536f)
                lineTo(16.98f, 4.69f)
                lineTo(16.98f, 6.954f)
                curveTo(18.229f, 8.218f, 19.0f, 9.955f, 19.0f, 11.872f)
                curveTo(19.0f, 13.79f, 18.229f, 15.528f, 16.98f, 16.792f)
                lineTo(16.98f, 19.303f)
                curveTo(16.98f, 20.493f, 16.054f, 21.469f, 14.884f, 21.548f)
                lineTo(14.73f, 21.553f)
                lineTo(9.23f, 21.553f)
                curveTo(8.038f, 21.553f, 7.064f, 20.627f, 6.985f, 19.457f)
                lineTo(6.98f, 19.303f)
                lineTo(6.979f, 16.75f)
                curveTo(5.754f, 15.489f, 5.0f, 13.769f, 5.0f, 11.872f)
                curveTo(5.0f, 9.976f, 5.754f, 8.256f, 6.979f, 6.995f)
                lineTo(6.98f, 4.69f)
                curveTo(6.98f, 3.5f, 7.904f, 2.524f, 9.076f, 2.445f)
                lineTo(9.23f, 2.44f)
                lineTo(14.73f, 2.44f)
                close()
                moveTo(8.48f, 17.924f)
                lineTo(8.48f, 19.303f)
                curveTo(8.48f, 19.683f, 8.762f, 19.996f, 9.128f, 20.046f)
                lineTo(9.23f, 20.053f)
                lineTo(14.73f, 20.053f)
                curveTo(15.109f, 20.053f, 15.423f, 19.77f, 15.473f, 19.405f)
                lineTo(15.48f, 19.303f)
                lineTo(15.48f, 17.948f)
                curveTo(14.455f, 18.536f, 13.267f, 18.873f, 12.0f, 18.873f)
                curveTo(10.717f, 18.873f, 9.514f, 18.527f, 8.48f, 17.924f)
                close()
                moveTo(12.0f, 6.372f)
                curveTo(8.963f, 6.372f, 6.5f, 8.835f, 6.5f, 11.872f)
                curveTo(6.5f, 14.91f, 8.962f, 17.372f, 12.0f, 17.372f)
                curveTo(15.038f, 17.372f, 17.5f, 14.91f, 17.5f, 11.872f)
                curveTo(17.5f, 8.835f, 15.037f, 6.372f, 12.0f, 6.372f)
                close()
                moveTo(14.73f, 3.94f)
                lineTo(9.23f, 3.94f)
                curveTo(8.85f, 3.94f, 8.536f, 4.222f, 8.487f, 4.588f)
                lineTo(8.48f, 4.69f)
                lineTo(8.479f, 5.821f)
                curveTo(9.513f, 5.218f, 10.716f, 4.872f, 12.0f, 4.872f)
                curveTo(13.267f, 4.872f, 14.455f, 5.209f, 15.48f, 5.798f)
                lineTo(15.48f, 4.69f)
                curveTo(15.48f, 4.311f, 15.197f, 3.997f, 14.832f, 3.947f)
                lineTo(14.73f, 3.94f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFCCCCCC)),
                stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 1.0f
            ) {
                moveTo(12.0f, 6.372f)
                curveTo(15.037f, 6.372f, 17.5f, 8.835f, 17.5f, 11.872f)
                curveTo(17.5f, 14.91f, 15.038f, 17.372f, 12.0f, 17.372f)
                curveTo(8.962f, 17.372f, 6.5f, 14.91f, 6.5f, 11.872f)
                curveTo(6.5f, 8.835f, 8.963f, 6.372f, 12.0f, 6.372f)
                close()
            }
        }.build()
}
