package com.illusion.checkfirm.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PreferenceRoute(
    onNavigateToAbout: () -> Unit,
    onNavigateToBackupRestore: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToWelcomeSearch: () -> Unit,
    onNavigateToInfoCatcher: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: PreferenceViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PreferenceScreen(
        uiState = uiState,
        onNavigateToAbout = onNavigateToAbout,
        onNavigateToBackupRestore = onNavigateToBackupRestore,
        onNavigateToHelp = onNavigateToHelp,
        onNavigateToWelcomeSearch = onNavigateToWelcomeSearch,
        onNavigateToInfoCatcher = onNavigateToInfoCatcher,
        onNavigateBack = onNavigateBack,
        onActiveDialogChange = viewModel::updateActiveDialog,
        onProfileNameChange = viewModel::updateProfileName,
        onThemeChange = viewModel::updateTheme,
        onLanguageChange = viewModel::updateLanguage,
        onQuickSearchBarChange = viewModel::updateQuickSearchBar,
        onBookmarkOrderChange = viewModel::updateBookmarkOrder,
        onWelcomeSearchChange = viewModel::updateWelcomeSearch,
        onInfoCatcherChange = viewModel::updateInfoCatcher,
        onFirebaseChange = viewModel::updateFirebase,
    )
}
