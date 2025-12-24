package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.model.local.InfoCatcherDao
import com.illusion.checkfirm.data.model.local.InfoCatcherEntity
import kotlinx.coroutines.flow.Flow

class InfoCatcherRepository(private val catcherDao: InfoCatcherDao) {

    val allDevices: Flow<List<InfoCatcherEntity>> = catcherDao.getAll()

    suspend fun insert(entity: InfoCatcherEntity) {
        catcherDao.insert(entity)
    }

    suspend fun delete(device: String) {
        catcherDao.delete(device)
    }
}