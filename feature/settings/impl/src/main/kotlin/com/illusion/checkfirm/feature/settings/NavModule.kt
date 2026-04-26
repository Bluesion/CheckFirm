package com.illusion.checkfirm.feature.settings

import com.illusion.checkfirm.core.navigation.EntryProviderInstaller
import com.illusion.checkfirm.core.navigation.Navigator
import com.illusion.checkfirm.feature.settings.about.AboutRoute
import com.illusion.checkfirm.feature.settings.backuprestore.BackupRestoreRoute
import com.illusion.checkfirm.feature.settings.catcher.InfoCatcherRoute
import com.illusion.checkfirm.feature.settings.help.FirmwareManualRoute
import com.illusion.checkfirm.feature.settings.help.HelpRoute
import com.illusion.checkfirm.feature.settings.help.MyDeviceRoute
import com.illusion.checkfirm.feature.settings.welcome.WelcomeSearchRoute
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object NavModule {

    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<SettingsRouteNavKey> {
            PreferenceRoute(
                onNavigateToAbout = { navigator.goTo(AboutRouteNavKey) },
                onNavigateToBackupRestore = { navigator.goTo(BackupRestoreRouteNavKey) },
                onNavigateToHelp = { navigator.goTo(HelpRouteNavKey) },
                onNavigateToWelcomeSearch = { navigator.goTo(WelcomeSearchRouteNavKey) },
                onNavigateToInfoCatcher = { navigator.goTo(InfoCatcherRouteNavKey) },
                onNavigateBack = navigator::goBack,
            )
        }

        entry<AboutRouteNavKey> {
            AboutRoute(
                onNavigateBack = navigator::goBack,
            )
        }

        entry<BackupRestoreRouteNavKey> {
            BackupRestoreRoute(
                onNavigationIconClick = navigator::goBack,
            )
        }

        entry<HelpRouteNavKey> {
            HelpRoute(
                onNavigateBack = navigator::goBack,
                onNavigateToFirmwareManual = { navigator.goTo(FirmwareManualScreenNavKey) },
                onNavigateToMyDevice = { navigator.goTo(MyDeviceScreenNavKey) },
            )
        }

        entry<FirmwareManualScreenNavKey> {
            FirmwareManualRoute(
                onNavigationIconClick = navigator::goBack,
            )
        }

        entry<MyDeviceScreenNavKey> {
            MyDeviceRoute(
                onNavigationIconClick = navigator::goBack,
            )
        }

        entry<WelcomeSearchRouteNavKey> {
            WelcomeSearchRoute(
                onNavigationIconClick = navigator::goBack,
            )
        }

        entry<InfoCatcherRouteNavKey> {
            InfoCatcherRoute(
                onNavigationIconClick = navigator::goBack,
            )
        }
    }
}
