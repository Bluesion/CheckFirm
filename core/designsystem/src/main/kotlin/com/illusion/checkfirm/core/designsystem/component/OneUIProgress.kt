package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OneUIProgress(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF387AFF),
    strokeWidth: Float = 4f
) {
    CircularProgressIndicator(
        modifier = modifier.padding(4.dp),
        color = color,
        strokeWidth = strokeWidth.dp
    )
}


