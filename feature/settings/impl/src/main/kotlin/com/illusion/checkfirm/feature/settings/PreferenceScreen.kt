@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.preference.api.Preference
import com.illusion.checkfirm.feature.settings.bookmark.BookmarkOrderDialog
import com.illusion.checkfirm.feature.settings.bookmark.BookmarkResetDialog
import com.illusion.checkfirm.feature.settings.language.LanguageDialog
import com.illusion.checkfirm.feature.settings.profile.ProfileDialog
import com.illusion.checkfirm.feature.settings.theme.ThemeDialog

@Composable
fun PreferenceScreen(
    preference: Preference,
    onNavigateToAbout: () -> Unit,
    onNavigateToBackupRestore: () -> Unit,
    onNavigateToHelp: () -> Unit,
    onNavigateToWelcomeSearch: () -> Unit,
    onNavigateToInfoCatcher: () -> Unit,
    onNavigateBack: () -> Unit,
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
    var activeDialog by remember { mutableStateOf(PreferenceDialog.None) }

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
                onClick = { activeDialog = PreferenceDialog.Profile },
            )
            AppearanceCard(
                onThemeClick = { activeDialog = PreferenceDialog.Theme },
                onLanguageClick = { activeDialog = PreferenceDialog.Language },
                isQuickSearchBarEnabled = preference.isQuickSearchBarEnabled,
                onQuickSearchBarClick = { onQuickSearchBarChange(!preference.isQuickSearchBarEnabled) },
                onQuickSearchBarChanged = onQuickSearchBarChange,
            )
            BookmarkCard(
                onBookmarkOrderClick = { activeDialog = PreferenceDialog.BookmarkOrder },
                onBookmarkResetClick = { activeDialog = PreferenceDialog.BookmarkReset },
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

    when (activeDialog) {
        PreferenceDialog.Profile -> ProfileDialog(
            initialName = preference.profileName,
            onDismiss = { activeDialog = PreferenceDialog.None },
            onConfirm = {
                onProfileNameChange(it)
                activeDialog = PreferenceDialog.None
            },
        )

        PreferenceDialog.Theme -> ThemeDialog(
            selectedTheme = preference.theme,
            onDismiss = { activeDialog = PreferenceDialog.None },
            onConfirm = {
                onThemeChange(it)
                activeDialog = PreferenceDialog.None
            },
        )

        PreferenceDialog.Language -> LanguageDialog(
            selectedLanguage = preference.language,
            onDismiss = { activeDialog = PreferenceDialog.None },
            onConfirm = {
                onLanguageChange(it)
                activeDialog = PreferenceDialog.None
            },
        )

        PreferenceDialog.BookmarkOrder -> BookmarkOrderDialog(
            selectedOrder = preference.bookmarkOrder,
            isAscending = preference.isBookmarkAscOrder,
            onDismiss = { activeDialog = PreferenceDialog.None },
            onConfirm = { order, ascending ->
                onBookmarkOrderChange(order, ascending)
                activeDialog = PreferenceDialog.None
            },
        )

        PreferenceDialog.BookmarkReset -> BookmarkResetDialog(
            onDismiss = { activeDialog = PreferenceDialog.None },
            onConfirm = { activeDialog = PreferenceDialog.None },
        )

        PreferenceDialog.None -> Unit
    }
}
