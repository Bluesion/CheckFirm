package com.illusion.checkfirm.database.catcher

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "catcher_device", indices = [Index(value = ["device"], unique = true)])
class InfoCatcherEntity(@PrimaryKey(autoGenerate = true) var id: Long?,
                        @ColumnInfo(name = "model") var model: String,
                        @ColumnInfo(name = "csc") var csc: String,
                        @ColumnInfo(name = "device") var device: String)