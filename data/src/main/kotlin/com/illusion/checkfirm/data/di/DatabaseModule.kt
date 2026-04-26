package com.illusion.checkfirm.data.di

import android.content.Context
import com.illusion.checkfirm.data.local.dao.BCDao
import com.illusion.checkfirm.data.local.dao.HistoryDao
import com.illusion.checkfirm.data.local.dao.InfoCatcherDao
import com.illusion.checkfirm.data.local.dao.WelcomeSearchDao
import com.illusion.checkfirm.data.local.database.BCDatabase
import com.illusion.checkfirm.data.local.database.HistoryDatabase
import com.illusion.checkfirm.data.local.database.InfoCatcherDatabase
import com.illusion.checkfirm.data.local.database.WelcomeSearchDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHistoryDatabase(@ApplicationContext context: Context): HistoryDatabase {
        return HistoryDatabase.getDatabase(context)
    }

    @Provides
    fun provideHistoryDao(database: HistoryDatabase): HistoryDao {
        return database.historyDao()
    }

    @Provides
    @Singleton
    fun provideWelcomeSearchDatabase(@ApplicationContext context: Context): WelcomeSearchDatabase {
        return WelcomeSearchDatabase.getDatabase(context)
    }

    @Provides
    fun provideWelcomeSearchDao(database: WelcomeSearchDatabase): WelcomeSearchDao {
        return database.welcomeSearchDao()
    }

    @Provides
    @Singleton
    fun provideInfoCatcherDatabase(@ApplicationContext context: Context): InfoCatcherDatabase {
        return InfoCatcherDatabase.getDatabase(context)
    }

    @Provides
    fun provideInfoCatcherDao(database: InfoCatcherDatabase): InfoCatcherDao {
        return database.infoCatcherDao()
    }

    @Provides
    @Singleton
    fun provideBCDatabase(@ApplicationContext context: Context): BCDatabase {
        return BCDatabase.getDatabase(context)
    }

    @Provides
    fun provideBCDao(database: BCDatabase): BCDao {
        return database.bcDao()
    }
}
