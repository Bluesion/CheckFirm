package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun OneFab(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    containerColor: Color? = null,
    contentColor: Color = MaterialTheme.colorScheme.primary,
) {
    val darkTheme = isSystemInDarkTheme()
    val defaultContainerColor = if (darkTheme) Color(0xFF272727) else Color(0xFFFCFCFC)

    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = containerColor ?: defaultContainerColor,
        contentColor = contentColor,
        shape = CircleShape,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
        )
    }
}

@ComponentPreview
@Composable
private fun OneFabPreview() {
    CheckFirmTheme {
        OneFab(
            onClick = {},
            icon = Icons.Rounded.Add,
            contentDescription = "Add",
        )
    }
}

