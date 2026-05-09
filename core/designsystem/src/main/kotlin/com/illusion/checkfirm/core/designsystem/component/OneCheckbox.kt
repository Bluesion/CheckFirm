package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun OneCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checkedColor: Color = MaterialTheme.colorScheme.primary,
    uncheckedColor: Color = MaterialTheme.colorScheme.outline,
    checkmarkColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    val color by animateColorAsState(
        targetValue = if (isChecked) checkedColor else uncheckedColor,
        animationSpec = tween(durationMillis = 200),
        label = "checkbox_color"
    )

    val checkFraction by animateFloatAsState(
        targetValue = if (isChecked) 1f else 0f,
        animationSpec = tween(durationMillis = 200),
        label = "checkbox_check_fraction"
    )

    Canvas(
        modifier = modifier
            .wrapContentSize(Alignment.Center)
            .padding(2.dp)
            .requiredSize(20.dp)
            .toggleable(
                value = isChecked,
                onValueChange = onCheckedChange,
                role = Role.Checkbox,
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    bounded = false,
                    radius = 20.dp
                )
            ),
    ) {
        val strokeWidth = 2.dp.toPx()
        val radius = size.minDimension / 2f - strokeWidth / 2f

        drawCircle(
            color = color,
            radius = radius,
            style = Stroke(width = strokeWidth)
        )

        if (checkFraction > 0f) {
            drawCircle(
                color = color,
                radius = (size.minDimension / 2f) * checkFraction
            )
        }

        if (checkFraction > 0f) {
            val path = Path().apply {
                val width = size.width
                val height = size.height

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

@Composable
fun OneCheckboxCard(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    OneCard(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = text,
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(12.dp))

            OneCheckbox(
                isChecked = isChecked,
                onCheckedChange = onCheckedChange,
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
                isChecked = isChecked,
                onCheckedChange = { isChecked = !isChecked },
            )
        }
    }
}

@ComponentPreview
@Composable
private fun OneCheckboxCardPreview() {
    var isChecked by remember { mutableStateOf(false) }
    CheckFirmTheme {
        Surface {
            OneCheckboxCard(
                text = "OneCheckboxCard",
                isChecked = isChecked,
                onCheckedChange = { isChecked = !isChecked },
            )
        }
    }
}
