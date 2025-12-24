package com.illusion.checkfirm.data.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WelcomeSearchDao {
    @Query("SELECT * from welcome_search_device ORDER BY device ASC")
    fun getAll(): Flow<List<WelcomeSearchEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(catcher: WelcomeSearchEntity)

    @Update
    suspend fun update(catcher: WelcomeSearchEntity)

    @Query("DELETE from welcome_search_device")
    suspend fun deleteAll()

    @Query("DELETE FROM welcome_search_device WHERE device = :device")
    suspend fun delete(device: String): Int
}