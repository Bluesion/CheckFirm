package com.illusion.checkfirm.core.data.model

import com.illusion.checkfirm.core.database.entity.BookmarkEntity
import com.illusion.checkfirm.core.database.entity.CategoryEntity
import kotlinx.serialization.Serializable

@Serializable
data class BackupRestoreFileModel(
    var bookmarkList: List<BookmarkEntity> = emptyList(),
    var categoryList: List<CategoryEntity> = emptyList()
)