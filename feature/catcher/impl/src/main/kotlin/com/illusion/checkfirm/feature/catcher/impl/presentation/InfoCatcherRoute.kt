package com.illusion.checkfirm.feature.catcher.impl.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun InfoCatcherRoute(
    onNavigationIconClick: () -> Unit,
    viewModel: InfoCatcherViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    InfoCatcherScreen(
        uiState = uiState,
        onNavigationIconClick = onNavigationIconClick,
        onEnableChange = viewModel::toggleEnabled,
        onAddDeviceClick = viewModel::showDialog,
        onDeleteDevice = viewModel::removeDevice,
        onDialogDismiss = viewModel::dismissDialog,
        onAddDevice = viewModel::addDevice
    )
}
