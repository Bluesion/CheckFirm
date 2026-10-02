package com.illusion.checkfirm.feature.settings.backuprestore

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneNavButton
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.feature.settings.R as FeatureR

@Composable
fun BackupRestoreScreen(
    uiState: BackupRestoreUiState,
    onBackupClick: () -> Unit = {},
    onRestoreClick: () -> Unit = {},
    onNavigateBack: () -> Unit,
) {
    OneScaffold(
        expandable = false,
        navigationIcon = {
            OneNavButton(
                onClick = onNavigateBack,
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    imageVector = OneIcons.Back,
                    contentDescription = stringResource(com.illusion.checkfirm.core.designsystem.R.string.navigate_back),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp),
                )
            }
        },
    ) { innerPadding ->
        BoxWithConstraints(Modifier.fillMaxSize().padding(bottom = innerPadding.calculateBottomPadding())) {
            val imageHeight = maxHeight * 0.5f
            Column(
                modifier = Modifier.fillMaxWidth().height(imageHeight).padding(horizontal = 16.dp).padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                Text(
                    text = stringResource(FeatureR.string.settings_bookmark_backup_restore_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(36.dp))
                TransferButton(FeatureR.string.backup, R.drawable.ic_btn_up, !uiState.isWorking, onBackupClick)
            }
            Image(
                painter = painterResource(R.drawable.img_device_line),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(0.65f).height(imageHeight),
            )
            Box(Modifier.align(Alignment.TopCenter).offset(y = imageHeight + 32.dp)) {
                TransferButton(FeatureR.string.restore, R.drawable.ic_btn_down, !uiState.isWorking, onRestoreClick)
            }
        }
    }
}

@Composable
private fun TransferButton(label: Int, icon: Int, enabled: Boolean, onClick: () -> Unit) {
    TextButton(onClick = onClick, enabled = enabled) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(painterResource(icon), null, Modifier.size(48.dp))
            Text(stringResource(label), color = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@ScreenPreview
@Composable
private fun BackupRestoreScreenPreview() {
    CheckFirmTheme {
        Surface {
            BackupRestoreScreen(
                uiState = BackupRestoreUiState(),
                onNavigateBack = {},
            )
        }
    }
}
