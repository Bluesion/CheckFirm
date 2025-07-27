package com.illusion.checkfirm.features.bookmark.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.R
import com.illusion.checkfirm.data.model.BookmarkEntity
import com.illusion.checkfirm.data.model.CategoryEntity
import com.illusion.checkfirm.data.repository.BCRepository
import com.illusion.checkfirm.data.source.local.BCDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val bcRepository: BCRepository

    private val _currentCategoryList = MutableStateFlow<List<String>>(emptyList())
    val currentCategoryList: StateFlow<List<String>> = _currentCategoryList.asStateFlow()

    init {
        val bcDao = BCDatabase.getDatabase(application).bcDao()
        bcRepository = BCRepository(bcDao)

        viewModelScope.launch {
            bcRepository.getAllCategory().collect {
                val tempCategoryList = mutableListOf<String>()
                tempCategoryList.add(application.applicationContext.getString(R.string.category_all))
                tempCategoryList.addAll(it.map { category -> category.name })
                _currentCategoryList.value = tempCategoryList
            }
        }
    }

    suspend fun getCategoryCount(): Int {
        return bcRepository.getCategoryCount()
    }

    fun getAllCategory(): Flow<List<CategoryEntity>> {
        return bcRepository.getAllCategory()
    }

    suspend fun getAllCategoryList(): List<CategoryEntity> {
        return bcRepository.getAllCategoryList()
    }

    fun addCategory(categoryName: String) = viewModelScope.launch(
        Dispatchers.IO
    ) {
        bcRepository.addCategory(
            CategoryEntity(
                null,
                categoryName.trim(),
                0
            )
        )
    }

    fun editCategory(id: Long, categoryName: String, position: Int) = viewModelScope.launch(
        Dispatchers.IO
    ) {
        bcRepository.editCategory(
            CategoryEntity(
                id,
                categoryName.trim(),
                position
            )
        )
    }

    fun deleteCategory(name: String) = viewModelScope.launch(Dispatchers.IO) {
        bcRepository.getBookmarkListByCategory("time", true, name).forEach {
            bcRepository.editBookmark(
                BookmarkEntity(
                    it.id!!,
                    it.name,
                    it.model,
                    it.csc,
                    it.device,
                    "",
                    it.position
                )
            )
        }
        bcRepository.deleteCategory(name)
    }

    fun reset() = viewModelScope.launch(Dispatchers.IO) {
        bcRepository.deleteAllCategory()
    }
}