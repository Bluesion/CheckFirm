package com.illusion.checkfirm.features.bookmark.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.data.model.BookmarkEntity
import com.illusion.checkfirm.data.repository.BCRepository
import com.illusion.checkfirm.data.source.local.BCDatabase
import com.illusion.checkfirm.data.source.local.SettingsDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BookmarkViewModel(application: Application) : AndroidViewModel(application) {

    private val bcRepository: BCRepository
    private val appSettingsRepository: SettingsDataSource

    private var bookmarkOrder = "time"
    private var isBookmarkOrderDesc = false

    private val _bookmarkList = MutableStateFlow<List<BookmarkEntity>>(emptyList())
    val bookmarkList: StateFlow<List<BookmarkEntity>> = _bookmarkList.asStateFlow()

    private val _selectedCategory = MutableStateFlow("")

    init {
        val bcDao = BCDatabase.getDatabase(application).bcDao()
        bcRepository = BCRepository(bcDao)
        appSettingsRepository = SettingsDataSource(application)

        viewModelScope.launch(Dispatchers.IO) {
            bookmarkOrder = appSettingsRepository.getBookmarkOrder.first()
            isBookmarkOrderDesc = !appSettingsRepository.isBookmarkAscOrder.first()
            getAllBookmarkByCategory()
        }
    }

    suspend fun getAllBookmarkList(): List<BookmarkEntity> {
        return bcRepository.getAllBookmarkList(bookmarkOrder, isBookmarkOrderDesc)
    }

    /**
     * Fetches all bookmarks based on the selected category.
     * If the category is empty, it fetches all bookmarks without filtering.
     */
    fun getAllBookmarkByCategory(category: String = "") = viewModelScope.launch(Dispatchers.IO) {
        if (category.isBlank()) {
            _selectedCategory.value = ""
        } else {
            _selectedCategory.value = category
        }

        _bookmarkList.value =
            bcRepository.getAllBookmark(bookmarkOrder, isBookmarkOrderDesc, _selectedCategory.value).first()
    }

    /**
     * Updates the bookmark list based on the current order and selected category.
     */
    private fun updateBookmarkList() = viewModelScope.launch(Dispatchers.IO) {
        _bookmarkList.value = bcRepository.getAllBookmark(
            bookmarkOrder, isBookmarkOrderDesc, _selectedCategory.value).first()
    }

    fun updateCategory(category: String = "") {
        getAllBookmarkByCategory(category)
    }

    suspend fun getBookmarkCount(): Int {
        return bcRepository.getBookmarkCount()
    }

    fun addOrEditBookmark(
        bookmarkEntity: BookmarkEntity
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (bookmarkEntity.id == null) {
            bcRepository.addBookmark(bookmarkEntity)
        } else {
            bcRepository.editBookmark(bookmarkEntity)
        }
        updateBookmarkList()
    }

    fun addBookmark(bookmarkName: String, model: String, csc: String, category: String) =
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            val optimizedName = bookmarkName.trim()
            val optimizedModel = model.trim().uppercase()
            val optimizedCsc = csc.trim().uppercase()
            val optimizedCategory = category.trim()

            bcRepository.addBookmark(
                BookmarkEntity(
                    null,
                    optimizedName,
                    optimizedModel,
                    optimizedCsc,
                    optimizedModel + optimizedCsc,
                    optimizedCategory,
                    0
                )
            )

            updateBookmarkList()
        }

    fun editBookmark(
        id: Long, bookmarkName: String, model: String, csc: String, category: String, position: Int
    ) = viewModelScope.launch(
        Dispatchers.IO
    ) {
        val optimizedName = bookmarkName.trim()
        val optimizedModel = model.trim().uppercase()
        val optimizedCsc = csc.trim().uppercase()
        val optimizedCategory = category.trim()

        bcRepository.editBookmark(
            BookmarkEntity(
                id,
                optimizedName,
                optimizedModel,
                optimizedCsc,
                optimizedModel + optimizedCsc,
                optimizedCategory,
                position
            )
        )

        updateBookmarkList()
    }

    fun deleteBookmark(device: String) = viewModelScope.launch(Dispatchers.IO) {
        bcRepository.deleteBookmark(device)
        updateBookmarkList()
    }

    fun reset() = viewModelScope.launch(Dispatchers.IO) {
        bcRepository.deleteAllBookmark()
        _bookmarkList.value = emptyList()
    }
}