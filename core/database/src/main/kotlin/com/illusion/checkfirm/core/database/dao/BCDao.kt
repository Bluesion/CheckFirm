package com.illusion.checkfirm.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.illusion.checkfirm.core.database.entity.BookmarkEntity
import com.illusion.checkfirm.core.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BCDao {
    @Query("SELECT * FROM bookmark_info ORDER BY CASE WHEN :isDesc = 0 THEN (CASE :order WHEN 'time' THEN id WHEN 'device' THEN device WHEN 'name' THEN name ELSE id END) END ASC, CASE WHEN :isDesc = 1 THEN (CASE :order WHEN 'time' THEN id WHEN 'device' THEN device WHEN 'name' THEN name ELSE id END) END DESC")
    fun getAllBookmark(
        order: String,
        isDesc: Boolean
    ): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmark_info WHERE category=:category ORDER BY CASE WHEN :isDesc = 0 THEN (CASE :order WHEN 'time' THEN id WHEN 'device' THEN device WHEN 'name' THEN name ELSE id END) END ASC, CASE WHEN :isDesc = 1 THEN (CASE :order WHEN 'time' THEN id WHEN 'device' THEN device WHEN 'name' THEN name ELSE id END) END DESC")
    fun getBookmarkByCategory(
        order: String,
        isDesc: Boolean,
        category: String
    ): Flow<List<BookmarkEntity>>

    @Insert(onConflict = REPLACE)
    suspend fun addBookmark(bookmark: BookmarkEntity)

    @Update(onConflict = REPLACE)
    suspend fun editBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmark_info WHERE device=:device")
    suspend fun deleteBookmark(device: String): Int

    @Query("DELETE FROM bookmark_info")
    suspend fun deleteAllBookmark()

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
