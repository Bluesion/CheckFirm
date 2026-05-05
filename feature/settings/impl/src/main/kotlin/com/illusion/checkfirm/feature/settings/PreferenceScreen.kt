package com.illusion.checkfirm.feature.settings

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.feature.settings.bookmark.BookmarkOrderDialog
import com.illusion.checkfirm.feature.settings.bookmark.BookmarkResetDialog
import com.illusion.checkfirm.feature.settings.language.LanguageDialog
import com.illusion.checkfirm.feature.settings.profile.ProfileDialog
import com.illusion.checkfirm.feature.settings.theme.ThemeDialog

@Composable
fun PreferenceScreen(
    uiState: PreferenceUiState,
    onNavigateToAbout: () -> Unit,
    onNavigateToBackupRestore: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToWelcomeSearch: () -> Unit,
    onNavigateToInfoCatcher: () -> Unit,
    onNavigateBack: () -> Unit,
    onActiveDialogChange: (PreferenceDialog) -> Unit,
    onProfileNameChange: (String) -> Unit,
    onThemeChange: (String) -> Unit,
    onLanguageChange: (String) -> Unit,
    onQuickSearchBarChange: (Boolean) -> Unit,
    onBookmarkOrderChange: (String, Boolean) -> Unit,
    onWelcomeSearchChange: (Boolean) -> Unit,
    onInfoCatcherChange: (Boolean) -> Unit,
    onFirebaseChange: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val preference = uiState.preference

    OneScaffold(
        title = stringResource(R.string.settings),
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = OneIcons.IcBack,
                    contentDescription = null,
                    tint = CheckFirmTheme.colors.toolbarIconTint,
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = innerPadding.calculateBottomPadding() + 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ProfileCard(
                profileName = preference.profileName,
                onClick = { onActiveDialogChange(PreferenceDialog.Profile) },
            )
            AppearanceCard(
                onThemeClick = { onActiveDialogChange(PreferenceDialog.Theme) },
                onLanguageClick = { onActiveDialogChange(PreferenceDialog.Language) },
                isQuickSearchBarEnabled = preference.isQuickSearchBarEnabled,
                onQuickSearchBarClick = { onQuickSearchBarChange(!preference.isQuickSearchBarEnabled) },
                onQuickSearchBarChanged = onQuickSearchBarChange,
            )
            BookmarkCard(
                onBookmarkOrderClick = { onActiveDialogChange(PreferenceDialog.BookmarkOrder) },
                onBookmarkResetClick = { onActiveDialogChange(PreferenceDialog.BookmarkReset) },
                onBackupRestoreClick = onNavigateToBackupRestore,
            )
            SearchCard(
                isWelcomeSearchEnabled = preference.isWelcomeSearchEnabled,
                onWelcomeSearchClick = onNavigateToWelcomeSearch,
                onWelcomeSearchChanged = onWelcomeSearchChange,
                isInfoCatcherEnabled = preference.isInfoCatcherEnabled,
                onInfoCatcherClick = onNavigateToInfoCatcher,
                onInfoCatcherChanged = onInfoCatcherChange,
                isFirebaseEnabled = preference.isFirebaseEnabled,
                onFirebaseClick = { onFirebaseChange(!preference.isFirebaseEnabled) },
                onFirebaseChanged = onFirebaseChange,
            )
            AboutCard(
                onHelpClick = onNavigateToHelp,
                onAboutClick = onNavigateToAbout,
                onInquiryClick = {
                    runCatching {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:illusionis.dev@gmail.com".toUri()
                            putExtra(Intent.EXTRA_SUBJECT, "CheckFirm Inquiry")
                        }
                        context.startActivity(intent)
                    }
                },
            )
        }
    }

    when (uiState.activeDialog) {
        PreferenceDialog.Profile -> ProfileDialog(
            initialName = preference.profileName,
            onDismiss = { onActiveDialogChange(PreferenceDialog.None) },
            onConfirm = {
                onProfileNameChange(it)
                onActiveDialogChange(PreferenceDialog.None)
            },
        )

        PreferenceDialog.Theme -> ThemeDialog(
            selectedTheme = preference.theme,
            onDismiss = { onActiveDialogChange(PreferenceDialog.None) },
            onConfirm = {
                onThemeChange(it)
                onActiveDialogChange(PreferenceDialog.None)
            },
        )

        PreferenceDialog.Language -> LanguageDialog(
            selectedLanguage = preference.language,
            onDismiss = { onActiveDialogChange(PreferenceDialog.None) },
            onConfirm = {
                onLanguageChange(it)
                onActiveDialogChange(PreferenceDialog.None)
            },
        )

        PreferenceDialog.BookmarkOrder -> BookmarkOrderDialog(
            selectedOrder = preference.bookmarkOrder,
            isAscending = preference.isBookmarkAscOrder,
            onDismiss = { onActiveDialogChange(PreferenceDialog.None) },
            onConfirm = { order, ascending ->
                onBookmarkOrderChange(order, ascending)
                onActiveDialogChange(PreferenceDialog.None)
            },
        )

        PreferenceDialog.BookmarkReset -> BookmarkResetDialog(
            onDismiss = { onActiveDialogChange(PreferenceDialog.None) },
            onConfirm = { onActiveDialogChange(PreferenceDialog.None) },
        )

        PreferenceDialog.None -> Unit
    }
}

@ScreenPreview
@Composable
private fun PreferenceScreenPreview() {
    CheckFirmTheme {
        Surface {
            PreferenceScreen(
                uiState = PreferenceUiState(),
                onNavigateToAbout = {},
                onNavigateToBackupRestore = {},
                onNavigateToHelp = {},
                onNavigateToWelcomeSearch = {},
                onNavigateToInfoCatcher = {},
                onNavigateBack = {},
                onActiveDialogChange = {},
                onProfileNameChange = {},
                onThemeChange = {},
                onLanguageChange = {},
                onQuickSearchBarChange = {},
                onBookmarkOrderChange = { _, _ -> },
                onWelcomeSearchChange = {},
                onInfoCatcherChange = {},
                onFirebaseChange = {},
            )
        }
    }
}
