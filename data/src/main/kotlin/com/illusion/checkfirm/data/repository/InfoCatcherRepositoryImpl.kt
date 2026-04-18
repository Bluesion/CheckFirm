package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.local.dao.InfoCatcherDao
import com.illusion.checkfirm.data.local.entity.asExternalModel
import com.illusion.checkfirm.data.local.entity.toInfoCatcherEntity
import com.illusion.checkfirm.domain.model.Device
import com.illusion.checkfirm.domain.repository.InfoCatcherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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