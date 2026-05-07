package com.illusion.checkfirm.feature.bookmark.impl.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Category
import com.illusion.checkfirm.domain.repository.BCRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CategoryEditUiState(
    val initialName: String? = null,
    val name: String = "",
    val nameError: NameError? = null,
    /** All bookmarks shown as a checklist. */
    val bookmarks: List<Bookmark> = emptyList(),
    /** Subset of [bookmarks] currently checked into this category. */
    val selected: Set<DeviceKey> = emptySet(),
)

enum class NameError { Blank, Reserved }

/** Stable key derived from device.toString() — matches DB device-column shape. */
@JvmInline
value class DeviceKey(val key: String)

private fun Bookmark.deviceKey() = DeviceKey(device.toString())

@HiltViewModel
class CategoryEditViewModel @Inject constructor(
    private val repository: BCRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryEditUiState())
    val uiState: StateFlow<CategoryEditUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<CategoryEditEvent>()
    val events: SharedFlow<CategoryEditEvent> = _events.asSharedFlow()

    private var initialized = false

    fun initialize(initialName: String?) {
        if (initialized) return
        initialized = true
        viewModelScope.launch {
            val bookmarks = repository.getAllBookmark("date", true).first()
            val preselected = if (initialName != null) {
                bookmarks.filter { it.category == initialName }.map { it.deviceKey() }.toSet()
            } else emptySet()
            _uiState.update {
                it.copy(
                    initialName = initialName,
                    name = initialName.orEmpty(),
                    bookmarks = bookmarks,
                    selected = preselected,
                )
            }
        }
    }

    fun updateName(value: String) {
        _uiState.update { it.copy(name = value, nameError = null) }
    }

    fun toggleBookmark(bookmark: Bookmark) {
        _uiState.update {
            val key = bookmark.deviceKey()
            it.copy(
                selected = if (key in it.selected) it.selected - key else it.selected + key,
            )
        }
    }

    /**
     * Save the (possibly renamed) category and reassign bookmark.category fields.
     * Bookmarks newly checked → set their category to [name]. Bookmarks that were
     * previously checked but now unchecked → blank their category.
     */
    fun save(reservedAllLabel: String) = viewModelScope.launch {
        val state = _uiState.value
        val newName = state.name.trim()
        if (newName.isBlank()) {
            _uiState.update { it.copy(nameError = NameError.Blank) }
            return@launch
        }
        if (newName == reservedAllLabel) {
            _uiState.update { it.copy(nameError = NameError.Reserved) }
            return@launch
        }

        // Rename: delete old + add new (Category has no ID; name is the key).
        if (state.initialName != null && state.initialName != newName) {
            repository.deleteCategory(state.initialName)
        }
        repository.addCategory(Category(newName))

        val previouslySelected = state.bookmarks
            .filter { it.category == state.initialName }
            .map { it.deviceKey() }
            .toSet()
        for (bookmark in state.bookmarks) {
            val key = bookmark.deviceKey()
            val isSelected = key in state.selected
            val wasSelected = key in previouslySelected
            when {
                isSelected && bookmark.category != newName ->
                    repository.editBookmark(bookmark.copy(category = newName))

                !isSelected && wasSelected ->
                    repository.editBookmark(bookmark.copy(category = ""))
            }
        }

        _events.emit(CategoryEditEvent.Saved)
    }
}

sealed interface CategoryEditEvent {
    data object Saved : CategoryEditEvent
}
