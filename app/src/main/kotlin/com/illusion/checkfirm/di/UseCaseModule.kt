package com.illusion.checkfirm.di

import com.illusion.checkfirm.FetchLatestAppVersionUseCaseImpl
import com.illusion.checkfirm.feature.home.api.FetchLatestAppVersionUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindFetchLatestAppVersionUseCase(
        impl: FetchLatestAppVersionUseCaseImpl
    ): FetchLatestAppVersionUseCase
}
