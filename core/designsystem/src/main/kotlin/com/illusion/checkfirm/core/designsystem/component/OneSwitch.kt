package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun OneSwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        thumbContent = {
            Box(
                modifier = Modifier.size(20.dp),
            )
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = MaterialTheme.colorScheme.primary,
            checkedBorderColor = MaterialTheme.colorScheme.primary,
            uncheckedThumbColor = Color.White,
            uncheckedTrackColor = MaterialTheme.colorScheme.surface,
            uncheckedBorderColor = MaterialTheme.colorScheme.outline,
        ),
    )
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
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                ),
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
