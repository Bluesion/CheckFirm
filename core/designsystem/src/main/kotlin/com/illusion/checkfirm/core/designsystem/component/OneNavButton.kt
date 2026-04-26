package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun OneNavButton(
    icon: ImageVector,
    isEncapsulated: Boolean = true,
) {
    // Animate padding and background alpha

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(40.dp)
            // 4. Encapsulation: Drop shadow, Blur, and Background
            .then(
                if (isEncapsulated) {
                    Modifier
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .background(Color(0xFF2C2C2E).copy(alpha = 0.7f), CircleShape)
                        .blur(8.dp) // Blur requires API 31+
                } else Modifier
            )
            .clip(CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp),
        )
    }
}

@ComponentPreview
@Composable
private fun OneNavButtonPreview() {
    CheckFirmTheme {
        OneNavButton(icon = OneIcons.IcBack)
    }
}
