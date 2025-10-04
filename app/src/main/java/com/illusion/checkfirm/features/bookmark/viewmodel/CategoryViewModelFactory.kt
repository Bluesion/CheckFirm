package com.illusion.checkfirm.features.bookmark.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.illusion.checkfirm.data.repository.BCRepository

class CategoryViewModelFactory(
    private val allString: String,
    private val bcRepository: BCRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(allString, bcRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}