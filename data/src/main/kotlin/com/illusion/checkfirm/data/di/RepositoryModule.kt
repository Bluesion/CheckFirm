package com.illusion.checkfirm.data.di

import com.illusion.checkfirm.data.repository.*
import com.illusion.checkfirm.domain.repository.AppMetadataRepository
import com.illusion.checkfirm.domain.repository.BCRepository
import com.illusion.checkfirm.domain.repository.HistoryRepository
import com.illusion.checkfirm.domain.repository.InfoCatcherRepository
import com.illusion.checkfirm.domain.repository.PreferenceRepository
import com.illusion.checkfirm.domain.repository.SherlockRepository
import com.illusion.checkfirm.domain.repository.WelcomeSearchRepository
import com.illusion.checkfirm.domain.remote.AppMetadataFetcher
import com.illusion.checkfirm.data.remote.AppMetadataFetcherImpl
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
    abstract fun bindWelcomeSearchRepository(
        impl: WelcomeSearchRepositoryImpl
    ): WelcomeSearchRepository

    @Binds
    @Singleton
    abstract fun bindSherlockRepository(
        impl: SherlockRepositoryImpl
    ): SherlockRepository

    @Binds
    @Singleton
    abstract fun bindAppMetadataFetcher(
        impl: AppMetadataFetcherImpl
    ): AppMetadataFetcher
}
