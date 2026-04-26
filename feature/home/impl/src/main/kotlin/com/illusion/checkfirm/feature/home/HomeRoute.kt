package com.illusion.checkfirm.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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

    HomeScreen(
        uiState = uiState,
        onSearchIconClick = onSearchIconClick,
        onBookmarkIconClick = onBookmarkIconClick,
        onPreferenceIconClick = onPreferenceIconClick,
        onWelcomeSearchClick = onWelcomeSearchClick,
        onInfoCatcherClick = onInfoCatcherClick,
        onCategoryChipClick = viewModel::updateSelectedCategory,
    )
}
