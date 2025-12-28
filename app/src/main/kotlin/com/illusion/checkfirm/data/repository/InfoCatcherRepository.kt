package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.model.local.InfoCatcherDao
import com.illusion.checkfirm.data.model.local.InfoCatcherEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface InfoCatcherRepository {
    val allDevices: Flow<List<InfoCatcherEntity>>
    suspend fun insert(entity: InfoCatcherEntity)
    suspend fun delete(device: String)
}

class InfoCatcherRepositoryImpl @Inject constructor(
    private val catcherDao: InfoCatcherDao
) : InfoCatcherRepository {

    override val allDevices: Flow<List<InfoCatcherEntity>> = catcherDao.getAll()

    override suspend fun insert(entity: InfoCatcherEntity) {
        catcherDao.insert(entity)
    }

    override suspend fun delete(device: String) {
        catcherDao.delete(device)
    }
}