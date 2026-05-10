package com.illusion.checkfirm.core.data.di

import com.illusion.checkfirm.core.data.repository.AppMetadataRepositoryImpl
import com.illusion.checkfirm.core.data.repository.BCRepositoryImpl
import com.illusion.checkfirm.core.data.repository.HistoryRepositoryImpl
import com.illusion.checkfirm.core.data.repository.InfoCatcherRepositoryImpl
import com.illusion.checkfirm.core.data.repository.SherlockRepositoryImpl
import com.illusion.checkfirm.core.data.repository.WelcomeSearchRepositoryImpl
import com.illusion.checkfirm.core.domain.remote.AppMetadataFetcher
import com.illusion.checkfirm.core.domain.remote.FirmwareFetcher
import com.illusion.checkfirm.core.domain.remote.SherlockDataSource
import com.illusion.checkfirm.core.domain.repository.AppMetadataRepository
import com.illusion.checkfirm.core.domain.repository.BCRepository
import com.illusion.checkfirm.core.domain.repository.HistoryRepository
import com.illusion.checkfirm.core.domain.repository.InfoCatcherRepository
import com.illusion.checkfirm.core.domain.repository.SherlockRepository
import com.illusion.checkfirm.core.domain.repository.WelcomeSearchRepository
import com.illusion.checkfirm.core.network.AppMetadataFetcherImpl
import com.illusion.checkfirm.core.network.FirmwareFetcherImpl
import com.illusion.checkfirm.core.network.SherlockDataSourceImpl
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

    @Binds
    @Singleton
    abstract fun bindFirmwareFetcher(
        impl: FirmwareFetcherImpl
    ): FirmwareFetcher

    @Binds
    @Singleton
    abstract fun bindSherlockDataSource(
        impl: SherlockDataSourceImpl
    ): SherlockDataSource
}
