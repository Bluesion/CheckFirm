package com.illusion.checkfirm.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.illusion.checkfirm.domain.model.Category
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "category_info", indices = [Index(value = ["name"], unique = true)])
class CategoryEntity(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "position") var position: Int
)

fun CategoryEntity.asExternalModel() = Category(
    name = name,
)

fun Category.asEntity() = CategoryEntity(
    id = null,
    name = name,
    position = 0
)