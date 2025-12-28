package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.model.local.BCDao
import com.illusion.checkfirm.data.model.local.BookmarkEntity
import com.illusion.checkfirm.data.model.local.CategoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface BCRepository {
    suspend fun getBookmarkCount(): Int
    fun getAllBookmark(
        order: String,
        isDesc: Boolean,
        category: String = ""
    ): Flow<List<BookmarkEntity>>
    suspend fun getAllBookmarkList(
        order: String,
        isDesc: Boolean
    ): List<BookmarkEntity>
    fun getBookmarkByCategory(
        order: String,
        isDesc: Boolean,
        category: String
    ): Flow<List<BookmarkEntity>>
    suspend fun getBookmarkListByCategory(
        order: String,
        isDesc: Boolean,
        category: String
    ): List<BookmarkEntity>
    suspend fun addBookmark(bookmark: BookmarkEntity)
    suspend fun editBookmark(bookmark: BookmarkEntity)
    suspend fun deleteBookmark(device: String)
    suspend fun deleteAllBookmark()
    suspend fun getCategoryCount(): Int
    fun getAllCategory(): Flow<List<CategoryEntity>>
    suspend fun getAllCategoryList(): List<CategoryEntity>
    suspend fun addCategory(entity: CategoryEntity)
    suspend fun editCategory(entity: CategoryEntity)
    suspend fun deleteCategory(name: String)
    suspend fun deleteAllCategory()
}

class BCRepositoryImpl @Inject constructor(
    private val bcDao: BCDao
) : BCRepository {

    override suspend fun getBookmarkCount(): Int {
        return bcDao.getBookmarkCount()
    }

    override fun getAllBookmark(
        order: String,
        isDesc: Boolean,
        category: String
    ): Flow<List<BookmarkEntity>> {
        return bcDao.getAllBookmark(order, isDesc, category)
    }

    override suspend fun getAllBookmarkList(order: String, isDesc: Boolean): List<BookmarkEntity> {
        return bcDao.getAllBookmarkList(order, isDesc)
    }

    override fun getBookmarkByCategory(
        order: String,
        isDesc: Boolean,
        category: String
    ): Flow<List<BookmarkEntity>> {
        return bcDao.getBookmarkByCategory(order, isDesc, category)
    }

    override suspend fun getBookmarkListByCategory(
        order: String,
        isDesc: Boolean,
        category: String
    ): List<BookmarkEntity> {
        return bcDao.getBookmarkListByCategory(order, isDesc, category)
    }

    override suspend fun addBookmark(bookmark: BookmarkEntity) {
        bcDao.addBookmark(bookmark)
    }

    override suspend fun editBookmark(bookmark: BookmarkEntity) {
        bcDao.editBookmark(bookmark)
    }

    override suspend fun deleteBookmark(device: String) {
        bcDao.deleteBookmark(device)
    }

    override suspend fun deleteAllBookmark() {
        bcDao.deleteAllBookmark()
    }

    override suspend fun getCategoryCount(): Int {
        return bcDao.getCategoryCount()
    }

    override fun getAllCategory(): Flow<List<CategoryEntity>> {
        return bcDao.getAllCategory()
    }

    override suspend fun getAllCategoryList(): List<CategoryEntity> {
        return bcDao.getAllCategoryList()
    }

    override suspend fun addCategory(entity: CategoryEntity) {
        bcDao.addCategory(entity)
    }

    override suspend fun editCategory(entity: CategoryEntity) {
        bcDao.editCategory(entity)
    }

    override suspend fun deleteCategory(name: String) {
        bcDao.deleteCategory(name)
    }

    override suspend fun deleteAllCategory() {
        bcDao.deleteAllCategory()
    }
}
