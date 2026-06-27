package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun OneLoadingIndicator(
    modifier: Modifier = Modifier,
    tint: Color? = null,
) {
    // When a tint is supplied, all four dots use it; otherwise each keeps its own color.
    val topColor = tint ?: Color(0xFF00E676)
    val rightColor = tint ?: Color(0xFF00B0FF)
    val bottomColor = tint ?: Color(0xFF00BFA5)
    val leftColor = tint ?: Color(0xFF2979FF)

    val infiniteTransition = rememberInfiniteTransition(label = "LoadingTransition")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RotationAnimation"
    )

    // Smooth organic ease-in/ease-out curve
    val smoothEase = CubicBezierEasing(0.42f, 0.0f, 0.58f, 1.0f)

    // 3. The Timing Sequence
    // 0f = Spread out, 1f = Tightly gathered
    val clusterProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2000

                // 1. Hold spread out (0ms to 420ms)
                0f at 0
                0f at 420 using smoothEase

                // 2. Inward and bounce (Finished in 1s)
                // Pull inward quickly (420ms to 850ms)
                1f at 850
                // Bounce/Hold at center (850ms to 1000ms)
                1f at 1000 using smoothEase

                // 3. Outward and reset (Finished within 230ms: 1000ms to 1230ms)
                0f at 1230

                // Remain spread out until the 2000ms loop restarts (ensures colorTop is at top)
                0f at 2000
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "ClusterProgress"
    )

    Canvas(
        modifier = modifier
            .sizeIn(minWidth = 24.dp, minHeight = 24.dp)
            .graphicsLayer {
                // CompositingStrategy.Offscreen is required for BlendMode.Screen
                // to mix overlapping colors into that bright teal/cyan center
                compositingStrategy = CompositingStrategy.Offscreen
            }
    ) {
        val center = Offset(size.width / 2, size.height / 2)

        // Sizes for spread out state
        val maxDist = size.minDimension / 3.5f
        val baseRadius = size.minDimension / 11f

        // Sizes for the gathered state
        val clusteredRadius = baseRadius * 1.8f
        val minDist = clusteredRadius * 0.15f // Creates that tight stacked look from your picture

        // Smoothly interpolate distance and size
        val currentDist = maxDist - ((maxDist - minDist) * clusterProgress)
        val currentRadius = baseRadius + ((clusteredRadius - baseRadius) * clusterProgress)

        rotate(rotation, center) {
            drawCircle(
                color = topColor,
                radius = currentRadius,
                center = Offset(center.x, center.y - currentDist),
                blendMode = BlendMode.Screen,
                alpha = 0.7f
            )
            drawCircle(
                color = rightColor,
                radius = currentRadius,
                center = Offset(center.x + currentDist, center.y),
                blendMode = BlendMode.Screen,
                alpha = 0.7f
            )
            drawCircle(
                color = bottomColor,
                radius = currentRadius,
                center = Offset(center.x, center.y + currentDist),
                blendMode = BlendMode.Screen,
                alpha = 0.7f
            )
            drawCircle(
                color = leftColor,
                radius = currentRadius,
                center = Offset(center.x - currentDist, center.y),
                blendMode = BlendMode.Screen,
                alpha = 0.7f
            )
        }
    }
}

@Composable
fun OneProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
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
private fun OneProgressIndicatorPreview() {
    CheckFirmTheme {
        Surface {
            OneProgressIndicator()
        }
    }
}

@ComponentPreview
@Composable
private fun OneLoadingIndicatorPreview() {
    CheckFirmTheme {
        Surface {
            OneLoadingIndicator()
        }
    }
}
