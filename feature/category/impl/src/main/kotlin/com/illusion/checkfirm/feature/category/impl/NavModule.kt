package com.illusion.checkfirm.feature.category.impl

import com.illusion.checkfirm.core.navigation.EntryProviderInstaller
import com.illusion.checkfirm.core.navigation.Navigator
import com.illusion.checkfirm.feature.category.api.CategoryRouteNavKey
import com.illusion.checkfirm.feature.category.api.CategoryEditRouteNavKey
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
        entry<CategoryRouteNavKey> {
            CategoryListRoute(
                onNavigationIconClick = navigator::goBack,
                onEditCategory = { name ->
                    navigator.goTo(CategoryEditRouteNavKey(categoryName = name))
                },
                onNewCategory = { navigator.goTo(CategoryEditRouteNavKey(categoryName = null)) },
            )
        }
        entry<CategoryEditRouteNavKey> { key ->
            CategoryRoute(
                initialName = key.categoryName,
                onNavigationIconClick = navigator::goBack,
            )
        }
    }
}