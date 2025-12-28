package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.model.local.HistoryDao
import com.illusion.checkfirm.data.model.local.HistoryEntity
import javax.inject.Inject

interface HistoryRepository {
    suspend fun getAllHistoryList(): List<HistoryEntity>
    suspend fun cleanUpHistory()
    suspend fun insert(entity: HistoryEntity)
    suspend fun update(entity: HistoryEntity)
    suspend fun delete(model: String, csc: String)
    suspend fun deleteAll()
}

class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override suspend fun getAllHistoryList(): List<HistoryEntity> {
        return historyDao.getAllHistoryList()
    }

    override suspend fun cleanUpHistory() {
        historyDao.cleanUpHistory()
    }

    override suspend fun insert(entity: HistoryEntity) {
        historyDao.insert(entity)
    }

    override suspend fun update(entity: HistoryEntity) {
        historyDao.update(entity)
    }

    override suspend fun delete(model: String, csc: String) {
        historyDao.delete(model, csc)
    }

    override suspend fun deleteAll() {
        historyDao.deleteAll()
    }
}