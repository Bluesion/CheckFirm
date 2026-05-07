package com.illusion.checkfirm.feature.settings.backuprestore

import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Category
import com.illusion.checkfirm.domain.model.Device
import kotlinx.serialization.Serializable

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
) {
    fun toDomain() = Bookmark(name, Device(model, csc), category)

    companion object {
        fun fromDomain(b: Bookmark) = BookmarkDto(b.name, b.device.model, b.device.csc, b.category)
    }
}

@Serializable
internal data class CategoryDto(val name: String) {
    fun toDomain() = Category(name)

    companion object {
        fun fromDomain(c: Category) = CategoryDto(c.name)
    }
}
