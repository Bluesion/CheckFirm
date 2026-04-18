package com.illusion.checkfirm.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.illusion.checkfirm.domain.model.Device

@Entity(tableName = "welcome_search_device", indices = [Index(value = ["device"], unique = true)])
class WelcomeSearchEntity(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "model") var model: String,
    @ColumnInfo(name = "csc") var csc: String,
    @ColumnInfo(name = "device") var device: String
)

fun WelcomeSearchEntity.asExternalModel() = Device(
    model = model,
    csc = csc
)

fun Device.toWelcomeSearchEntity(): WelcomeSearchEntity {
    return WelcomeSearchEntity(
        id = null,
        model = model,
        csc = csc,
        device = toString()
    )
}