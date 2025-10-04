package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.model.local.WelcomeSearchDao
import com.illusion.checkfirm.data.model.local.WelcomeSearchEntity
import kotlinx.coroutines.flow.Flow

class WelcomeSearchRepository(private val welcomeSearchDao: WelcomeSearchDao) {

    val allDevices: Flow<List<WelcomeSearchEntity>> = welcomeSearchDao.getAll()

    suspend fun insert(entity: WelcomeSearchEntity) {
        welcomeSearchDao.insert(entity)
    }

    suspend fun delete(device: String) {
        welcomeSearchDao.delete(device)
    }
}