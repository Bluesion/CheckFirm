package com.illusion.checkfirm.di

import com.illusion.checkfirm.CheckFirmBuildConfigImpl
import com.illusion.checkfirm.core.domain.model.CheckFirmBuildConfig
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindBuildConfig(
        impl: CheckFirmBuildConfigImpl
    ): CheckFirmBuildConfig
}
