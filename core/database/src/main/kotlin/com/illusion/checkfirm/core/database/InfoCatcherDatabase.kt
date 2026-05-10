package com.illusion.checkfirm.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.illusion.checkfirm.core.database.dao.InfoCatcherDao
import com.illusion.checkfirm.core.database.entity.InfoCatcherEntity

@Database(entities = [InfoCatcherEntity::class], version = 1, exportSchema = false)
abstract class InfoCatcherDatabase : RoomDatabase() {

    abstract fun infoCatcherDao(): InfoCatcherDao

    companion object {
        @Volatile
        private var INSTANCE: InfoCatcherDatabase? = null

        fun getDatabase(context: Context): InfoCatcherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InfoCatcherDatabase::class.java,
                    "catcher_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}