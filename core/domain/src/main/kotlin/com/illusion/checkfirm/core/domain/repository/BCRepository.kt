package com.illusion.checkfirm.core.domain.repository

import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.Category
import com.illusion.checkfirm.core.domain.model.Device
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
    suspend fun deleteBookmark(device: Device)
    suspend fun deleteAllBookmark()
    fun getAllCategory(): Flow<List<Category>>
    suspend fun addCategory(category: Category)
    suspend fun editCategory(category: Category)
    suspend fun deleteCategory(name: String)
    suspend fun deleteAllCategory()
}
