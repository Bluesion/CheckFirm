package com.illusion.checkfirm.data.model.local

import kotlinx.serialization.Serializable

@Serializable
data class BackupRestoreItem(
    var bookmarkList: List<BookmarkEntity> = emptyList(),
    var categoryList: List<CategoryEntity> = emptyList()
)