package com.illusion.checkfirm.data.di

import com.illusion.checkfirm.data.repository.*
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
    abstract fun bindAppMetadataRepository(
        impl: AppMetadataRepositoryImpl
    ): AppMetadataRepository

    @Binds
    @Singleton
    abstract fun bindBCRepository(
        impl: BCRepositoryImpl
    ): BCRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(
        impl: HistoryRepositoryImpl
    ): HistoryRepository

    @Binds
    @Singleton
    abstract fun bindInfoCatcherRepository(
        impl: InfoCatcherRepositoryImpl
    ): InfoCatcherRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindWelcomeSearchRepository(
        impl: WelcomeSearchRepositoryImpl
    ): WelcomeSearchRepository
}
