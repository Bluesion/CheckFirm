package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun OneRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    selectedColor: Color = Color(0xFF0381FE)
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        colors = RadioButtonDefaults.colors(
            selectedColor = selectedColor
        )
    )
}


