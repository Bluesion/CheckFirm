package com.illusion.checkfirm.data.local.model

import com.illusion.checkfirm.data.local.entity.BookmarkEntity
import com.illusion.checkfirm.data.local.entity.CategoryEntity
import kotlinx.serialization.Serializable

@Serializable
data class BackupRestoreFileModel(
    var bookmarkList: List<BookmarkEntity> = emptyList(),
    var categoryList: List<CategoryEntity> = emptyList()
)