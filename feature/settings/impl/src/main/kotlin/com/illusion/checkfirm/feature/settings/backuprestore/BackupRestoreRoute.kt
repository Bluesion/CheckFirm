package com.illusion.checkfirm.feature.settings.backuprestore

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun BackupRestoreRoute(
    onNavigationIconClick: () -> Unit,
    viewModel: BackupRestoreViewModel = hiltViewModel(),
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
