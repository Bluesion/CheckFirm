package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.model.local.HistoryDao
import com.illusion.checkfirm.data.model.local.HistoryEntity

class HistoryRepository(private val historyDao: HistoryDao) {

    suspend fun getAllHistoryList(): List<HistoryEntity> {
        return historyDao.getAllHistoryList()
    }

    suspend fun cleanUpHistory() {
        historyDao.cleanUpHistory()
    }

    suspend fun insert(entity: HistoryEntity) {
        historyDao.insert(entity)
    }

    suspend fun update(entity: HistoryEntity) {
        historyDao.update(entity)
    }

    suspend fun delete(model: String, csc: String) {
        historyDao.delete(model, csc)
    }

    suspend fun deleteAll() {
        historyDao.deleteAll()
    }
}