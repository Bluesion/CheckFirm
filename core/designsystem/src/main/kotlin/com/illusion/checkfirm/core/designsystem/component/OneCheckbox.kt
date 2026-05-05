package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun OneCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    checkedColor: Color = MaterialTheme.colorScheme.primary,
    uncheckedColor: Color = MaterialTheme.colorScheme.outline,
    checkmarkColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    val color by animateColorAsState(
        targetValue = if (checked) checkedColor else uncheckedColor,
        animationSpec = tween(durationMillis = 200),
        label = "checkbox_color"
    )

    val checkFraction by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "checkbox_check_fraction"
    )

    val toggleableModifier = if (onCheckedChange != null) {
        Modifier.toggleable(
            value = checked,
            onValueChange = onCheckedChange,
            role = Role.Checkbox,
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(
                bounded = false,
                radius = 20.dp
            )
        )
    } else {
        Modifier
    }

    Canvas(
        modifier = modifier
            .wrapContentSize(Alignment.Center)
            .padding(2.dp)
            .requiredSize(20.dp)
            .then(toggleableModifier)
    ) {
        val strokeWidth = 2.dp.toPx()
        val radius = size.minDimension / 2f - strokeWidth / 2f

        // Draw border
        drawCircle(
            color = color,
            radius = radius,
            style = Stroke(width = strokeWidth)
        )

        // Draw filled circle
        if (checkFraction > 0f) {
            drawCircle(
                color = color,
                radius = (size.minDimension / 2f) * checkFraction
            )
        }

        // Draw checkmark
        if (checkFraction > 0f) {
            val path = Path().apply {
                val width = size.width
                val height = size.height

                // Scales from the SVG viewBox (24x24) to the current canvas size
                val scaleX = width / 24f
                val scaleY = height / 24f

                moveTo(6f * scaleX, 11.4231f * scaleY)
                lineTo(10.1372f * scaleX, 15.2803f * scaleY)
                cubicTo(
                    10.5686f * scaleX, 15.6825f * scaleY,
                    11.2404f * scaleX, 15.6729f * scaleY,
                    11.6601f * scaleX, 15.2586f * scaleY
                )
                lineTo(18f * scaleX, 9f * scaleY)
            }

            val pathMeasure = androidx.compose.ui.graphics.PathMeasure()
            pathMeasure.setPath(path, false)

            val animatedPath = Path()
            pathMeasure.getSegment(
                startDistance = 0f,
                stopDistance = pathMeasure.length * checkFraction,
                destination = animatedPath,
                startWithMoveTo = true
            )

            drawPath(
                path = animatedPath,
                color = checkmarkColor,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}

@ComponentPreview
@Composable
private fun OneCheckboxPreview() {
    var isChecked by remember { mutableStateOf(false) }
    CheckFirmTheme {
        Surface {
            OneCheckbox(
                checked = isChecked,
                onCheckedChange = { isChecked = !isChecked },
            )
        }
    }
}
