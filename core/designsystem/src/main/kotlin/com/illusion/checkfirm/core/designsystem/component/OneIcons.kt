package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object OneIcons {
    val CheckFirm: ImageVector
        get() = ImageVector.Builder(
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

    val Back: ImageVector
        get() = ImageVector.Builder(
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

    val Category: ImageVector
        get() = ImageVector.Builder(
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

    val ChevronRight: ImageVector
        get() = ImageVector.Builder(
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

    val Clear: ImageVector
        get() = ImageVector.Builder(
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

    val HelpDevice: ImageVector
        get() = ImageVector.Builder(
            defaultWidth = 28.dp,
            defaultHeight = 28.dp,
            viewportWidth = 28.0f,
            viewportHeight = 28.0f
        ).apply {
            path(
                fill = SolidColor(Color.White),
                fillAlpha = 1.0f,
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

    val HelpManual: ImageVector
        get() = ImageVector.Builder(
            defaultWidth = 28.dp,
            defaultHeight = 28.dp,
            viewportWidth = 28.0f,
            viewportHeight = 28.0f
        ).apply {
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

    val InfoCatcher: ImageVector
        get() = ImageVector.Builder(
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

    val Profile: ImageVector
        get() = ImageVector.Builder(
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

    val WelcomeSearch: ImageVector
        get() = ImageVector.Builder(
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
            }
        }.build()

    val Add: ImageVector
        get() = ImageVector.Builder(
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

    val Bookmark: ImageVector
        get() = ImageVector.Builder(
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

    val Device: ImageVector
        get() = ImageVector.Builder(
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

    val Search: ImageVector
        get() = ImageVector.Builder(
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

    val Settings: ImageVector
        get() = ImageVector.Builder(
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                fillAlpha = 1.0f,
                pathFillType = PathFillType.EvenOdd,
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
}
