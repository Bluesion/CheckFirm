package com.illusion.checkfirm.features.bookmark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.data.model.local.BookmarkEntity
import com.illusion.checkfirm.data.model.local.CategoryEntity
import com.illusion.checkfirm.data.repository.BCRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CategoryViewModel @Inject constructor(
    @Named("allString") private val allString: String,
    private val bcRepository: BCRepository
) : ViewModel() {

    private val _currentCategoryList = MutableStateFlow<List<String>>(emptyList())
    val currentCategoryList: StateFlow<List<String>> = _currentCategoryList.asStateFlow()

    init {
        viewModelScope.launch {
            bcRepository.getAllCategory().collect {
                val tempCategoryList = mutableListOf<String>()
                tempCategoryList.add(allString)
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