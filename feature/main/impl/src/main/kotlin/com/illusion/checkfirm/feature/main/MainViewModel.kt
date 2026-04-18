package com.illusion.checkfirm.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Category
import com.illusion.checkfirm.domain.model.Preference
import com.illusion.checkfirm.domain.repository.BCRepository
import com.illusion.checkfirm.domain.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class MainUiState(
    val preference: Preference = Preference(),
    val categories: List<Category> = emptyList(),
    val bookmarks: List<Bookmark> = emptyList(),
    val selectedCategory: String = "All",
)

@HiltViewModel
class MainViewModel @Inject constructor(
    preferenceRepository: PreferenceRepository,
    bcRepository: BCRepository,
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val uiState: StateFlow<MainUiState> = combine(
        preferenceRepository.getSettings(),
        bcRepository.getAllCategory(),
        bcRepository.getAllBookmark("date", true),
        _selectedCategory,
    ) { preference, categories, bookmarks, selected ->
        MainUiState(
            preference = preference,
            categories = categories,
            bookmarks = bookmarks,
            selectedCategory = selected
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainUiState()
    )

    fun updateSelectedCategory(category: String) {
        _selectedCategory.value = category
    }
}
