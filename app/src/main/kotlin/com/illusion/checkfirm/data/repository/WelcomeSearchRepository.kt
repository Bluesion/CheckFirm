package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.model.local.WelcomeSearchDao
import com.illusion.checkfirm.data.model.local.WelcomeSearchEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface WelcomeSearchRepository {
    val allDevices: Flow<List<WelcomeSearchEntity>>
    suspend fun insert(entity: WelcomeSearchEntity)
    suspend fun delete(device: String)
}

class WelcomeSearchRepositoryImpl @Inject constructor(
    private val welcomeSearchDao: WelcomeSearchDao
) : WelcomeSearchRepository {

    override val allDevices: Flow<List<WelcomeSearchEntity>> = welcomeSearchDao.getAll()

    override suspend fun insert(entity: WelcomeSearchEntity) {
        welcomeSearchDao.insert(entity)
    }

    override suspend fun delete(device: String) {
        welcomeSearchDao.delete(device)
    }
}