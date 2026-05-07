package com.illusion.checkfirm.feature.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
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
        onThemeChange = { theme ->
            viewModel.updateTheme(theme)
            AppCompatDelegate.setDefaultNightMode(theme.toNightMode())
        },
        onLanguageChange = { tag ->
            viewModel.updateLanguage(tag)
            AppCompatDelegate.setApplicationLocales(
                if (tag.isBlank()) LocaleListCompat.getEmptyLocaleList()
                else LocaleListCompat.forLanguageTags(tag),
            )
        },
        onQuickSearchBarChange = viewModel::updateQuickSearchBar,
        onBookmarkOrderChange = viewModel::updateBookmarkOrder,
        onWelcomeSearchChange = viewModel::updateWelcomeSearch,
        onInfoCatcherChange = viewModel::updateInfoCatcher,
        onFirebaseChange = viewModel::updateFirebase,
        onResetBookmarks = viewModel::resetBookmarks,
    )
}

private fun String.toNightMode(): Int = when (this) {
    "light" -> AppCompatDelegate.MODE_NIGHT_NO
    "dark" -> AppCompatDelegate.MODE_NIGHT_YES
    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
}
