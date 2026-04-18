package com.illusion.checkfirm.core.preference.impl.data.di

import com.illusion.checkfirm.domain.repository.PreferenceRepository
import com.illusion.checkfirm.core.preference.impl.data.repository.PreferenceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferenceRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPreferenceRepository(
        impl: PreferenceRepositoryImpl,
    ): PreferenceRepository
}
