package com.illusion.checkfirm.feature.sherlock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illusion.checkfirm.domain.model.SearchResult

@Composable
fun SherlockRoute(
    payload: SearchResult?,
    onNavigationIconClick: () -> Unit,
    viewModel: SherlockViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(payload) { viewModel.initialize(payload) }

    SherlockScreen(
        uiState = uiState,
        onNavigationIconClick = onNavigationIconClick,
        onTabChange = viewModel::selectTab,
        onBuildPrefixChange = viewModel::setBuildPrefix,
        onCscPrefixChange = viewModel::setCscPrefix,
        onBasebandPrefixChange = viewModel::setBasebandPrefix,
        onManualBuildChange = viewModel::setManualBuild,
        onManualCscChange = viewModel::setManualCsc,
        onManualBasebandChange = viewModel::setManualBaseband,
        onScriptStartChange = viewModel::setScriptStart,
        onScriptEndChange = viewModel::setScriptEnd,
        onStartScript = viewModel::runScript,
        onShowInfo = { viewModel.showInfoDialog(true) },
        onDismissInfo = { viewModel.showInfoDialog(false) },
    )
}
