package com.illusion.checkfirm.feature.category.impl

import com.illusion.checkfirm.core.domain.model.Bookmark

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

fun Bookmark.deviceKey() = DeviceKey(device.toString())

sealed interface CategoryEditEvent {
    data object Saved : CategoryEditEvent
}
