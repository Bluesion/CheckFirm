package com.illusion.checkfirm.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.storageKey
import com.illusion.checkfirm.core.domain.model.Device
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

fun BookmarkEntity.asExternalModel() = Bookmark(
    name = name,
    id = id,
    position = position,
    device = Device(model, csc),
    category = category,
)

fun Bookmark.asEntity() = BookmarkEntity(
    id = id,
    name = name,
    model = device.model,
    csc = device.csc,
    device = device.storageKey,
    category = category,
    position = position
)