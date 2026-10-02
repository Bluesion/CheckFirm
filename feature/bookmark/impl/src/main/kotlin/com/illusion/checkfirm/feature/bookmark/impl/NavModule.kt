package com.illusion.checkfirm.feature.bookmark.impl

import com.illusion.checkfirm.core.navigation.EntryProviderInstaller
import com.illusion.checkfirm.core.navigation.Navigator
import com.illusion.checkfirm.feature.bookmark.api.BookmarkRouteNavKey
import com.illusion.checkfirm.feature.category.api.CategoryRouteNavKey
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
        entry<BookmarkRouteNavKey> {
            BookmarkRoute(
                onNavigationIconClick = navigator::goBack,
                onCategoryClick = { navigator.goTo(com.illusion.checkfirm.feature.category.api.CategoryEditRouteNavKey()) },
                onEditCategory = { navigator.goTo(com.illusion.checkfirm.feature.category.api.CategoryEditRouteNavKey(it)) },
            )
        }
    }
}
