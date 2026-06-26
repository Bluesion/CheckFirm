package com.illusion.checkfirm.core.designsystem.component

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun OneNavButton(
    icon: ImageVector,
    onClick: () -> Unit,
    isEncapsulated: Boolean = Build.VERSION.SDK_INT >= 31,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(40.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (isEncapsulated) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color(0xFF2C2C2E).copy(alpha = 0.7f), CircleShape)
                    .blur(radius = 24.dp), // Blur requires API 31+
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .clickable(onClick = onClick),
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
}

@ComponentPreview
@Composable
private fun OneNavButtonPreview() {
    CheckFirmTheme {
        Surface {
            OneNavButton(
                icon = OneIcons.Back,
                onClick = {},
            )
        }
    }
}
