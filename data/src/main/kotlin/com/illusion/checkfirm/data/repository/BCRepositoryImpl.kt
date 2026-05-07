package com.illusion.checkfirm.data.repository

import com.illusion.checkfirm.data.local.dao.BCDao
import com.illusion.checkfirm.data.local.entity.asEntity
import com.illusion.checkfirm.data.local.entity.asExternalModel
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Category
import com.illusion.checkfirm.domain.model.Device
import com.illusion.checkfirm.domain.repository.BCRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BCRepositoryImpl @Inject constructor(
    private val bcDao: BCDao
) : BCRepository {

    override fun getAllBookmark(
        order: String,
        isDesc: Boolean
    ): Flow<List<Bookmark>> {
        return bcDao.getAllBookmark(order, isDesc).map {
            it.map { entity -> entity.asExternalModel() }
        }
    }

    override fun getBookmarkByCategory(
        order: String,
        isDesc: Boolean,
        category: String
    ): Flow<List<Bookmark>> {
        return bcDao.getBookmarkByCategory(order, isDesc, category).map {
            it.map { entity -> entity.asExternalModel() }
        }
    }

    override suspend fun addBookmark(bookmark: Bookmark) {
        bcDao.addBookmark(bookmark.asEntity())
    }

    override suspend fun editBookmark(bookmark: Bookmark) {
        bcDao.editBookmark(bookmark.asEntity())
    }

    override suspend fun deleteBookmark(device: Device) {
        bcDao.deleteBookmark(device.toString())
    }

    override suspend fun deleteAllBookmark() {
        bcDao.deleteAllBookmark()
    }

    override fun getAllCategory(): Flow<List<Category>> {
        return bcDao.getAllCategory().map {
            it.map { entity -> entity.asExternalModel() }
        }
    }

    override suspend fun addCategory(category: Category) {
        bcDao.addCategory(category.asEntity())
    }

    override suspend fun editCategory(category: Category) {
        bcDao.editCategory(category.asEntity())
    }

    override suspend fun deleteCategory(name: String) {
        bcDao.deleteCategory(name)
    }

    override suspend fun deleteAllCategory() {
        bcDao.deleteAllCategory()
    }
}
