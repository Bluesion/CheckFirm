package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

private val SwitchTrackWidth = 35.dp
private val SwitchTrackHeight = 20.dp
private val SwitchThumbDiameter = 16.dp
private val SwitchThumbInset = 2.dp
private val SwitchThumbStrokeWidth = 1.dp

@Composable
fun OneSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    val darkTheme = isSystemInDarkTheme()
    val checkedTrackColor = MaterialTheme.colorScheme.primary
    val uncheckedTrackColor = if (darkTheme) Color(0xFF44474E) else Color(0xFF99999E)
    val thumbColor = Color(0xFFFCFCFF)
    val uncheckedThumbStroke = if (darkTheme) Color(0xFFA1A1A1) else Color(0xFF8C8C8C)
    val checkedThumbStroke = MaterialTheme.colorScheme.primary

    val trackColor by animateColorAsState(
        targetValue = if (checked) checkedTrackColor else uncheckedTrackColor,
        animationSpec = tween(durationMillis = 200),
        label = "trackColor",
    )
    val thumbStrokeColor by animateColorAsState(
        targetValue = if (checked) checkedThumbStroke else uncheckedThumbStroke,
        animationSpec = tween(durationMillis = 200),
        label = "thumbStroke",
    )

    val thumbTravel = SwitchTrackWidth - SwitchThumbDiameter - (SwitchThumbInset * 2)
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) thumbTravel else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "thumbOffset",
    )

    val density = LocalDensity.current
    val thumbStrokePx = with(density) { SwitchThumbStrokeWidth.toPx() }

    val interactionSource = remember { MutableInteractionSource() }

    val baseModifier = if (onCheckedChange != null) {
        modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            role = Role.Switch,
            onClick = { onCheckedChange(!checked) },
        )
    } else modifier

    Box(
        modifier = baseModifier
            .size(width = SwitchTrackWidth, height = SwitchTrackHeight),
        contentAlignment = Alignment.CenterStart,
    ) {
        Canvas(
            modifier = Modifier
                .size(width = SwitchTrackWidth, height = SwitchTrackHeight)
                .clip(RoundedCornerShape(SwitchTrackHeight / 2)),
        ) {
            drawRoundRect(
                color = trackColor,
                size = Size(size.width, size.height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.height / 2),
            )
        }
        Box(
            modifier = Modifier
                .padding(start = SwitchThumbInset + thumbOffset)
                .size(SwitchThumbDiameter),
        ) {
            Canvas(modifier = Modifier.size(SwitchThumbDiameter)) {
                val radius = size.minDimension / 2f
                val center = Offset(radius, radius)
                drawCircle(color = thumbColor, radius = radius, center = center)
                drawCircle(
                    color = thumbStrokeColor,
                    radius = radius - thumbStrokePx / 2,
                    center = center,
                    style = Stroke(width = thumbStrokePx),
                )
            }
        }
    }
}

@Composable
fun OneSwitchCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textOn: String = stringResource(R.string.switch_on),
    textOff: String = stringResource(R.string.switch_off),
) {
    val darkTheme = isSystemInDarkTheme()
    val cardOff = if (darkTheme) Color(0xFF171719) else Color(0xFFFDFCFF)
    val cardOn = if (darkTheme) Color(0xFF053053) else Color(0xFFE3EAF0)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(role = Role.Switch) { onCheckedChange(!checked) },
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = if (checked) cardOn else cardOff),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (checked) textOn else textOff,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.weight(1f))
            OneSwitch(checked = checked, onCheckedChange = null)
        }
    }
}

@ComponentPreview
@Composable
private fun OneSwitchPreview() {
    CheckFirmTheme {
        Surface {
            var isChecked by remember { mutableStateOf(false) }
            OneSwitch(
                checked = isChecked,
                onCheckedChange = { isChecked = !isChecked },
            )
        }
    }
}

@ComponentPreview
@Composable
private fun OneSwitchCardPreview() {
    CheckFirmTheme {
        Surface {
            var isChecked by remember { mutableStateOf(false) }
            OneSwitchCard(
                checked = isChecked,
                onCheckedChange = { isChecked = !isChecked },
            )
        }
    }
}
