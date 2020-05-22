package com.illusion.checkfirm.database.bookmark

import androidx.lifecycle.LiveData

class BookmarkRepository(private val bookmarkDao: BookmarkDao) {

    val allBookmarks: LiveData<List<BookmarkEntity>> = bookmarkDao.getAll()
    val allCategory: LiveData<List<String>> = bookmarkDao.getCategory()

    fun getCount(): LiveData<Int?>? {
        return bookmarkDao.getCount()
    }

    fun insert(entity: BookmarkEntity) {
        bookmarkDao.insert(entity)
    }

    fun update(entity: BookmarkEntity) {
        bookmarkDao.update(entity)
    }

    fun delete(device: String) {
        bookmarkDao.delete(device)
    }
}