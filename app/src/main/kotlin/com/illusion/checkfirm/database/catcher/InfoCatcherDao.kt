package com.illusion.checkfirm.database.catcher

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface InfoCatcherDao {
    @Query("SELECT * from catcher_device ORDER BY device ASC")
    fun getAll(): LiveData<List<InfoCatcherEntity>>

    @Insert(onConflict = REPLACE)
    fun insert(catcher: InfoCatcherEntity)

    @Update
    fun update(catcher: InfoCatcherEntity)

    @Query("DELETE from catcher_device")
    fun deleteAll()

    @Query("DELETE FROM catcher_device WHERE device = :device")
    fun delete(device: String): Int
}