package com.illusion.checkfirm.data.source.local

import android.content.Context

object DatabaseProvider {

    private lateinit var bcDatabase: BCDatabase
    private lateinit var historyDatabase: HistoryDatabase
    private lateinit var infoCatcherDatabase: InfoCatcherDatabase
    private lateinit var welcomeSearchDatabase: WelcomeSearchDatabase

    fun initialize(context: Context) {
        bcDatabase = BCDatabase.getDatabase(context)
        historyDatabase = HistoryDatabase.getDatabase(context)
        infoCatcherDatabase = InfoCatcherDatabase.getDatabase(context)
        welcomeSearchDatabase = WelcomeSearchDatabase.getDatabase(context)
    }

    fun getBCDao() = bcDatabase.bcDao()
    fun getHistoryDao() = historyDatabase.historyDao()
    fun getInfoCatcherDao() = infoCatcherDatabase.infoCatcherDao()
    fun getWelcomeSearchDao() = welcomeSearchDatabase.welcomeSearchDao()
}