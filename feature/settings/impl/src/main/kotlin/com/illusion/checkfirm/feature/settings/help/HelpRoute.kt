package com.illusion.checkfirm.feature.settings.help

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HelpRoute(
    onNavigateBack: () -> Unit,
    onNavigateToFirmwareManual: () -> Unit,
    onNavigateToMyDevice: () -> Unit,
    viewModel: HelpViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HelpScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNavigateToFirmwareManual = onNavigateToFirmwareManual,
        onNavigateToMyDevice = onNavigateToMyDevice,
    )
}