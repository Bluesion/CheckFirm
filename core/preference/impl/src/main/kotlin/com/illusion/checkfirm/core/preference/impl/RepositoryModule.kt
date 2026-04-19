package com.illusion.checkfirm.core.preference.impl

import com.illusion.checkfirm.core.preference.api.PreferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMyRepository(
        impl: PreferenceRepositoryImpl,
    ): PreferenceRepository
}