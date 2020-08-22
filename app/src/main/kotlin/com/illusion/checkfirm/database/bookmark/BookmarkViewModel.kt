package com.illusion.checkfirm.database.bookmark

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BookmarkRepository
    val allCategory: LiveData<List<String>>

    init {
        val bookmarkDao = BookmarkDatabase.getDatabase(application).bookmarkDao()
        repository = BookmarkRepository(bookmarkDao)
        allCategory = repository.allCategory
    }

    fun getCount(): LiveData<Int?>? {
        return repository.getCount()
    }

    fun getBookmarks(order: String, isDesc: Boolean): LiveData<List<BookmarkEntity>> {
        return repository.getBookmarks(order, isDesc)
    }

    fun insert(bookmarkName: String, model: String, csc: String, category: String) = viewModelScope.launch {
        InsertThread(bookmarkName, model, csc, category).start()
    }

    fun update(bookmarkName: String, id: Long, model: String, csc: String, category: String) = viewModelScope.launch {
        UpdateThread(bookmarkName, id, model, csc, category).start()
    }

    fun delete(device: String) = viewModelScope.launch {
        DeleteThread(device).start()
    }

    inner class InsertThread(private val bookmarkName: String, val model: String, val csc: String, private val category: String) : Thread() {
        override fun run() {
            val entity = BookmarkEntity(null, bookmarkName, model, csc, model + csc, category)
            repository.insert(entity)
        }
    }

    inner class UpdateThread(private val bookmarkName: String, private val bookmarkId: Long,
                             val model: String, val csc: String, private val category: String) : Thread() {
        override fun run() {
            val entity = BookmarkEntity(bookmarkId, bookmarkName, model, csc, model + csc, category)
            repository.update(entity)
        }
    }

    inner class DeleteThread(val device: String) : Thread() {
        override fun run() {
            repository.delete(device)
        }
    }
}
