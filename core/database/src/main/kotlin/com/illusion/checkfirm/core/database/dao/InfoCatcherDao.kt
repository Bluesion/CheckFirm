package com.illusion.checkfirm.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.illusion.checkfirm.core.database.entity.InfoCatcherEntity
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

    @Query("DELETE FROM catcher_device WHERE model = :model AND csc = :csc")
    suspend fun delete(model: String, csc: String): Int
}