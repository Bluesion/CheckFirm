package com.illusion.checkfirm.database.bookmark

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_info", indices = [Index(value = ["name", "device"], unique = true)])
class BookmarkEntity(@PrimaryKey(autoGenerate = true) var id: Long?,
                     @ColumnInfo(name = "name") var name: String,
                     @ColumnInfo(name = "model") var model: String,
                     @ColumnInfo(name = "csc") var csc: String,
                     @ColumnInfo(name = "device") var device: String)