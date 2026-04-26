package com.illusion.checkfirm.feature.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WelcomeSearchRoute(
    viewModel: WelcomeSearchViewModel = hiltViewModel(),
    onNavigationIconClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WelcomeSearchScreen(
        uiState = uiState,
        onIsWelcomeSearchEnabledChange = viewModel::updateIsWelcomeSearchEnabled,
        onShowDialogChange = viewModel::updateShowDialog,
        onModelChange = viewModel::updateModel,
        onCscChange = viewModel::updateCsc,
        onSelectedChipChange = viewModel::updateSelectedChip,
        onNavigationIconClick = onNavigationIconClick
    )
}