package com.illusion.checkfirm.database.bookmark

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BookmarkRepository
    val allBookmarks: LiveData<List<BookmarkEntity>>

    init {
        val bookmarkDao = BookmarkDatabase.getDatabase(application, viewModelScope).bookmarkDao()
        repository = BookmarkRepository(bookmarkDao)
        allBookmarks = repository.allBookmarks
    }

    fun getCount(): LiveData<Int?>? {
        return repository.getCount()
    }

    fun insert(bookmarkName: String, model: String, csc: String) = viewModelScope.launch {
        InsertThread(bookmarkName, model, csc).start()
    }

    fun update(bookmarkName: String, id: Long, model: String, csc: String) = viewModelScope.launch {
        UpdateThread(bookmarkName, id, model, csc).start()
    }

    fun delete(device: String) = viewModelScope.launch {
        DeleteThread(device).start()
    }

    inner class InsertThread(private val bookmarkName: String, val model: String, val csc: String) : Thread() {
        override fun run() {
            val entity = BookmarkEntity(null, bookmarkName, model, csc, model + csc)
            repository.insert(entity)
        }
    }

    inner class UpdateThread(private val bookmarkName: String, val bookmarkId: Long, val model: String, val csc: String) : Thread() {
        override fun run() {
            val entity = BookmarkEntity(bookmarkId, bookmarkName, model, csc, model + csc)
            repository.update(entity)
        }
    }

    inner class DeleteThread(val device: String) : Thread() {
        override fun run() {
            repository.delete(device)
        }
    }
}