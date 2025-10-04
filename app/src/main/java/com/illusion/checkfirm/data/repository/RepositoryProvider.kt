package com.illusion.checkfirm.data.repository

import android.content.Context
import com.illusion.checkfirm.data.di.dataStore
import com.illusion.checkfirm.data.source.local.DataStoreManager
import com.illusion.checkfirm.data.source.local.DatabaseProvider
import com.illusion.checkfirm.data.source.remote.VersionFetcher

// Context should be application context
class RepositoryProvider(private val context: Context) {

    private lateinit var bcRepository: BCRepository
    private lateinit var historyRepository: HistoryRepository
    private lateinit var infoCatcherRepository: InfoCatcherRepository
    private lateinit var welcomeSearchRepository: WelcomeSearchRepository
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var mainRepository: MainRepository

    fun initialize() {
        bcRepository = BCRepository(DatabaseProvider.getBCDao())
        historyRepository = HistoryRepository(DatabaseProvider.getHistoryDao())
        infoCatcherRepository = InfoCatcherRepository(DatabaseProvider.getInfoCatcherDao())
        welcomeSearchRepository = WelcomeSearchRepository(DatabaseProvider.getWelcomeSearchDao())
        settingsRepository = SettingsRepository(DataStoreManager(context.dataStore))
        mainRepository = MainRepository(VersionFetcher())
    }

    fun getBCRepository(): BCRepository = bcRepository
    fun getHistoryRepository(): HistoryRepository = historyRepository
    fun getInfoCatcherRepository(): InfoCatcherRepository = infoCatcherRepository
    fun getWelcomeSearchRepository(): WelcomeSearchRepository = welcomeSearchRepository
    fun getSettingsRepository(): SettingsRepository = settingsRepository
    fun getMainRepository(): MainRepository = mainRepository
}