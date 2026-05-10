package com.illusion.checkfirm.core.datastore.di

import com.illusion.checkfirm.core.datastore.PreferenceRepositoryImpl
import com.illusion.checkfirm.core.preference.api.PreferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPreferenceRepository(
        impl: PreferenceRepositoryImpl,
    ): PreferenceRepository
}