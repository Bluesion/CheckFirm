package com.illusion.checkfirm.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.illusion.checkfirm.core.database.entity.WelcomeSearchEntity
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

    @Query("DELETE FROM welcome_search_device WHERE model = :model AND csc = :csc")
    suspend fun delete(model: String, csc: String): Int
}