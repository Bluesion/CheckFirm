package com.illusion.checkfirm.core.data.repository

import com.illusion.checkfirm.core.database.dao.InfoCatcherDao
import com.illusion.checkfirm.core.database.entity.asExternalModel
import com.illusion.checkfirm.core.database.entity.toInfoCatcherEntity
import com.illusion.checkfirm.core.domain.model.Device
import com.illusion.checkfirm.core.domain.repository.InfoCatcherRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InfoCatcherRepositoryImpl @Inject constructor(
    private val catcherDao: InfoCatcherDao
) : InfoCatcherRepository {

    override val allDevices: Flow<List<Device>> =
        catcherDao.getAll().map {
            it.map { entity -> entity.asExternalModel() }
        }

    override suspend fun insert(device: Device) {
        catcherDao.insert(device.toInfoCatcherEntity())
    }

    override suspend fun delete(device: Device) {
        catcherDao.delete(device.toString())
    }
}