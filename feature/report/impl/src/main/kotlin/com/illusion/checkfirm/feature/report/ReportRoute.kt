package com.illusion.checkfirm.feature.report

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ReportRoute(
    onNavigationIconClick: () -> Unit,
    viewModel: ReportViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

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
        onUserMessageUpdate = viewModel::updateUserMessage,
        onSubmitClick = viewModel::submitReport,
        onNavigationIconClick = onNavigationIconClick,
    )
}
