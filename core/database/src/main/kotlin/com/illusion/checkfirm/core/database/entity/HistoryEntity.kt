package com.illusion.checkfirm.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.illusion.checkfirm.core.domain.model.Date
import com.illusion.checkfirm.core.domain.model.Device
import com.illusion.checkfirm.core.domain.model.SearchHistory

@Entity(tableName = "history_info", indices = [Index(value = ["model", "csc"], unique = true)])
class HistoryEntity(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "model") var model: String,
    @ColumnInfo(name = "csc") var csc: String,
    @ColumnInfo(name = "year") var year: Int,
    @ColumnInfo(name = "month") var month: Int,
    @ColumnInfo(name = "day") var day: Int
)

fun HistoryEntity.asExternalModel() = SearchHistory(
    device = Device(model, csc),
    date = Date(year, month, day),
)

fun SearchHistory.asEntity(): HistoryEntity {
    return HistoryEntity(
        id = null,
        model = device.model,
        csc = device.csc,
        year = date.year,
        month = date.month,
        day = date.day,
    )
}
