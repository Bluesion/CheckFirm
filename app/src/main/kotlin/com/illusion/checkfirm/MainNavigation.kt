package com.illusion.checkfirm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.illusion.checkfirm.domain.model.ApiResponse
import com.illusion.checkfirm.domain.model.AppVersionStatus
import com.illusion.checkfirm.feature.bookmark.impl.presentation.BookmarkListRoute
import com.illusion.checkfirm.feature.catcher.impl.presentation.InfoCatcherRoute
import com.illusion.checkfirm.feature.main.AppMetadataViewModel
import com.illusion.checkfirm.feature.main.MainRoute
import com.illusion.checkfirm.feature.main.OutdatedScreen
import com.illusion.checkfirm.feature.report.ReportRoute
import com.illusion.checkfirm.feature.search.SearchRoute
import com.illusion.checkfirm.feature.settings.PreferenceRoute
import com.illusion.checkfirm.feature.settings.about.AboutRoute
import com.illusion.checkfirm.feature.settings.backuprestore.BackupRestoreRoute
import com.illusion.checkfirm.feature.settings.help.FirmwareManualRoute
import com.illusion.checkfirm.feature.settings.help.HelpRoute
import com.illusion.checkfirm.feature.settings.help.MyDeviceRoute
import com.illusion.checkfirm.feature.sherlock.SherlockRoute
import com.illusion.checkfirm.feature.welcome.WelcomeSearchRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {
    @Serializable
    data object Main : Screen
    @Serializable
    data object Search : Screen
    @Serializable
    data object Bookmark : Screen
    @Serializable
    data object Preference : Screen
    @Serializable
    data object Welcome : Screen
    @Serializable
    data object Catcher : Screen
    @Serializable
    data object Sherlock : Screen
    @Serializable
    data object Report : Screen
    @Serializable
    data object About : Screen
    @Serializable
    data object BackupRestore : Screen
    @Serializable
    data object Help : Screen
    @Serializable
    data object MyDevice : Screen
    @Serializable
    data object FirmwareManual : Screen
    @Serializable
    data object Outdated : Screen
}

@Composable
fun MainNavigation(
    appMetadataViewModel: AppMetadataViewModel = hiltViewModel()
) {
    val backStack = rememberNavBackStack(Screen.Main)
    val isOldVersion by appMetadataViewModel.isOldVersion.collectAsStateWithLifecycle()

    LaunchedEffect(isOldVersion) {
        if (isOldVersion is ApiResponse.Success) {
            val status = (isOldVersion as ApiResponse.Success).data
            if (status == AppVersionStatus.UPDATE_REQUIRED && backStack.lastOrNull() != Screen.Outdated) {
                backStack.add(Screen.Outdated)
            }
        }
    }

    val pop: () -> Unit = { backStack.removeLastOrNull() }

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Screen.Main> {
                MainRoute(
                    onSearchIconClick = { backStack.add(Screen.Search) },
                    onBookmarkIconClick = { backStack.add(Screen.Bookmark) },
                    onPreferenceIconClick = { backStack.add(Screen.Preference) },
                    onWelcomeSearchClick = { backStack.add(Screen.Welcome) },
                    onInfoCatcherClick = { backStack.add(Screen.Catcher) }
                )
            }
            entry<Screen.Search> {
                SearchRoute(
                    onNavigationIconClick = pop,
                    onSherlockClick = { backStack.add(Screen.Sherlock) }
                )
            }
            entry<Screen.Bookmark> {
                BookmarkListRoute(onNavigationIconClick = pop)
            }
            entry<Screen.Preference> {
                PreferenceRoute(
                    onNavigateToAbout = { backStack.add(Screen.About) },
                    onNavigateToBackupRestore = { backStack.add(Screen.BackupRestore) },
                    onNavigateToHelp = { backStack.add(Screen.Help) },
                    onNavigateToWelcomeSearch = { backStack.add(Screen.Welcome) },
                    onNavigateToInfoCatcher = { backStack.add(Screen.Catcher) },
                    onNavigateBack = pop
                )
            }
            entry<Screen.Welcome> {
                WelcomeSearchRoute(onNavigationIconClick = pop)
            }
            entry<Screen.Catcher> {
                InfoCatcherRoute(onNavigationIconClick = pop)
            }
            entry<Screen.Sherlock> {
                SherlockRoute(onNavigationIconClick = pop)
            }
            entry<Screen.Report> {
                ReportRoute(onNavigationIconClick = pop)
            }
            entry<Screen.About> {
                AboutRoute(
                    onNavigateBack = pop,
                    onNavigateToReport = { backStack.add(Screen.Report) }
                )
            }
            entry<Screen.BackupRestore> {
                BackupRestoreRoute(onNavigationIconClick = pop)
            }
            entry<Screen.Help> {
                HelpRoute(
                    onNavigateBack = pop,
                    onNavigateToFirmwareManual = { backStack.add(Screen.FirmwareManual) },
                    onNavigateToMyDevice = { backStack.add(Screen.MyDevice) }
                )
            }
            entry<Screen.MyDevice> {
                MyDeviceRoute(onNavigationIconClick = pop)
            }
            entry<Screen.FirmwareManual> {
                FirmwareManualRoute(onNavigationIconClick = pop)
            }
            entry<Screen.Outdated> {
                OutdatedScreen(onClose = pop)
            }
        }
    )
}
