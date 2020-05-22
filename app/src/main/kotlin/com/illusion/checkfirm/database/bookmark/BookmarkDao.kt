package com.illusion.checkfirm.database.bookmark

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmark_info ORDER BY device ASC")
    fun getAll(): LiveData<List<BookmarkEntity>>

    @Query("SELECT COUNT(*) FROM bookmark_info")
    fun getCount(): LiveData<Int?>?

    @Query("SELECT category FROM bookmark_info")
    fun getCategory(): LiveData<List<String>>

    @Insert(onConflict = REPLACE)
    fun insert(catcher: BookmarkEntity)

    @Update(onConflict = REPLACE)
    fun update(catcher: BookmarkEntity)

    @Query("DELETE FROM bookmark_info")
    fun deleteAll()

    @Query("DELETE FROM bookmark_info WHERE device = :device")
    fun delete(device: String): Int
}