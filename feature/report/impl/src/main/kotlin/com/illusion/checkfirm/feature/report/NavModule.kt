package com.illusion.checkfirm.feature.report

import com.illusion.checkfirm.core.navigation.EntryProviderInstaller
import com.illusion.checkfirm.core.navigation.Navigator
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
        entry<ReportRouteNavKey> {
            ReportRoute(
                onNavigationIconClick = navigator::goBack,
            )
        }
    }
}
