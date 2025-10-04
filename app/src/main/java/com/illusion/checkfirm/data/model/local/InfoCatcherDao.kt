package com.illusion.checkfirm.data.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface InfoCatcherDao {
    @Query("SELECT * from catcher_device ORDER BY device ASC")
    fun getAll(): Flow<List<InfoCatcherEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(catcher: InfoCatcherEntity)

    @Update
    suspend fun update(catcher: InfoCatcherEntity)

    @Query("DELETE from catcher_device")
    suspend fun deleteAll()

    @Query("DELETE FROM catcher_device WHERE device = :device")
    suspend fun delete(device: String): Int
}