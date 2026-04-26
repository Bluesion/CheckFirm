package com.illusion.checkfirm.feature.settings.help

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FirmwareManualRoute(
    onNavigationIconClick: () -> Unit,
    viewModel: FirmwareManualViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FirmwareManualScreen(uiState = uiState, onNavigationIconClick = onNavigationIconClick)
}