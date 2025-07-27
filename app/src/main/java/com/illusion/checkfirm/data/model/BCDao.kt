package com.illusion.checkfirm.data.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BCDao {

    @Query("SELECT * FROM bookmark_info WHERE CASE WHEN :category = '' THEN (1 = 1) ELSE (category=:category) END ORDER BY CASE WHEN :isDesc = 0 THEN (CASE :order WHEN 'time' THEN id WHEN 'device' THEN device WHEN 'name' THEN name ELSE id END) END ASC, CASE WHEN :isDesc = 1 THEN (CASE :order WHEN 'time' THEN id WHEN 'device' THEN device WHEN 'name' THEN name ELSE id END) END DESC")
    fun getAllBookmark(order: String, isDesc: Boolean, category: String = ""): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmark_info ORDER BY CASE WHEN :isDesc = 0 THEN (CASE :order WHEN 'time' THEN id WHEN 'device' THEN device WHEN 'name' THEN name ELSE id END) END ASC, CASE WHEN :isDesc = 1 THEN (CASE :order WHEN 'time' THEN id WHEN 'device' THEN device WHEN 'name' THEN name ELSE id END) END DESC")
    suspend fun getAllBookmarkList(order: String, isDesc: Boolean): List<BookmarkEntity>

    @Query("SELECT * FROM bookmark_info WHERE CASE WHEN :category = '' THEN (1 = 1) ELSE (category=:category) END ORDER BY CASE WHEN :isDesc = 0 THEN (CASE :order WHEN 'time' THEN id WHEN 'device' THEN device WHEN 'name' THEN name ELSE id END) END ASC, CASE WHEN :isDesc = 1 THEN (CASE :order WHEN 'time' THEN id WHEN 'device' THEN device WHEN 'name' THEN name ELSE id END) END DESC")
    fun getBookmarkByCategory(order: String, isDesc: Boolean, category: String): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmark_info WHERE CASE WHEN :category = '' THEN (1 = 1) ELSE (category=:category) END ORDER BY CASE WHEN :isDesc = 0 THEN (CASE :order WHEN 'time' THEN id WHEN 'device' THEN device WHEN 'name' THEN name ELSE id END) END ASC, CASE WHEN :isDesc = 1 THEN (CASE :order WHEN 'time' THEN id WHEN 'device' THEN device WHEN 'name' THEN name ELSE id END) END DESC")
    suspend fun getBookmarkListByCategory(order: String, isDesc: Boolean, category: String): List<BookmarkEntity>

    @Query("SELECT COUNT(*) FROM bookmark_info")
    suspend fun getBookmarkCount(): Int

    @Insert(onConflict = REPLACE)
    suspend fun addBookmark(bookmark: BookmarkEntity)

    @Update(onConflict = REPLACE)
    suspend fun editBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmark_info WHERE device=:device")
    suspend fun deleteBookmark(device: String): Int

    @Query("DELETE FROM bookmark_info")
    suspend fun deleteAllBookmark()

    @Query("SELECT COUNT(*) FROM category_info")
    suspend fun getCategoryCount(): Int

    @Query("SELECT * FROM category_info ORDER BY position ASC, id DESC")
    fun getAllCategory(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM category_info ORDER BY position ASC, id DESC")
    suspend fun getAllCategoryList(): List<CategoryEntity>

    @Insert(onConflict = REPLACE)
    suspend fun addCategory(category: CategoryEntity)

    @Update(onConflict = REPLACE)
    suspend fun editCategory(category: CategoryEntity)

    @Query("DELETE FROM category_info WHERE name=:name")
    suspend fun deleteCategory(name: String): Int

    @Query("DELETE FROM category_info")
    suspend fun deleteAllCategory()
}