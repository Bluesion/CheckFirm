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
    val preference by viewModel.preference.collectAsStateWithLifecycle()

    PreferenceScreen(
        preference = preference,
        onNavigateToAbout = onNavigateToAbout,
        onNavigateToBackupRestore = onNavigateToBackupRestore,
        onNavigateToHelp = onNavigateToHelp,
        onNavigateToWelcomeSearch = onNavigateToWelcomeSearch,
        onNavigateToInfoCatcher = onNavigateToInfoCatcher,
        onNavigateBack = onNavigateBack,
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
