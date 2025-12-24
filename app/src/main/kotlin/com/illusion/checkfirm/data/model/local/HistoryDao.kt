package com.illusion.checkfirm.data.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history_info ORDER BY id DESC")
    suspend fun getAllHistoryList(): List<HistoryEntity>

    @Query("DELETE FROM history_info where id NOT IN (SELECT id from history_info ORDER BY id DESC LIMIT 10)")
    suspend fun cleanUpHistory()

    @Insert(onConflict = REPLACE)
    suspend fun insert(data: HistoryEntity)

    @Update(onConflict = REPLACE)
    suspend fun update(data: HistoryEntity)

    @Query("DELETE FROM history_info")
    suspend fun deleteAll()

    @Query("DELETE FROM history_info WHERE model=:model AND csc=:csc")
    suspend fun delete(model: String, csc: String): Int
}