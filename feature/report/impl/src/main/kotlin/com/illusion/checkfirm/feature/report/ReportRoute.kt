package com.illusion.checkfirm.feature.report

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun ReportRoute(
    onNavigationIconClick: () -> Unit,
    viewModel: ReportViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val labels = mapOf(
        "type_1" to stringResource(R.string.report_type_1),
        "type_2" to stringResource(R.string.report_type_2),
        "type_3" to stringResource(R.string.report_type_3),
        "type_4" to stringResource(R.string.report_type_4),
    )
    val successMsg = stringResource(R.string.report_success)
    val failMsg = stringResource(R.string.report_fail)

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            val text = when (event) {
                ReportEvent.SubmitSuccess -> successMsg
                ReportEvent.SubmitError -> failMsg
            }
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    ReportScreen(
        uiState = uiState,
        onBugTypeToggle = viewModel::toggleBugType,
        onLogsChange = viewModel::updateLogs,
        onSubmitClick = { viewModel.submitReport(labels) },
        onNavigationIconClick = onNavigationIconClick,
    )
}
