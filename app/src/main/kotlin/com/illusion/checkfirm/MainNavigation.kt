package com.illusion.checkfirm

import com.illusion.checkfirm.core.navigation.EntryProviderInstaller
import com.illusion.checkfirm.core.navigation.Navigator
import com.illusion.checkfirm.feature.bookmark.api.BookmarkRouteNavKey
import com.illusion.checkfirm.feature.home.HomeRoute
import com.illusion.checkfirm.feature.home.api.HomeRouteNavKey
import com.illusion.checkfirm.feature.search.SearchRouteNavKey
import com.illusion.checkfirm.feature.settings.InfoCatcherRouteNavKey
import com.illusion.checkfirm.feature.settings.SettingsRouteNavKey
import com.illusion.checkfirm.feature.settings.WelcomeSearchRouteNavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
object MainNavModule {

    @Provides
    @ActivityRetainedScoped
    fun provideNavigator(): Navigator = Navigator(startNavKey = HomeRouteNavKey)

    @IntoSet
    @Provides
    fun provideEntryProviderInstaller(navigator: Navigator): EntryProviderInstaller = {
        entry<HomeRouteNavKey> {
            HomeRoute(
                onSearchIconClick = { navigator.goTo(SearchRouteNavKey) },
                onBookmarkIconClick = { navigator.goTo(BookmarkRouteNavKey) },
                onPreferenceIconClick = { navigator.goTo(SettingsRouteNavKey) },
                onWelcomeSearchClick = { navigator.goTo(WelcomeSearchRouteNavKey) },
                onInfoCatcherClick = { navigator.goTo(InfoCatcherRouteNavKey) },
            )
        }
    }
}
