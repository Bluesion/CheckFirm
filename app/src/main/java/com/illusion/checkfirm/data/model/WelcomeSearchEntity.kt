package com.illusion.checkfirm.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "welcome_search_device", indices = [Index(value = ["device"], unique = true)])
class WelcomeSearchEntity(@PrimaryKey(autoGenerate = true) var id: Long?,
                        @ColumnInfo(name = "model") var model: String,
                        @ColumnInfo(name = "csc") var csc: String,
                        @ColumnInfo(name = "device") var device: String)