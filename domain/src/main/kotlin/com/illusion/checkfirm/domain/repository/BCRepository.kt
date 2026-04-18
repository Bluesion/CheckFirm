package com.illusion.checkfirm.domain.repository

import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface BCRepository {
    fun getAllBookmark(
        order: String, isDesc: Boolean
    ): Flow<List<Bookmark>>

    fun getBookmarkByCategory(
        order: String, isDesc: Boolean, category: String
    ): Flow<List<Bookmark>>

    suspend fun addBookmark(bookmark: Bookmark)
    suspend fun editBookmark(bookmark: Bookmark)
    suspend fun deleteBookmark(device: String)
    suspend fun deleteAllBookmark()
    fun getAllCategory(): Flow<List<Category>>
    suspend fun addCategory(category: Category)
    suspend fun editCategory(category: Category)
    suspend fun deleteCategory(name: String)
    suspend fun deleteAllCategory()
}
