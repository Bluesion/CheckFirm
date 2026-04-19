package com.illusion.checkfirm.feature.catcher.impl.data.di

import com.illusion.checkfirm.core.navigation.EntryProviderInstaller
import com.illusion.checkfirm.core.navigation.Navigator
import com.illusion.checkfirm.feature.catcher.api.InfoCatcherRouteNavKey
import com.illusion.checkfirm.feature.catcher.impl.presentation.InfoCatcherRoute
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
        entry<InfoCatcherRouteNavKey> {
            InfoCatcherRoute(
                onNavigationIconClick = navigator::goBack,
            )
        }
    }
}
