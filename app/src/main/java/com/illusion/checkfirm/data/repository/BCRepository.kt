package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.model.BCDao
import com.illusion.checkfirm.data.model.BookmarkEntity
import com.illusion.checkfirm.data.model.CategoryEntity
import kotlinx.coroutines.flow.Flow

class BCRepository(private val bcDao: BCDao) {

    suspend fun getBookmarkCount(): Int {
        return bcDao.getBookmarkCount()
    }

    fun getAllBookmark(order: String, isDesc: Boolean, category: String = ""): Flow<List<BookmarkEntity>> {
        return bcDao.getAllBookmark(order, isDesc, category)
    }

    suspend fun getAllBookmarkList(order: String, isDesc: Boolean): List<BookmarkEntity> {
        return bcDao.getAllBookmarkList(order, isDesc)
    }

    fun getBookmarkByCategory(order: String, isDesc: Boolean, category: String): Flow<List<BookmarkEntity>> {
        return bcDao.getBookmarkByCategory(order, isDesc, category)
    }

    suspend fun getBookmarkListByCategory(order: String, isDesc: Boolean, category: String): List<BookmarkEntity> {
        return bcDao.getBookmarkListByCategory(order, isDesc, category)
    }

    suspend fun addBookmark(bookmark: BookmarkEntity) {
        bcDao.addBookmark(bookmark)
    }

    suspend fun editBookmark(bookmark: BookmarkEntity) {
        bcDao.editBookmark(bookmark)
    }

    suspend fun deleteBookmark(device: String) {
        bcDao.deleteBookmark(device)
    }

    suspend fun deleteAllBookmark() {
        bcDao.deleteAllBookmark()
    }

    suspend fun getCategoryCount(): Int {
        return bcDao.getCategoryCount()
    }

    fun getAllCategory(): Flow<List<CategoryEntity>> {
        return bcDao.getAllCategory()
    }

    suspend fun getAllCategoryList(): List<CategoryEntity> {
        return bcDao.getAllCategoryList()
    }

    suspend fun addCategory(entity: CategoryEntity) {
        bcDao.addCategory(entity)
    }

    suspend fun editCategory(entity: CategoryEntity) {
        bcDao.editCategory(entity)
    }

    suspend fun deleteCategory(name: String) {
        bcDao.deleteCategory(name)
    }

    suspend fun deleteAllCategory() {
        bcDao.deleteAllCategory()
    }
}
