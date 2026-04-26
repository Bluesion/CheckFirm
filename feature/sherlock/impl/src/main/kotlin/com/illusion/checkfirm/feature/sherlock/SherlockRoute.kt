package com.illusion.checkfirm.feature.sherlock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SherlockRoute(
    onNavigationIconClick: () -> Unit,
    viewModel: SherlockViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SherlockScreen(
        uiState = uiState,
        onNavigationIconClick = onNavigationIconClick,
        onTabChange = viewModel::selectTab,
        onPdaChange = viewModel::updatePda,
        onCscChange = viewModel::updateCsc,
        onBasebandChange = viewModel::updateBaseband,
        onScriptStartChange = viewModel::updateScriptStart,
        onScriptEndChange = viewModel::updateScriptEnd,
        onStartScript = { viewModel.setResult("Script completed") },
        onDismissResult = viewModel::clearResult,
    )
}