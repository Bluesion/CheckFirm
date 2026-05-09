package com.illusion.checkfirm.feature.settings.help.mydevice

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MyDeviceRoute(
    onNavigationIconClick: () -> Unit,
    viewModel: MyDeviceViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MyDeviceScreen(uiState = uiState, onNavigationIconClick = onNavigationIconClick)
}