package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun OneRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
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

@ComponentPreview
@Composable
private fun OneRadioButtonPreview() {
    CheckFirmTheme {
        Surface {
            var isChecked by remember { mutableStateOf(false) }
            OneRadioButton(
                selected = isChecked,
                onClick = { isChecked = !isChecked },
            )
        }
    }
}

