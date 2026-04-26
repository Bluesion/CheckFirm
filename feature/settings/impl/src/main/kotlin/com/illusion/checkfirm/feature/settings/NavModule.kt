package com.illusion.checkfirm.feature.settings

import com.illusion.checkfirm.core.navigation.EntryProviderInstaller
import com.illusion.checkfirm.core.navigation.Navigator
import com.illusion.checkfirm.feature.settings.about.AboutRoute
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
    }
}
