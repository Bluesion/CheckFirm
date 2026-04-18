package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.local.dao.HistoryDao
import com.illusion.checkfirm.data.local.entity.asEntity
import com.illusion.checkfirm.data.local.entity.asExternalModel
import com.illusion.checkfirm.domain.model.SearchHistory
import com.illusion.checkfirm.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override fun getAllHistory(): Flow<List<SearchHistory>> {
        return historyDao.getAllHistory().map { entities ->
            entities.map { it.asExternalModel() }
        }
    }

    override suspend fun getAllHistoryList(): List<SearchHistory> {
        return historyDao.getAllHistoryList().map { it.asExternalModel() }
    }

    override suspend fun cleanUpHistory() {
        historyDao.cleanUpHistory()
    }

    override suspend fun insert(history: SearchHistory) {
        historyDao.insert(history.asEntity())
    }

    override suspend fun update(history: SearchHistory) {
        historyDao.update(history.asEntity())
    }

    override suspend fun delete(history: SearchHistory) {
        historyDao.delete(history.device.model, history.device.csc)
    }

    override suspend fun deleteAll() {
        historyDao.deleteAll()
    }
}
