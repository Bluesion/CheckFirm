package com.illusion.checkfirm.feature.settings.backuprestore

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illusion.checkfirm.feature.settings.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun BackupRestoreRoute(
    onNavigationIconClick: () -> Unit,
    viewModel: BackupRestoreViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val backupSuccess = stringResource(R.string.backup_toast)
    val backupFailed = stringResource(R.string.backup_failed)
    val restoreSuccess = stringResource(R.string.restore_toast)
    val restoreFailed = stringResource(R.string.restore_failed)

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            val text = when (event) {
                BackupRestoreEvent.BackupSuccess -> backupSuccess
                BackupRestoreEvent.BackupFail -> backupFailed
                BackupRestoreEvent.RestoreSuccess -> restoreSuccess
                BackupRestoreEvent.RestoreFail -> restoreFailed
            }
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            if (event is BackupRestoreEvent.BackupSuccess || event is BackupRestoreEvent.RestoreSuccess) {
                onNavigationIconClick()
            }
        }
    }

    val backupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri -> viewModel.backup(uri) }
        }
    }

    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri -> viewModel.restore(uri) }
        }
    }

    BackupRestoreScreen(
        uiState = uiState,
        onBackupClick = {
            val date = SimpleDateFormat("yyyyMMdd", Locale.KOREAN)
                .format(Calendar.getInstance().time)
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
