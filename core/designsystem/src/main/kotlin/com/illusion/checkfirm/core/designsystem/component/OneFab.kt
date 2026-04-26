package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun OneFab(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    containerColor: Color? = null,
    contentColor: Color = Color(0xFF0381FE)
) {
    val darkTheme = isSystemInDarkTheme()
    val defaultContainerColor = if (darkTheme) Color(0xFF272727) else Color(0xFFFCFCFC)

    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = containerColor ?: defaultContainerColor,
        contentColor = contentColor,
        shape = CircleShape
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}


