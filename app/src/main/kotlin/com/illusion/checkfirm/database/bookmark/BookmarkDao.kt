package com.illusion.checkfirm.database.bookmark

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmark_info ORDER BY CASE WHEN :isDesc = 0 THEN (CASE :order WHEN 'time' THEN id WHEN 'device' THEN device WHEN 'name' THEN name ELSE id END) END ASC, CASE WHEN :isDesc = 1 THEN (CASE :order WHEN 'time' THEN id WHEN 'device' THEN device WHEN 'name' THEN name ELSE id END) END DESC")
    fun getBookmarks(order: String, isDesc: Boolean): LiveData<List<BookmarkEntity>>

    @Query("SELECT COUNT(*) FROM bookmark_info")
    fun getCount(): LiveData<Int?>?

    @Query("SELECT * FROM bookmark_info WHERE category=:category")
    fun getByCategory(category: String): LiveData<List<BookmarkEntity>>

    @Query("SELECT category FROM bookmark_info")
    fun getCategory(): LiveData<List<String>>

    @Insert(onConflict = REPLACE)
    fun insert(catcher: BookmarkEntity)

    @Update(onConflict = REPLACE)
    fun update(catcher: BookmarkEntity)

    @Query("DELETE FROM bookmark_info")
    fun deleteAll()

    @Query("DELETE FROM bookmark_info WHERE device=:device")
    fun delete(device: String): Int
}
