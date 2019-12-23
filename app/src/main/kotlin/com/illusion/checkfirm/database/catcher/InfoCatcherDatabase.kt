package com.illusion.checkfirm.database.catcher

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [InfoCatcherEntity::class], version = 1, exportSchema = false)
abstract class InfoCatcherDatabase : RoomDatabase() {

    abstract fun catcherDao(): InfoCatcherDao

    companion object {
        @Volatile
        private var INSTANCE: InfoCatcherDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): InfoCatcherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, InfoCatcherDatabase::class.java, "catcher_database").build()
                INSTANCE = instance
                instance
            }
        }
    }
}