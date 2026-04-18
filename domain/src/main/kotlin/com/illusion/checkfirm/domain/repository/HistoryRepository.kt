package com.illusion.checkfirm.domain.repository

import com.illusion.checkfirm.domain.model.SearchHistory
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getAllHistory(): Flow<List<SearchHistory>>
    suspend fun getAllHistoryList(): List<SearchHistory>
    suspend fun cleanUpHistory()
    suspend fun insert(history: SearchHistory)
    suspend fun update(history: SearchHistory)
    suspend fun delete(history: SearchHistory)
    suspend fun deleteAll()
}
