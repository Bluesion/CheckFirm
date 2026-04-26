@file:OptIn(ExperimentalMaterial3Api::class)

package com.illusion.checkfirm.feature.settings.backuprestore

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun BackupRestoreScreen(
    uiState: BackupRestoreUiState = BackupRestoreUiState(),
    onBackupClick: () -> Unit = {},
    onRestoreClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    OneScaffold(
        title = stringResource(R.string.settings_bookmark_backup_restore),
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = OneIcons.IcBack,
                    contentDescription = null,
                    tint = CheckFirmTheme.colors.toolbarIconTint,
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = innerPadding.calculateBottomPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.settings_bookmark_backup_restore_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(36.dp))

            TextButton(onClick = onBackupClick) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.ic_btn_up),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                    )
                    Text(
                        text = stringResource(R.string.backup),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(220.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.img_device_line),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Spacer(Modifier.height(32.dp))

            TextButton(onClick = onRestoreClick) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.ic_btn_down),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                    )
                    Text(
                        text = stringResource(R.string.restore),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }
    }
}

@Composable
fun BackupRestoreRoute(
    viewModel: BackupRestoreViewModel = hiltViewModel(),
    onNavigationIconClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LocalContext.current

    val backupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            result.data?.data?.let { uri -> viewModel.backup(uri) }
        }
    }

    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            result.data?.data?.let { uri -> viewModel.restore(uri) }
        }
    }

    BackupRestoreScreen(
        uiState = uiState,
        onBackupClick = {
            val date = SimpleDateFormat(
                "yyyyMMdd",
                Locale.getDefault()
            ).format(Calendar.getInstance().time)
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/json"
                putExtra(Intent.EXTRA_TITLE, "CheckFirm_backup_$date.json")
            }
            backupLauncher.launch(intent)
        },
        onRestoreClick = {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/json"
            }
            restoreLauncher.launch(intent)
        },
        onNavigateBack = onNavigationIconClick,
    )
}
