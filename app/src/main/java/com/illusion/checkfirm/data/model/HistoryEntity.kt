package com.illusion.checkfirm.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "history_info", indices = [Index(value = ["model", "csc"], unique = true)])
class HistoryEntity(@PrimaryKey(autoGenerate = true) var id: Long?,
                    @ColumnInfo(name = "model") var model: String,
                    @ColumnInfo(name = "csc") var csc: String,
                    @ColumnInfo(name = "year") var year: Int,
                    @ColumnInfo(name = "month") var month: Int,
                    @ColumnInfo(name = "day") var day: Int)