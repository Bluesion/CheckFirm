package com.illusion.checkfirm.feature.settings.backuprestore

import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.Category
import com.illusion.checkfirm.core.domain.model.Device
import kotlinx.serialization.Serializable
import kotlinx.coroutines.flow.first

/**
 * Backup-file schema. Kept as a feature-local DTO so the domain models stay
 * pure data classes; only this layer cares about serialization.
 *
 * The JSON shape mirrors the legacy XML app's [BackupRestoreItem] so files
 * exported from one version can be restored on the other.
 */
@Serializable
internal data class BackupItem(
    val bookmarkList: List<BookmarkDto> = emptyList(),
    val categoryList: List<CategoryDto> = emptyList(),
)

@Serializable
internal data class BookmarkDto(
    val name: String,
    val model: String,
    val csc: String,
    val category: String,
    val id: Long? = null,
    val device: String = "DeviceItem(model=$model, csc=$csc)",
    val position: Int = 0,
) {
    fun toDomain() = Bookmark(name, Device(model, csc), category, id, position)

    companion object {
        fun fromDomain(b: Bookmark) = BookmarkDto(b.name, b.device.model, b.device.csc, b.category, b.id, position = b.position)
    }
}

@Serializable
internal data class CategoryDto(val name: String, val id: Long? = null, val position: Int = 0) {
    fun toDomain() = Category(name, id, position)

    companion object {
        fun fromDomain(c: Category) = CategoryDto(c.name, c.id, c.position)
    }
}

/** Merge by logical identity. Foreign row ids must never replace unrelated local records. */
internal suspend fun restoreBackup(repository: com.illusion.checkfirm.core.domain.repository.BCRepository, backup: BackupItem) {
    val bookmarks = repository.getAllBookmark("time", false).first()
    val categories = repository.getAllCategory().first()
    backup.categoryList.forEach { item ->
        val existing = categories.firstOrNull { it.name == item.name }
        val category = item.toDomain().copy(id = existing?.id ?: item.id.takeIf { categories.isEmpty() })
        if (existing == null) repository.addCategory(category) else repository.editCategory(category)
    }
    // Row-id order is the legacy time order. Insert in ascending order when ids must be remapped.
    backup.bookmarkList.sortedBy { it.id ?: Long.MAX_VALUE }.forEach { item ->
        val existing = bookmarks.firstOrNull { it.device == item.toDomain().device }
        val bookmark = item.toDomain().copy(id = existing?.id ?: item.id.takeIf { bookmarks.isEmpty() })
        if (existing == null) repository.addBookmark(bookmark) else repository.editBookmark(bookmark)
    }
}
