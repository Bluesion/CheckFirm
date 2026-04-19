package com.illusion.checkfirm.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illusion.checkfirm.domain.model.ApiResponse
import com.illusion.checkfirm.domain.model.AppVersionStatus

@Composable
fun HomeRoute(
    onSearchIconClick: () -> Unit,
    onBookmarkIconClick: () -> Unit,
    onPreferenceIconClick: () -> Unit,
    onWelcomeSearchClick: () -> Unit,
    onInfoCatcherClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isOldVersion by appMetadataViewModel.isOldVersion.collectAsStateWithLifecycle()

    LaunchedEffect(isOldVersion) {
        if (isOldVersion is ApiResponse.Success) {
            val status = (isOldVersion as ApiResponse.Success).data
            if (status == AppVersionStatus.UPDATE_REQUIRED && backStack.lastOrNull() != Screen.Outdated) {
                backStack.add(Screen.Outdated)
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        onSearchIconClick = onSearchIconClick,
        onBookmarkIconClick = onBookmarkIconClick,
        onPreferenceIconClick = onPreferenceIconClick,
        onWelcomeSearchClick = onWelcomeSearchClick,
        onInfoCatcherClick = onInfoCatcherClick,
        onCategoryChipClick = viewModel::updateSelectedCategory
    )
}
