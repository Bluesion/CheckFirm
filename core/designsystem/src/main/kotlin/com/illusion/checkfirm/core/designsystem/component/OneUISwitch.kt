package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OneUISwitch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier
    )
}

@Composable
fun OneUISwitchCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textOn: String = "On",
    textOff: String = "Off"
) {
    val darkTheme = isSystemInDarkTheme()
    val cardBackgroundOff = if (darkTheme) Color(0xFF171717) else Color(0xFFFCFCFC)
    val cardBackgroundOn = if (darkTheme) Color(0xFF272727) else Color(0xFFE9E9E9)

    val textColorDark = Color(0xFFFAFAFA)
    val textColorLight = Color(0xFF252525)

    val backgroundColor = if (checked) cardBackgroundOn else cardBackgroundOff
    val textColor = if (darkTheme) textColorDark else textColorLight

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(
                role = Role.Switch,
                onClick = { onCheckedChange(!checked) }
            ),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (checked) textOn else textOff,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                ),
                color = textColor
            )
            Spacer(modifier = Modifier.weight(1f))
            OneUISwitch(
                checked = checked,
                onCheckedChange = null // Card handles click
            )
        }
    }
}


