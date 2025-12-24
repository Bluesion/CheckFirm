package com.illusion.checkfirm.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "bookmark_info", indices = [Index(value = ["name", "device"], unique = true)])
class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "model") var model: String,
    @ColumnInfo(name = "csc") var csc: String,
    @ColumnInfo(name = "device") var device: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "position") var position: Int
)