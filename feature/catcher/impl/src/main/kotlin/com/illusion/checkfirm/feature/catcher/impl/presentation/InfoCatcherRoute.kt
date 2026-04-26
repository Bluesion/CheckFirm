package com.illusion.checkfirm.feature.catcher.impl.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun InfoCatcherRoute(
    onNavigationIconClick: () -> Unit,
    viewModel: InfoCatcherViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    InfoCatcherScreen(
        uiState = uiState,
        onNavigationIconClick = onNavigationIconClick,
        onEnableChange = viewModel::toggleEnabled,
        onAddDeviceClick = viewModel::showDialog,
        onDeleteDevice = viewModel::removeDevice,
        onDialogDismiss = viewModel::dismissDialog,
        onDialogModelChange = viewModel::updateDialogModel,
        onDialogCscChange = viewModel::updateDialogCsc,
        onAddDevice = viewModel::addDevice,
    )
}
