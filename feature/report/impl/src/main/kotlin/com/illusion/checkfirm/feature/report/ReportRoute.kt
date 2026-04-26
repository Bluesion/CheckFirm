package com.illusion.checkfirm.feature.report

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun ReportRoute(
    onNavigationIconClick: () -> Unit,
    viewModel: ReportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is ReportEvent.SubmitSuccess -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is ReportEvent.SubmitError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ReportScreen(
        uiState = uiState,
        onBugTypeChange = viewModel::updateBugType,
        onDeviceDetailsChange = viewModel::updateDeviceDetails,
        onLogsChange = viewModel::updateLogs,
        onConsentChange = viewModel::updateConsent,
        onSubmitClick = viewModel::submitReport,
        onNavigationIconClick = onNavigationIconClick
    )
}
