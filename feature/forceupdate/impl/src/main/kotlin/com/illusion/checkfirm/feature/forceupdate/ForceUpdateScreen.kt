package com.illusion.checkfirm.feature.forceupdate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun ForceUpdateScreen(
    onUpdateButtonClick: () -> Unit,
    onCloseButtonClick: () -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "\uD83D\uDCF1",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.outdated_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.outdated_description),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = onUpdateButtonClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.about_update))
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = onCloseButtonClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.close))
            }
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
