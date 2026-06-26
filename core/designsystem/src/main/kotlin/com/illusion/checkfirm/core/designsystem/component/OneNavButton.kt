package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

private val NavButtonShadowElevation = 6.dp

/**
 * Visibility of [OneNavButton]'s background, evaluated lazily at draw time. Defaults to fully
 * visible. [OneScaffold] overrides it so the background only appears once content has scrolled
 * up behind the collapsing toolbar (i.e. when the toolbar title is hidden), and is always
 * hidden on the fixed toolbar.
 */
val LocalOneNavButtonBackgroundAlpha = compositionLocalOf<() -> Float> { { 1f } }

@Composable
fun OneNavButton(
    onClick: () -> Unit,
    shape: Shape,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val backgroundColor = MaterialTheme.colorScheme.surfaceBright
    val backgroundAlpha = LocalOneNavButtonBackgroundAlpha.current

    // No clip on the outer box, otherwise the drop shadow would be clipped away.
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        // Solid background with a drop shadow, shown only when content has scrolled behind the
        // toolbar (see OneScaffold). The fill alpha and the shadow both track the same value,
        // read at draw time so scrolling doesn't recompose.
        Box(
            modifier = Modifier
                .matchParentSize()
                .graphicsLayer {
                    val visible = backgroundAlpha()
                    alpha = visible
                    shadowElevation = NavButtonShadowElevation.toPx() * visible
                    this.shape = shape
                    clip = true
                }
                .background(color = backgroundColor, shape = shape),
        )

        // Separate clickable layer clipped to the shape so the ripple stays circular and the
        // icon on top is never affected by the background's alpha.
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(shape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

@ComponentPreview
@Composable
private fun OneNavButtonPreview() {
    CheckFirmTheme {
        Surface {
            OneNavButton(
                onClick = {},
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    imageVector = OneIcons.Back,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}
