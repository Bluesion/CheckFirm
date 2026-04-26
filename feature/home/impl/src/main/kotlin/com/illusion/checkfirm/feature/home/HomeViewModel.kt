package com.illusion.checkfirm.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.core.preference.api.PreferenceRepository
import com.illusion.checkfirm.domain.repository.BCRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    preferenceRepository: PreferenceRepository,
    bcRepository: BCRepository,
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    val uiState: StateFlow<HomeUiState> = combine(
        preferenceRepository.getSettings(),
        bcRepository.getAllCategory(),
        bcRepository.getAllBookmark("date", true),
        _selectedCategory,
    ) { preference, categories, bookmarks, selected ->
        HomeUiState(
            preference = preference,
            categories = categories,
            bookmarks = bookmarks,
            selectedCategory = selected
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    fun updateSelectedCategory(category: String) {
        _selectedCategory.value = category
    }
}
