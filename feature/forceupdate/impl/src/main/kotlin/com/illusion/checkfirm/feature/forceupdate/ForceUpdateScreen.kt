package com.illusion.checkfirm.feature.forceupdate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun ForceUpdateScreen(
    onUpdateButtonClick: () -> Unit,
    onCloseButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "T 업데이트")
        Button(
            onClick = onUpdateButtonClick,
        ) {
            Text(text = "T 업데이트")
        }
        Button(
            onClick = onCloseButtonClick,
        ) {
            Text(text = "T 앱 종료")
        }
    }
}

@ScreenPreview
@Composable
private fun ForceUpdateScreenPreview() {
    CheckFirmTheme {
        Surface {
            ForceUpdateScreen(
                onUpdateButtonClick = {},
                onCloseButtonClick = {},
            )
        }
    }
}
