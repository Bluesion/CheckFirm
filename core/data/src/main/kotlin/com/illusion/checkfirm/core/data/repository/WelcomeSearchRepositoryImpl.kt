package com.illusion.checkfirm.core.data.repository

import com.illusion.checkfirm.core.database.dao.WelcomeSearchDao
import com.illusion.checkfirm.core.database.entity.asExternalModel
import com.illusion.checkfirm.core.database.entity.toWelcomeSearchEntity
import com.illusion.checkfirm.core.domain.model.Device
import com.illusion.checkfirm.core.domain.repository.WelcomeSearchRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WelcomeSearchRepositoryImpl @Inject constructor(
    private val welcomeSearchDao: WelcomeSearchDao
) : WelcomeSearchRepository {

    override val allDevices: Flow<List<Device>> =
        welcomeSearchDao.getAll().map {
            it.map { entity -> entity.asExternalModel() }
        }

    override suspend fun insert(device: Device) {
        welcomeSearchDao.insert(device.toWelcomeSearchEntity())
    }

    override suspend fun delete(device: Device) {
        welcomeSearchDao.delete(device.toString())
    }
}